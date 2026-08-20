# Student Grade Manager

[![Java CI - Maven](https://github.com/dukandu4112/Student-Grade-Manager/actions/workflows/maven.yml/badge.svg)](https://github.com/dukandu4112/Student-Grade-Manager/actions/workflows/maven.yml)

A portfolio-ready Java 17 command-line application for managing students and academic grades with persistent SQLite storage, reliable validation, analytics, CSV export, automated tests, and GitHub Actions CI.

## Features

- Add, rename, search, list, and delete students.
- Record multiple grades per student and organize them by subject.
- View complete grade history for an individual student.
- Edit or delete individual grade records.
- Calculate per-student and class-wide averages.
- Convert numeric averages to A/B/C/D/F letter grades.
- Show each student's highest and lowest recorded grade.
- Sort the roster by name, highest average, or lowest average.
- Export the complete student/grade dataset to CSV.
- Persist data automatically in SQLite with foreign-key cascading deletes.
- Validate names, numeric input, grade ranges, IDs, and destructive actions.
- Run automated JUnit 5 tests in GitHub Actions on every push and pull request.

## Requirements

- Java 17 or newer
- Maven 3.6+

SQLite JDBC and JUnit are installed automatically by Maven.

## Build

```bash
git clone https://github.com/dukandu4112/Student-Grade-Manager.git
cd Student-Grade-Manager
mvn clean verify
```

Create the executable JAR:

```bash
mvn clean package
```

Run it:

```bash
java -jar target/student-grade-manager.jar
```

The application creates `students.db` in the working directory on first run. That local database is intentionally excluded from Git.

## Menu

```text
1. Add student
2. Add grade
3. View student details and grade history
4. Show all students
5. Edit grade
6. Delete grade
7. Rename student
8. Delete student
9. Search student
10. Show class average
11. Sort by highest average
12. Sort by lowest average
13. Export CSV
14. Exit
```

## Project Structure

```text
Student-Grade-Manager/
├── .github/workflows/maven.yml
├── docs/
│   └── ARCHITECTURE.md
├── src/
│   ├── main/java/com/grademanager/
│   │   ├── Main.java
│   │   ├── ConsoleApp.java
│   │   ├── DatabaseManager.java
│   │   ├── GradeManager.java
│   │   ├── Student.java
│   │   ├── GradeRecord.java
│   │   ├── GradeScale.java
│   │   └── CsvService.java
│   └── test/java/com/grademanager/
│       ├── DatabaseManagerTest.java
│       ├── GradeScaleTest.java
│       ├── GradeRecordTest.java
│       └── StudentTest.java
├── .gitignore
├── CONTRIBUTING.md
├── LICENSE
├── README.md
└── pom.xml
```

## Database Schema

```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE COLLATE NOCASE,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    grade REAL NOT NULL CHECK (grade >= 0 AND grade <= 100),
    subject TEXT NOT NULL DEFAULT 'General',
    recorded_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

## Grade Scale

| Range | Letter |
|---:|:---:|
| 90-100 | A |
| 80-89.99 | B |
| 70-79.99 | C |
| 60-69.99 | D |
| 0-59.99 | F |

## Testing

```bash
mvn test
```

The test suite covers grade boundaries, input/model validation, SQLite CRUD operations, searching, averages, highest/lowest analytics, updates, cascading deletion, and duplicate-name handling.

## Design Notes

The application separates responsibilities into model classes, a database access layer, business logic, console UI, and export service. SQL uses prepared statements, database foreign keys are enabled, generated runtime files are ignored, and the Maven Shade plugin creates one executable JAR without generating a tracked `dependency-reduced-pom.xml`.

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for more detail.

## Version

**Version:** 1.0.0  
**Status:** Complete portfolio CLI release

## License

MIT License. See [LICENSE](LICENSE).

## Author

**dukandu4112** — Computer Science student and project author.
