package com.habittracker;

import com.habittracker.dao.Database;
import com.habittracker.ui.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {

        // 1. Set Look & Feel ONCE (before UI starts)
        try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Initialize DB (non-UI work)
        Database.initializeDatabase();

        // 3. Start UI on EDT
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}