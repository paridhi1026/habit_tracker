# Habit Tracker Desktop Application

A beginner-friendly Java Swing desktop app for tracking daily and weekly habits with SQLite persistence.

## Project Structure

```text
HabitTracker/
+-- lib/
|   +-- sqlite-jdbc-<version>.jar
+-- src/
|   +-- main/
|       +-- java/
|           +-- com/
|               +-- habittracker/
|                   +-- Main.java
|                   +-- dao/
|                   |   +-- Database.java
|                   |   +-- HabitDao.java
|                   |   +-- HabitLogDao.java
|                   |   +-- UserDao.java
|                   +-- model/
|                   |   +-- Habit.java
|                   |   +-- HabitLog.java
|                   |   +-- User.java
|                   +-- service/
|                   |   +-- PasswordService.java
|                   |   +-- ProgressService.java
|                   |   +-- StreakService.java
|                   +-- ui/
|                       +-- DashboardFrame.java
|                       +-- HabitDialog.java
|                       +-- LoginFrame.java
+-- run.ps1
+-- README.md
```

## Features

- Register and login with username and password.
- Passwords are stored as PBKDF2 hashes with random salt.
- Add, edit, and delete habits.
- Track Daily or Weekly habits.
- Mark a habit as complete for today.
- See today's status, current streak, longest streak, and monthly completion percentage in a JTable.
- Rows with a current streak of 3 or more are highlighted.

## Database

The app creates `habit_tracker.db` automatically in the project root.

Tables:

- `User(id, username, password)`
- `Habit(id, user_id, name, frequency)`
- `HabitLog(id, habit_id, date, status)`

## How to Run

1. Install JDK 17 or newer.
2. Download SQLite JDBC from Maven Central:
   `https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/`
3. Place the jar file in the `lib` folder.
   Example: `lib/sqlite-jdbc-3.45.3.0.jar`
4. From this folder, run:

```powershell
.\run.ps1
```

The script compiles all Java files into `out` and starts the app.

## Manual Compile and Run

```powershell
javac -cp "lib/*" -d out (Get-ChildItem -Recurse src/main/java/*.java).FullName
java -cp "out;lib/*" com.habittracker.Main
```

## Code Walkthrough

- `Main.java` initializes the database and opens the login screen.
- `Database.java` creates the SQLite tables if they do not exist.
- `UserDao.java`, `HabitDao.java`, and `HabitLogDao.java` contain JDBC operations.
- `PasswordService.java` hashes and verifies passwords.
- `StreakService.java` calculates current and longest streaks.
- `ProgressService.java` calculates a simple monthly completion percentage.
- `LoginFrame.java` handles register/login.
- `DashboardFrame.java` displays habits in a JTable and contains the main actions.
- `HabitDialog.java` is reused for adding and editing habits.
