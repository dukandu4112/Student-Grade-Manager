package com.grademanager;

import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DatabaseManager implements AutoCloseable {
    private final Connection connection;

    public DatabaseManager(Path databasePath) throws SQLException {
        this("jdbc:sqlite:" + databasePath.toAbsolutePath());
    }

    DatabaseManager(String jdbcUrl) throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        initializeSchema();
    }

    private void initializeSchema() throws SQLException {
        String studentsSql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE COLLATE NOCASE,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """;
        String gradesSql = """
                CREATE TABLE IF NOT EXISTS grades (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    grade REAL NOT NULL CHECK (grade >= 0 AND grade <= 100),
                    subject TEXT NOT NULL DEFAULT 'General',
                    recorded_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
                )
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(studentsSql);
            statement.execute(gradesSql);
            statement.execute("CREATE INDEX IF NOT EXISTS idx_grades_student_id ON grades(student_id)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_grades_subject ON grades(subject)");
        }
    }

    public Student addStudent(String name) throws SQLException {
        String cleaned = requireName(name);
        String sql = "INSERT INTO students(name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cleaned);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Student was inserted but no ID was returned.");
                return findStudentById(keys.getInt(1)).orElseThrow();
            }
        }
    }

    public Optional<Student> findStudentById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, name, created_at FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapStudent(rs)) : Optional.empty();
            }
        }
    }

    public List<Student> searchStudents(String query) throws SQLException {
        String q = query == null ? "" : query.trim();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, name, created_at FROM students WHERE name LIKE ? COLLATE NOCASE ORDER BY name COLLATE NOCASE")) {
            ps.setString(1, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Student> results = new ArrayList<>();
                while (rs.next()) results.add(mapStudent(rs));
                return results;
            }
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id, name, created_at FROM students ORDER BY name COLLATE NOCASE");
             ResultSet rs = ps.executeQuery()) {
            List<Student> students = new ArrayList<>();
            while (rs.next()) students.add(mapStudent(rs));
            return students;
        }
    }

    public boolean renameStudent(int studentId, String newName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE students SET name = ? WHERE id = ?")) {
            ps.setString(1, requireName(newName));
            ps.setInt(2, studentId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    public GradeRecord addGrade(int studentId, double grade, String subject) throws SQLException {
        validateGrade(grade);
        if (findStudentById(studentId).isEmpty()) throw new IllegalArgumentException("Student ID does not exist: " + studentId);
        String normalizedSubject = normalizeSubject(subject);
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO grades(student_id, grade, subject) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, studentId);
            ps.setDouble(2, grade);
            ps.setString(3, normalizedSubject);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Grade was inserted but no ID was returned.");
                return findGradeById(keys.getInt(1)).orElseThrow();
            }
        }
    }

    public Optional<GradeRecord> findGradeById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, student_id, grade, subject, recorded_at FROM grades WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapGrade(rs)) : Optional.empty();
            }
        }
    }

    public List<GradeRecord> getGradesForStudent(int studentId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id, student_id, grade, subject, recorded_at FROM grades WHERE student_id = ? ORDER BY recorded_at, id")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<GradeRecord> grades = new ArrayList<>();
                while (rs.next()) grades.add(mapGrade(rs));
                return grades;
            }
        }
    }

    public boolean updateGrade(int gradeId, double grade, String subject) throws SQLException {
        validateGrade(grade);
        try (PreparedStatement ps = connection.prepareStatement("UPDATE grades SET grade = ?, subject = ? WHERE id = ?")) {
            ps.setDouble(1, grade);
            ps.setString(2, normalizeSubject(subject));
            ps.setInt(3, gradeId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteGrade(int gradeId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM grades WHERE id = ?")) {
            ps.setInt(1, gradeId);
            return ps.executeUpdate() == 1;
        }
    }

    public double getStudentAverage(int studentId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT AVG(grade) FROM grades WHERE student_id = ?")) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0.0;
                double value = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : value;
            }
        }
    }

    public double getClassAverage() throws SQLException {
        try (Statement s = connection.createStatement(); ResultSet rs = s.executeQuery("SELECT AVG(grade) FROM grades")) {
            if (!rs.next()) return 0.0;
            double value = rs.getDouble(1);
            return rs.wasNull() ? 0.0 : value;
        }
    }

    public Optional<GradeRecord> getHighestGradeForStudent(int studentId) throws SQLException {
        return getExtremeGrade(studentId, "DESC");
    }

    public Optional<GradeRecord> getLowestGradeForStudent(int studentId) throws SQLException {
        return getExtremeGrade(studentId, "ASC");
    }

    private Optional<GradeRecord> getExtremeGrade(int studentId, String direction) throws SQLException {
        String sql = "SELECT id, student_id, grade, subject, recorded_at FROM grades WHERE student_id = ? ORDER BY grade " + direction + ", id ASC LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapGrade(rs)) : Optional.empty();
            }
        }
    }

    public List<StudentSummary> getStudentSummaries(String sort) throws SQLException {
        String orderBy = switch (sort == null ? "name" : sort.toLowerCase()) {
            case "average_desc" -> "average_grade DESC, s.name COLLATE NOCASE";
            case "average_asc" -> "average_grade ASC, s.name COLLATE NOCASE";
            default -> "s.name COLLATE NOCASE";
        };
        String sql = """
                SELECT s.id, s.name, COUNT(g.id) AS grade_count, COALESCE(AVG(g.grade), 0) AS average_grade
                FROM students s
                LEFT JOIN grades g ON g.student_id = s.id
                GROUP BY s.id, s.name
                ORDER BY %s
                """.formatted(orderBy);
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<StudentSummary> summaries = new ArrayList<>();
            while (rs.next()) {
                summaries.add(new StudentSummary(rs.getInt("id"), rs.getString("name"), rs.getInt("grade_count"), rs.getDouble("average_grade")));
            }
            return summaries;
        }
    }

    private static Student mapStudent(ResultSet rs) throws SQLException {
        String raw = rs.getString("created_at");
        LocalDateTime createdAt = parseDateTime(raw);
        return new Student(rs.getInt("id"), rs.getString("name"), createdAt);
    }

    private static GradeRecord mapGrade(ResultSet rs) throws SQLException {
        return new GradeRecord(
                rs.getInt("id"), rs.getInt("student_id"), rs.getDouble("grade"), rs.getString("subject"), parseDateTime(rs.getString("recorded_at")));
    }

    private static LocalDateTime parseDateTime(String raw) {
        if (raw == null || raw.isBlank()) return LocalDateTime.now();
        return LocalDateTime.parse(raw.replace(' ', 'T'));
    }

    private static String requireName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Student name cannot be blank.");
        return name.trim();
    }

    private static String normalizeSubject(String subject) {
        return subject == null || subject.trim().isEmpty() ? "General" : subject.trim();
    }

    private static void validateGrade(double grade) {
        if (!Double.isFinite(grade) || grade < 0 || grade > 100) throw new IllegalArgumentException("Grade must be between 0 and 100.");
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    public record StudentSummary(int id, String name, int gradeCount, double averageGrade) {
        public String letterGrade() {
            return gradeCount == 0 ? "N/A" : GradeScale.letterGrade(averageGrade);
        }
    }
}
