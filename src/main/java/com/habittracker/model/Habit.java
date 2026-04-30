package com.habittracker.model;

public class Habit {
    private int id;
    private int userId;
    private String name;
    private String frequency;

    public Habit() {
    }

    public Habit(int id, int userId, String name, String frequency) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.frequency = frequency;
    }

    public Habit(int userId, String name, String frequency) {
        this.userId = userId;
        this.name = name;
        this.frequency = frequency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
