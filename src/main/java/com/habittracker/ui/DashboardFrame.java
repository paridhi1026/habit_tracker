package com.habittracker.ui;

import com.habittracker.dao.HabitDao;
import com.habittracker.dao.HabitLogDao;
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.service.ProgressService;
import com.habittracker.service.StreakService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final User user;
    private final HabitDao habitDao = new HabitDao();
    private final HabitLogDao habitLogDao = new HabitLogDao();
    private final StreakService streakService = new StreakService();
    private final ProgressService progressService = new ProgressService();
    private final DefaultTableModel tableModel;
    private final JTable habitTable;
    private final JLabel messageLabel = new JLabel("Welcome!");
    private List<Habit> habits;

    public DashboardFrame(User user) {
        this.user = user;
        setTitle("Habit Tracker - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 500);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Habit", "Frequency", "Today", "Current Streak", "Longest Streak", "Progress"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        habitTable = new JTable(tableModel);
        habitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habitTable.removeColumn(habitTable.getColumnModel().getColumn(0));
        habitTable.setRowHeight(28);
        habitTable.setDefaultRenderer(Object.class, new StreakCellRenderer());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(new JScrollPane(habitTable), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 8, 14));

        JLabel titleLabel = new JLabel("Hello, " + user.getUsername());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));

        messageLabel.setHorizontalAlignment(JLabel.RIGHT);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(event -> addHabit());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(event -> editHabit());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> deleteHabit());

        JButton completeButton = new JButton("Mark Complete");
        completeButton.addActionListener(event -> markComplete());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(event -> logout());

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(completeButton);
        panel.add(logoutButton);

        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        habits = habitDao.findByUserId(user.getId());

        // Build each row from the habit plus calculated status, streaks, and progress.
        for (Habit habit : habits) {
            boolean doneToday = habitLogDao.isCompletedOnDate(habit.getId(), LocalDate.now());
            int currentStreak = streakService.calculateCurrentStreak(habit.getId(), habit.getFrequency());
            int longestStreak = streakService.calculateLongestStreak(habit.getId(), habit.getFrequency());
            int progress = progressService.calculateCompletionPercentage(habit.getId(), habit.getFrequency());

            tableModel.addRow(new Object[]{
                    habit.getId(),
                    habit.getName(),
                    habit.getFrequency(),
                    doneToday ? "Done" : "Not Done",
                    currentStreak,
                    longestStreak,
                    progress + "%"
            });
        }
    }

    private void addHabit() {
        HabitDialog dialog = new HabitDialog(this, null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            Habit habit = new Habit(user.getId(), dialog.getHabitName(), dialog.getFrequency());
            habitDao.addHabit(habit);
            refreshTable();
        }
    }

    private void editHabit() {
        Habit selectedHabit = getSelectedHabit();
        if (selectedHabit == null) {
            JOptionPane.showMessageDialog(this, "Please select a habit to edit.");
            return;
        }

        HabitDialog dialog = new HabitDialog(this, selectedHabit);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            selectedHabit.setName(dialog.getHabitName());
            selectedHabit.setFrequency(dialog.getFrequency());
            habitDao.updateHabit(selectedHabit);
            refreshTable();
        }
    }

    private void deleteHabit() {
        Habit selectedHabit = getSelectedHabit();
        if (selectedHabit == null) {
            JOptionPane.showMessageDialog(this, "Please select a habit to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete habit '" + selectedHabit.getName() + "'?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            habitDao.deleteHabit(selectedHabit.getId(), user.getId());
            refreshTable();
        }
    }

    private void markComplete() {
        Habit selectedHabit = getSelectedHabit();
        if (selectedHabit == null) {
            JOptionPane.showMessageDialog(this, "Please select a habit to mark complete.");
            return;
        }

        habitLogDao.markCompleted(selectedHabit.getId(), LocalDate.now());
        int streak = streakService.calculateCurrentStreak(selectedHabit.getId(), selectedHabit.getFrequency());
        messageLabel.setText(streakService.buildStreakMessage(streak));
        refreshTable();
    }

    private Habit getSelectedHabit() {
        int selectedRow = habitTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }

        int modelRow = habitTable.convertRowIndexToModel(selectedRow);
        int habitId = (int) tableModel.getValueAt(modelRow, 0);

        for (Habit habit : habits) {
            if (habit.getId() == habitId) {
                return habit;
            }
        }

        return null;
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    private static class StreakCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                component.setBackground(Color.WHITE);
                int streak = Integer.parseInt(table.getValueAt(row, 3).toString());
                if (streak >= 3) {
                    component.setBackground(new Color(255, 244, 214));
                }
            }

            return component;
        }
    }
}
