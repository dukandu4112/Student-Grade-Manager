package com.grademanager;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {
    private final GradeManager manager;
    private final Scanner scanner;

    public ConsoleApp(GradeManager manager, Scanner scanner) {
        this.manager = manager;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Student Grade Manager v1.0.0");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> addGrade();
                    case 3 -> showStudentDetails();
                    case 4 -> showRoster("name");
                    case 5 -> editGrade();
                    case 6 -> deleteGrade();
                    case 7 -> renameStudent();
                    case 8 -> deleteStudent();
                    case 9 -> searchStudent();
                    case 10 -> showClassAverage();
                    case 11 -> showRoster("average_desc");
                    case 12 -> showRoster("average_asc");
                    case 13 -> exportCsv();
                    case 14 -> { System.out.println("Goodbye!"); return; }
                    default -> System.out.println("Invalid option. Choose 1-14.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("""

                ===== Student Grade Manager =====
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
                """);
    }

    private void addStudent() throws SQLException {
        String name = readRequired("Student name: ");
        Student student = manager.addStudent(name);
        System.out.println("Added: " + student);
    }

    private void addGrade() throws SQLException {
        showRoster("name");
        int studentId = readInt("Student ID: ");
        double grade = readDouble("Grade (0-100): ");
        String subject = readLine("Subject [General]: ");
        GradeRecord record = manager.addGrade(studentId, grade, subject);
        System.out.printf("Added grade #%d: %.2f (%s) - %s%n", record.id(), record.grade(), GradeScale.letterGrade(record.grade()), record.subject());
    }

    private void showStudentDetails() throws SQLException {
        int id = readInt("Student ID: ");
        Student student = manager.findStudent(id).orElseThrow(() -> new IllegalArgumentException("Student not found."));
        List<GradeRecord> grades = manager.gradeHistory(id);
        System.out.println("\n" + student.getName() + " (ID " + id + ")");
        if (grades.isEmpty()) {
            System.out.println("No grades recorded.");
            return;
        }
        for (GradeRecord grade : grades) {
            System.out.printf("#%d | %-20s | %6.2f | %s%n", grade.id(), grade.subject(), grade.grade(), GradeScale.letterGrade(grade.grade()));
        }
        double avg = manager.studentAverage(id);
        System.out.printf("Average: %.2f (%s)%n", avg, GradeScale.letterGrade(avg));
        manager.highestGrade(id).ifPresent(g -> System.out.printf("Highest: %.2f (%s)%n", g.grade(), g.subject()));
        manager.lowestGrade(id).ifPresent(g -> System.out.printf("Lowest: %.2f (%s)%n", g.grade(), g.subject()));
    }

    private void showRoster(String sort) throws SQLException {
        List<DatabaseManager.StudentSummary> rows = manager.summaries(sort);
        if (rows.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.printf("%-5s %-28s %-8s %-10s %-7s%n", "ID", "Name", "Grades", "Average", "Letter");
        for (DatabaseManager.StudentSummary row : rows) {
            System.out.printf("%-5d %-28s %-8d %-10.2f %-7s%n", row.id(), row.name(), row.gradeCount(), row.averageGrade(), row.letterGrade());
        }
    }

    private void editGrade() throws SQLException {
        int gradeId = readInt("Grade ID to edit: ");
        double grade = readDouble("New grade (0-100): ");
        String subject = readLine("New subject [General]: ");
        System.out.println(manager.editGrade(gradeId, grade, subject) ? "Grade updated." : "Grade not found.");
    }

    private void deleteGrade() throws SQLException {
        int gradeId = readInt("Grade ID to delete: ");
        if (!confirm("Delete this grade? (y/n): ")) return;
        System.out.println(manager.deleteGrade(gradeId) ? "Grade deleted." : "Grade not found.");
    }

    private void renameStudent() throws SQLException {
        int id = readInt("Student ID: ");
        String name = readRequired("New name: ");
        System.out.println(manager.renameStudent(id, name) ? "Student renamed." : "Student not found.");
    }

    private void deleteStudent() throws SQLException {
        int id = readInt("Student ID to delete: ");
        if (!confirm("Delete student and all associated grades? (y/n): ")) return;
        System.out.println(manager.deleteStudent(id) ? "Student deleted." : "Student not found.");
    }

    private void searchStudent() throws SQLException {
        String query = readRequired("Search name: ");
        List<Student> results = manager.searchStudents(query);
        if (results.isEmpty()) {
            System.out.println("No matching students.");
            return;
        }
        results.forEach(System.out::println);
    }

    private void showClassAverage() throws SQLException {
        double average = manager.classAverage();
        System.out.printf("Class average: %.2f%s%n", average, average == 0 ? "" : " (" + GradeScale.letterGrade(average) + ")");
    }

    private void exportCsv() throws Exception {
        String raw = readLine("Output file [student-grades.csv]: ");
        Path output = Path.of(raw.isBlank() ? "student-grades.csv" : raw);
        CsvService.exportData(manager, output);
        System.out.println("Exported to " + output.toAbsolutePath());
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readRequired(String prompt) {
        String value = readLine(prompt);
        if (value.isBlank()) throw new IllegalArgumentException("Value cannot be blank.");
        return value;
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try { return Integer.parseInt(value); }
            catch (NumberFormatException e) { System.out.println("Enter a whole number."); }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                double number = Double.parseDouble(value);
                if (!Double.isFinite(number)) throw new NumberFormatException();
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid numeric grade.");
            }
        }
    }

    private boolean confirm(String prompt) {
        String value = readLine(prompt);
        return value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes");
    }
}
