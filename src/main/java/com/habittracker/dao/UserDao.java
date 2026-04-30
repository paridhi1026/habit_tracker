package com.habittracker.dao;

import com.habittracker.model.User;
import com.habittracker.service.PasswordService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public boolean register(String username, char[] password) {
        String sql = "INSERT INTO User (username, password) VALUES (?, ?)";
        String hashedPassword = PasswordService.hashPassword(password);

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public User login(String username, char[] password) {
        String sql = "SELECT id, username, password FROM User WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (PasswordService.verifyPassword(password, storedPassword)) {
                        return new User(
                                resultSet.getInt("id"),
                                resultSet.getString("username"),
                                storedPassword
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
