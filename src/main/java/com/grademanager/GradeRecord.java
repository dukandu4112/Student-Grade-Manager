package com.grademanager;

import java.time.LocalDateTime;
import java.util.Objects;

public record GradeRecord(int id, int studentId, double grade, String subject, LocalDateTime recordedAt) {
    public GradeRecord {
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }
        subject = normalizeSubject(subject);
        recordedAt = Objects.requireNonNullElseGet(recordedAt, LocalDateTime::now);
    }

    public GradeRecord(int studentId, double grade, String subject) {
        this(-1, studentId, grade, subject, LocalDateTime.now());
    }

    private static String normalizeSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return "General";
        }
        return subject.trim();
    }
}
