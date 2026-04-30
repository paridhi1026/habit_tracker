package com.habittracker.service;

import com.habittracker.dao.HabitLogDao;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreakService {
    private final HabitLogDao habitLogDao = new HabitLogDao();

    public int calculateCurrentStreak(int habitId, String frequency) {
        List<LocalDate> completedDates = habitLogDao.findCompletedDates(habitId);
        Set<LocalDate> completedDateSet = new HashSet<>(completedDates);

        int streak = 0;
        LocalDate date = LocalDate.now();

        while (completedDateSet.contains(date)) {
            streak++;
            date = previousExpectedDate(date, frequency);
        }

        return streak;
    }

    public int calculateLongestStreak(int habitId, String frequency) {
        List<LocalDate> completedDates = habitLogDao.findCompletedDates(habitId);
        if (completedDates.isEmpty()) {
            return 0;
        }

        int longest = 1;
        int current = 1;

        for (int i = 1; i < completedDates.size(); i++) {
            LocalDate previous = completedDates.get(i - 1);
            LocalDate currentDate = completedDates.get(i);

            if (currentDate.equals(nextExpectedDate(previous, frequency))) {
                current++;
            } else {
                current = 1;
            }

            longest = Math.max(longest, current);
        }

        return longest;
    }

    private LocalDate previousExpectedDate(LocalDate date, String frequency) {
        if ("Weekly".equalsIgnoreCase(frequency)) {
            return date.minusWeeks(1);
        }
        return date.minusDays(1);
    }

    private LocalDate nextExpectedDate(LocalDate date, String frequency) {
        if ("Weekly".equalsIgnoreCase(frequency)) {
            return date.plusWeeks(1);
        }
        return date.plusDays(1);
    }

    public String buildStreakMessage(int streak) {
        if (streak <= 0) {
            return "Mark a habit complete to start a streak.";
        }
        return "Great job! \uD83D\uDD25 " + streak + "-day streak!";
    }
}
