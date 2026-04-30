package com.habittracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitLogDao {

    public void markCompleted(int habitId, LocalDate date) {
        // One log per habit per day. If it already exists, keep it marked as Done.
        String sql = "INSERT INTO HabitLog (habit_id, date, status) " +
                     "VALUES (?, ?, 'Done') " +
                     "ON CONFLICT(habit_id, date) DO UPDATE SET status = 'Done'";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, habitId);
            statement.setString(2, date.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not mark habit completed.", e);
        }
    }

    public boolean isCompletedOnDate(int habitId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM HabitLog WHERE habit_id = ? AND date = ? AND status = 'Done'";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, habitId);
            statement.setString(2, date.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not check habit status.", e);
        }
    }

    public List<LocalDate> findCompletedDates(int habitId) {
        String sql = "SELECT date FROM HabitLog WHERE habit_id = ? AND status = 'Done' ORDER BY date";
        List<LocalDate> dates = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dates.add(LocalDate.parse(resultSet.getString("date")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not load completion dates.", e);
        }

        return dates;
    }

    public int countCompletedLogs(int habitId) {
        String sql = "SELECT COUNT(*) FROM HabitLog WHERE habit_id = ? AND status = 'Done'";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not count completed logs.", e);
        }
    }
}