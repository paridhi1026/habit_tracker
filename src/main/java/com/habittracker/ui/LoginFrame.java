package com.habittracker.ui;

import com.habittracker.dao.UserDao;
import com.habittracker.model.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final UserDao userDao = new UserDao();

    public LoginFrame() {
        setTitle("Habit Tracker - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 240);
        setLocationRelativeTo(null);
        setResizable(false);

        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Username"), constraints);

        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Password"), constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(event -> login());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(event -> register());

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(registerButton, constraints);

        constraints.gridx = 1;
        panel.add(loginButton, constraints);

        return panel;
    }

    private void login() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (!isInputValid(username, password)) {
            return;
        }

        User user = userDao.login(username, password);
        clearPassword(password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
            return;
        }

        new DashboardFrame(user).setVisible(true);
        dispose();
    }

    private void register() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (!isInputValid(username, password)) {
            return;
        }

        boolean created = userDao.register(username, password);
        clearPassword(password);

        if (created) {
            JOptionPane.showMessageDialog(this, "Registration successful. You can login now.");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Try another one.");
        }
    }

    private boolean isInputValid(String username, char[] password) {
        if (username.isBlank() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return false;
        }

        if (password.length < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.");
            return false;
        }

        return true;
    }

    private void clearPassword(char[] password) {
        Arrays.fill(password, '\0');
        passwordField.setText("");
    }
}
