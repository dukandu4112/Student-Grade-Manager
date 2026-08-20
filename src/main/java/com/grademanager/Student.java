package com.grademanager;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Student {
    private final int id;
    private final String name;
    private final LocalDateTime createdAt;

    public Student(int id, String name, LocalDateTime createdAt) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be blank.");
        }
        this.id = id;
        this.name = name.trim();
        this.createdAt = Objects.requireNonNullElseGet(createdAt, LocalDateTime::now);
    }

    public Student(String name) {
        this(-1, name, LocalDateTime.now());
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return id > 0 ? id + ". " + name : name;
    }
}
