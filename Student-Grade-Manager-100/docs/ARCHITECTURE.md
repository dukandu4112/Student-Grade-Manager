# Architecture

## Overview

Student Grade Manager uses a small layered design so each part of the program has one clear responsibility.

- `Main` owns application startup and shutdown.
- `ConsoleApp` owns terminal interaction and input handling.
- `GradeManager` exposes application-level operations.
- `DatabaseManager` owns SQLite schema creation and SQL operations.
- `Student` and `GradeRecord` model persisted data.
- `GradeScale` centralizes numeric-to-letter grade conversion.
- `CsvService` owns export formatting.

## Data Flow

```text
User
  ↓
ConsoleApp
  ↓
GradeManager
  ↓
DatabaseManager
  ↓
SQLite
```

## Persistence

The SQLite database is created automatically. Foreign keys are enabled per connection, and deleting a student cascades to that student's grade records. Runtime database files are not committed to source control.

## Validation

Validation exists both in Java and in the database schema. Grade values must remain between 0 and 100. Student names cannot be blank, and names are unique without regard to case.

## Build and CI

Maven compiles Java 17 source, runs JUnit 5 tests, and packages a shaded executable JAR. GitHub Actions runs `mvn -B clean verify` for pushes and pull requests targeting `main`.
