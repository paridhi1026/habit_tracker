package com.habittracker.ui;

import com.habittracker.model.Habit;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class HabitDialog extends JDialog {
    private final JTextField nameField = new JTextField(20);
    private final JComboBox<String> frequencyComboBox = new JComboBox<>(new String[]{"Daily", "Weekly"});
    private boolean saved;

    public HabitDialog(DashboardFrame owner, Habit habit) {
        super(owner, habit == null ? "Add Habit" : "Edit Habit", true);
        setSize(340, 190);
        setLocationRelativeTo(owner);
        setResizable(false);

        if (habit != null) {
            nameField.setText(habit.getName());
            frequencyComboBox.setSelectedItem(habit.getFrequency());
        }

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getHabitName() {
        return nameField.getText().trim();
    }

    public String getFrequency() {
        return (String) frequencyComboBox.getSelectedItem();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 8, 18));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Habit name"), constraints);

        constraints.gridx = 1;
        panel.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Frequency"), constraints);

        constraints.gridx = 1;
        panel.add(frequencyComboBox, constraints);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        javax.swing.JButton cancelButton = new javax.swing.JButton("Cancel");
        cancelButton.addActionListener(event -> dispose());

        javax.swing.JButton saveButton = new javax.swing.JButton("Save");
        saveButton.addActionListener(event -> save());

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    private void save() {
        if (getHabitName().isBlank()) {
            JOptionPane.showMessageDialog(this, "Habit name is required.");
            return;
        }

        saved = true;
        dispose();
    }
}
