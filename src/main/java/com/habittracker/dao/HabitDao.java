package com.habittracker.dao;

import com.habittracker.model.Habit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HabitDao {
    public void addHabit(Habit habit) {
        String sql = "INSERT INTO Habit (user_id, name, frequency) VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, habit.getUserId());
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getFrequency());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not add habit.", e);
        }
    }

    public void updateHabit(Habit habit) {
        String sql = "UPDATE Habit SET name = ?, frequency = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getFrequency());
            statement.setInt(3, habit.getId());
            statement.setInt(4, habit.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update habit.", e);
        }
    }

    public void deleteHabit(int habitId, int userId) {
        String sql = "DELETE FROM Habit WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, habitId);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete habit.", e);
        }
    }

    public List<Habit> findByUserId(int userId) {
        String sql = "SELECT id, user_id, name, frequency FROM Habit WHERE user_id = ? ORDER BY name";
        List<Habit> habits = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    habits.add(new Habit(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("name"),
                            resultSet.getString("frequency")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not load habits.", e);
        }

        return habits;
    }
}
