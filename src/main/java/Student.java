import java.util.ArrayList;
import java.util.List;

/**
 * Student class represents a student with their grades and academic information.
 * Now integrated with database support for persistent storage.
 */
public class Student {
    private int id;  // Database ID
    private String name;
    private double grade;  // Current or average grade
    private List<Double> gradeHistory;  // Track all grades
    private String subject;  // Optional subject tracking

    /**
     * Constructor for new students (without database ID).
     * @param name The student's name
     */
    public Student(String name) {
        this.id = -1;  // -1 indicates not yet saved to database
        this.name = name;
        this.grade = 0.0;
        this.gradeHistory = new ArrayList<>();
        this.subject = "General";
    }

    /**
     * Constructor for students loaded from database.
     * @param id The database ID
     * @param name The student's name
     * @param grade The average grade
     */
    public Student(int id, String name, double grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.gradeHistory = new ArrayList<>();
        this.subject = "General";
    }

    // ===== Getters and Setters =====

    /**
     * Get the student's database ID.
     * @return The ID, or -1 if not yet saved
     */
    public int getId() {
        return id;
    }

    /**
     * Set the student's database ID.
     * @param id The database ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the student's name.
     * @return The student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the student's name.
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the student's current/average grade.
     * @return The grade
     */
    public double getGrade() {
        return grade;
    }

    /**
     * Set the student's grade.
     * @param grade The new grade
     */
    public void setGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            this.grade = grade;
            this.gradeHistory.add(grade);
        } else {
            System.out.println("Invalid grade. Please enter a value between 0 and 100.");
        }
    }

    /**
     * Get the subject for this student.
     * @return The subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject for this student.
     * @param subject The subject name
     */
    public void setSubject(String subject) {
        this.subject = subject != null ? subject : "General";
    }

    /**
     * Get the grade history for this student.
     * @return List of all grades
     */
    public List<Double> getGradeHistory() {
        return new ArrayList<>(gradeHistory);
    }

    /**
     * Add a grade to the history and update average.
     * @param grade The grade to add
     */
    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            this.gradeHistory.add(grade);
            this.grade = calculateAverage();
        } else {
            System.out.println("Invalid grade. Please enter a value between 0 and 100.");
        }
    }

    /**
     * Calculate the average of all grades in history.
     * @return The average grade
     */
    public double calculateAverage() {
        if (gradeHistory.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double g : gradeHistory) {
            sum += g;
        }
        return sum / gradeHistory.size();
    }

    /**
     * Get the number of grades recorded.
     * @return The count of grades
     */
    public int getGradeCount() {
        return gradeHistory.size();
    }

    /**
     * Check if the student has been saved to the database.
     * @return true if saved, false otherwise
     */
    public boolean isSaved() {
        return id != -1;
    }

    // ===== Grade Conversion =====

    /**
     * Convert the current grade to a letter grade.
     * @return The letter grade (A, B, C, D, or F)
     */
    public String getLetterGrade() {
        return getLetterGrade(grade);
    }

    /**
     * Convert a numerical grade to a letter grade.
     * @param numericGrade The numerical grade
     * @return The letter grade (A, B, C, D, or F)
     */
    public static String getLetterGrade(double numericGrade) {
        if (numericGrade >= 90)
            return "A";
        else if (numericGrade >= 80)
            return "B";
        else if (numericGrade >= 70)
            return "C";
        else if (numericGrade >= 60)
            return "D";
        else
            return "F";
    }

    /**
     * Get the highest grade in the history.
     * @return The highest grade, or 0 if no grades
     */
    public double getHighestGrade() {
        if (gradeHistory.isEmpty()) {
            return 0.0;
        }
        return gradeHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }

    /**
     * Get the lowest grade in the history.
     * @return The lowest grade, or 0 if no grades
     */
    public double getLowestGrade() {
        if (gradeHistory.isEmpty()) {
            return 0.0;
        }
        return gradeHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }

    // ===== String Representation =====

    /**
     * Get a string representation of the student.
     * @return A formatted string with student details
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grade=" + String.format("%.2f", grade) +
                ", letterGrade='" + getLetterGrade() + '\'' +
                ", gradeCount=" + gradeHistory.size() +
                ", subject='" + subject + '\'' +
                '}';
    }
}
