import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all database operations for the Student Grade Manager.
 * Uses SQLite for persistent data storage.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:grades.db";
    private Connection connection;

    /**
     * Initialize the database connection and create tables if they don't exist.
     */
    public void connect() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found. Please add sqlite-jdbc to your project.");
            System.out.println("Download from: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create the necessary database tables if they don't exist.
     */
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

            String createGradesTable = "CREATE TABLE IF NOT EXISTS grades (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "grade REAL NOT NULL," +
                    "subject TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE" +
                    ")";

            stmt.execute(createStudentsTable);
            stmt.execute(createGradesTable);
            System.out.println("Database tables created/verified.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a new student to the database.
     * @param name The name of the student
     * @return true if successful, false otherwise
     */
    public boolean addStudent(String name) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO students (name) VALUES (?)")) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("Error: Student '" + name + "' already exists.");
            } else {
                System.out.println("Error adding student: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Add a grade for a student.
     * @param studentId The student's ID
     * @param grade The grade value
     * @param subject The subject (optional)
     * @return true if successful, false otherwise
     */
    public boolean addGrade(int studentId, double grade, String subject) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO grades (student_id, grade, subject) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, studentId);
            pstmt.setDouble(2, grade);
            pstmt.setString(3, subject != null ? subject : "General");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all students from the database.
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM students ORDER BY id")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double average = getStudentAverage(id);
                Student student = new Student(name);
                student.setId(id);
                student.setGrade(average);
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Get the average grade for a specific student.
     * @param studentId The student's ID
     * @return The average grade, or 0 if no grades found
     */
    public double getStudentAverage(int studentId) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT AVG(grade) as average FROM grades WHERE student_id = ?")) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double average = rs.getDouble("average");
                    return Double.isNaN(average) ? 0 : average;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating average: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get overall average grade of all students.
     * @return The overall average grade
     */
    public double getOverallAverage() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT AVG(grade) as average FROM grades")) {
            if (rs.next()) {
                double average = rs.getDouble("average");
                return Double.isNaN(average) ? 0 : average;
            }
        } catch (SQLException e) {
            System.out.println("Error calculating overall average: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Search for a student by name.
     * @param name The student's name
     * @return The Student object, or null if not found
     */
    public Student searchStudent(String name) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT id, name FROM students WHERE LOWER(name) LIKE LOWER(?)")) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String studentName = rs.getString("name");
                    double average = getStudentAverage(id);
                    Student student = new Student(studentName);
                    student.setId(id);
                    student.setGrade(average);
                    return student;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for student: " + e.getMessage());
        }
        return null;
    }

    /**
     * Delete a student and all their grades.
     * @param studentId The student's ID
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM students WHERE id = ?")) {
            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update a student's grade.
     * @param studentId The student's ID
     * @param newGrade The new grade
     * @return true if successful, false otherwise
     */
    public boolean updateGrade(int studentId, double newGrade) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO grades (student_id, grade, subject) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, studentId);
            pstmt.setDouble(2, newGrade);
            pstmt.setString(3, "General");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all grades for a specific student.
     * @param studentId The student's ID
     * @return List of grades for the student
     */
    public List<Double> getStudentGrades(int studentId) {
        List<Double> grades = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT grade FROM grades WHERE student_id = ? ORDER BY created_at DESC")) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(rs.getDouble("grade"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving grades: " + e.getMessage());
        }
        return grades;
    }

    /**
     * Close the database connection.
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }
}
