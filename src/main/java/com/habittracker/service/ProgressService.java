package com.habittracker.service;

import com.habittracker.dao.HabitLogDao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProgressService {
    private final HabitLogDao habitLogDao = new HabitLogDao();

    public int calculateCompletionPercentage(int habitId, String frequency) {
        int completed = habitLogDao.countCompletedLogs(habitId);
        long possible = calculatePossibleOccurrences(frequency);

        if (possible <= 0) {
            return 0;
        }

        int percentage = (int) Math.round((completed * 100.0) / possible);
        return Math.min(percentage, 100);
    }

    private long calculatePossibleOccurrences(String frequency) {
        // This beginner-friendly version tracks progress from the first day of the current month.
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        if ("Weekly".equalsIgnoreCase(frequency)) {
            long days = ChronoUnit.DAYS.between(startOfMonth, today) + 1;
            return Math.max(1, (long) Math.ceil(days / 7.0));
        }

        return ChronoUnit.DAYS.between(startOfMonth, today) + 1;
    }
}
