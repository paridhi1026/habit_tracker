package com.habittracker;

import com.habittracker.dao.Database;
import com.habittracker.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        Database.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Swing will use the default look and feel if the system one is unavailable.
            }

            new LoginFrame().setVisible(true);
        });
    }
}
