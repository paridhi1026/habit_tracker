package com.habittracker.model;

import java.time.LocalDate;

public class HabitLog {
    private int id;
    private int habitId;
    private LocalDate date;
    private String status;

    public HabitLog() {
    }

    public HabitLog(int id, int habitId, LocalDate date, String status) {
        this.id = id;
        this.habitId = habitId;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
