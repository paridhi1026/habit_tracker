package com.habittracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DATABASE_URL = "jdbc:sqlite:habit_tracker.db";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    public static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found. Add sqlite-jdbc jar.", e);
        }

        String createUserTable =
                "CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL" +
                ");";

        String createHabitTable =
                "CREATE TABLE IF NOT EXISTS Habit (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "frequency TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE" +
                ");";

        String createHabitLogTable =
                "CREATE TABLE IF NOT EXISTS HabitLog (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "habit_id INTEGER NOT NULL, " +
                "date TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "UNIQUE(habit_id, date), " +
                "FOREIGN KEY (habit_id) REFERENCES Habit(id) ON DELETE CASCADE" +
                ");";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(createUserTable);
            statement.execute(createHabitTable);
            statement.execute(createHabitLogTable);

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database.", e);
        }
    }
}