import java.util.List;
import java.util.Scanner;

/**
 * Main class for the Student Grade Manager application.
 * Provides a command-line interface for managing student grades with database persistence.
 */
public class Main {
    private static DatabaseManager dbManager;
    private static Scanner input;

    public static void main(String[] args) {
        // Initialize database manager and input
        dbManager = new DatabaseManager();
        input = new Scanner(System.in);

        // Connect to database
        dbManager.connect();

        int choice;

        do {
            displayMenu();
            choice = getIntInput();
            input.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrade();
                case 3 -> showAverageGrade();
                case 4 -> showAllStudents();
                case 5 -> editStudentGrade();
                case 6 -> deleteStudent();
                case 7 -> searchStudent();
                case 8 -> {
                    System.out.println("Goodbye!");
                    dbManager.disconnect();
                }
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        input.close();
    }

    /**
     * Display the main menu.
     */
    private static void displayMenu() {
        System.out.println("\n===== Student Grade Manager =====");
        System.out.println("1. Add student");
        System.out.println("2. Add grade");
        System.out.println("3. Show average grade");
        System.out.println("4. Show all students");
        System.out.println("5. Edit student grade");
        System.out.println("6. Delete student");
        System.out.println("7. Search student");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Get an integer input from the user.
     * @return The integer value entered
     */
    private static int getIntInput() {
        try {
            return input.nextInt();
        } catch (Exception e) {
            input.nextLine();  // Clear invalid input
            return -1;
        }
    }

    /**
     * Get a double input from the user.
     * @return The double value entered
     */
    private static double getDoubleInput() {
        try {
            return input.nextDouble();
        } catch (Exception e) {
            input.nextLine();  // Clear invalid input
            return -1;
        }
    }

    /**
     * Add a new student to the database.
     */
    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = input.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return;
        }

        if (dbManager.addStudent(name)) {
            System.out.println("✓ Student '" + name + "' added successfully.");
        } else {
            System.out.println("✗ Failed to add student. Student may already exist.");
        }
    }

    /**
     * Add a grade to a student.
     */
    private static void addGrade() {
        List<Student> students = dbManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students available. Please add a student first.");
            return;
        }

        displayStudentList(students);

        System.out.print("Enter student number: ");
        int studentNumber = getIntInput();
        input.nextLine();

        if (studentNumber < 1 || studentNumber > students.size()) {
            System.out.println("Invalid student number.");
            return;
        }

        Student student = students.get(studentNumber - 1);

        System.out.print("Enter grade (0-100): ");
        double grade = getDoubleInput();
        input.nextLine();

        if (grade < 0 || grade > 100) {
            System.out.println("Invalid grade. Please enter a value between 0 and 100.");
            return;
        }

        System.out.print("Enter subject (optional, press Enter for 'General'): ");
        String subject = input.nextLine();
        if (subject.trim().isEmpty()) {
            subject = "General";
        }

        if (dbManager.addGrade(student.getId(), grade, subject)) {
            System.out.println("✓ Grade " + grade + " added successfully for " + student.getName() + ".");
        } else {
            System.out.println("✗ Failed to add grade.");
        }
    }

    /**
     * Show the average grade of a specific student or all students.
     */
    private static void showAverageGrade() {
        List<Student> students = dbManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        displayStudentList(students);

        System.out.print("Enter student number (or 0 for class average): ");
        int studentNumber = getIntInput();
        input.nextLine();

        if (studentNumber == 0) {
            double classAverage = dbManager.getOverallAverage();
            System.out.println("\n===== Class Average =====");
            System.out.println("Average Grade: " + String.format("%.2f", classAverage));
            System.out.println("Letter Grade: " + Student.getLetterGrade(classAverage));
        } else if (studentNumber >= 1 && studentNumber <= students.size()) {
            Student student = students.get(studentNumber - 1);
            double average = dbManager.getStudentAverage(student.getId());
            System.out.println("\n===== Student Average =====");
            System.out.println("Name: " + student.getName());
            System.out.println("Average Grade: " + String.format("%.2f", average));
            System.out.println("Letter Grade: " + Student.getLetterGrade(average));
        } else {
            System.out.println("Invalid student number.");
        }
    }

    /**
     * Show all students with their average grades.
     */
    private static void showAllStudents() {
        List<Student> students = dbManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("\n===== Student List =====");
        displayStudentList(students);
    }

    /**
     * Display the student list with their information.
     * @param students List of students to display
     */
    private static void displayStudentList(List<Student> students) {
        System.out.println();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            double average = dbManager.getStudentAverage(student.getId());
            System.out.println((i + 1) + ". " + student.getName()
                    + " - Average Grade: " + String.format("%.2f", average)
                    + " - Letter Grade: " + Student.getLetterGrade(average));
        }
    }

    /**
     * Edit a student's grade by adding a new grade.
     */
    private static void editStudentGrade() {
        List<Student> students = dbManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        displayStudentList(students);

        System.out.print("Enter student number: ");
        int studentNumber = getIntInput();
        input.nextLine();

        if (studentNumber < 1 || studentNumber > students.size()) {
            System.out.println("Invalid student number.");
            return;
        }

        Student student = students.get(studentNumber - 1);

        System.out.print("Enter new grade (0-100): ");
        double newGrade = getDoubleInput();
        input.nextLine();

        if (newGrade < 0 || newGrade > 100) {
            System.out.println("Invalid grade. Please enter a value between 0 and 100.");
            return;
        }

        if (dbManager.updateGrade(student.getId(), newGrade)) {
            System.out.println("✓ Grade updated successfully for " + student.getName() + ".");
        } else {
            System.out.println("✗ Failed to update grade.");
        }
    }

    /**
     * Delete a student and all their grades from the database.
     */
    private static void deleteStudent() {
        List<Student> students = dbManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        displayStudentList(students);

        System.out.print("Enter student number to delete: ");
        int studentNumber = getIntInput();
        input.nextLine();

        if (studentNumber < 1 || studentNumber > students.size()) {
            System.out.println("Invalid student number.");
            return;
        }

        Student student = students.get(studentNumber - 1);

        System.out.print("Are you sure you want to delete '" + student.getName() + "'? (yes/no): ");
        String confirmation = input.nextLine().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            if (dbManager.deleteStudent(student.getId())) {
                System.out.println("✓ Student '" + student.getName() + "' deleted successfully.");
            } else {
                System.out.println("✗ Failed to delete student.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Search for a student by name.
     */
    private static void searchStudent() {
        System.out.print("Enter student name to search: ");
        String searchName = input.nextLine();

        Student student = dbManager.searchStudent(searchName);

        if (student != null) {
            double average = dbManager.getStudentAverage(student.getId());
            List<Double> grades = dbManager.getStudentGrades(student.getId());

            System.out.println("\n===== Student Found =====");
            System.out.println("Name: " + student.getName());
            System.out.println("Average Grade: " + String.format("%.2f", average));
            System.out.println("Letter Grade: " + Student.getLetterGrade(average));
            System.out.println("Total Grades: " + grades.size());

            if (!grades.isEmpty()) {
                System.out.println("All Grades: ");
                for (int i = 0; i < grades.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + String.format("%.2f", grades.get(i)));
                }
            }
        } else {
            System.out.println("✗ Student not found.");
        }
    }
}
