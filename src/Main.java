import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GradeManager manager = new GradeManager();

        int choice;

        do {
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

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter student name: ");
                    String name = input.nextLine();
                    manager.addStudent(name);
                    System.out.println("Student added successfully.");
                }
                case 2 -> {
                    System.out.print("Enter student number: ");
                    int studentNumber = input.nextInt();
                    if (studentNumber >= 1 && studentNumber <= manager.getStudents().size()) {
                        System.out.print("Enter grade: ");
                        double grade = input.nextDouble();
                        manager.getStudents().get(studentNumber - 1).setGrade(grade);
                        System.out.println("Grade added successfully.");
                    } else {
                        System.out.println("Invalid student number.");
                    }
                }
                case 3 -> {
                    if (manager.getStudents().isEmpty()) {
                        System.out.println("No students available.");
                    } else {
                        System.out.println("Average grade: " + manager.calculateAverage());
                    }
                }
                case 4 -> {
                    if (manager.getStudents().isEmpty()) {
                        System.out.println("No students available.");
                    } else {
                        System.out.println("\nStudent List:");
                        for (int i = 0; i < manager.getStudents().size(); i++) {
                            Student student = manager.getStudents().get(i);
                            System.out.println((i + 1) + ". " + student.getName()
                                    + " - Grade: " + student.getGrade()
                                    + " - Letter Grade: " + student.getLetterGrade());
                        }
                    }
                }
                case 5 -> {
                    System.out.print("Enter student number: ");
                    int studentNumber = input.nextInt();
                    if (studentNumber >= 1 && studentNumber <= manager.getStudents().size()) {
                        System.out.print("Enter new grade: ");
                        double newGrade = input.nextDouble();
                        manager.getStudents().get(studentNumber - 1).setGrade(newGrade);
                        System.out.println("Grade updated successfully.");
                    } else {
                        System.out.println("Invalid student number.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter student number to delete: ");
                    int studentNumber = input.nextInt();
                    if (studentNumber >= 1 && studentNumber <= manager.getStudents().size()) {
                        manager.getStudents().remove(studentNumber - 1);
                        System.out.println("Student deleted successfully.");
                    } else {
                        System.out.println("Invalid student number.");
                    }
                }
                case 7 -> {
                    System.out.print("Enter student name to search: ");
                    String searchName = input.nextLine();
                    boolean found = false;
                    for (Student student : manager.getStudents()) {
                        if (student.getName().equalsIgnoreCase(searchName)) {
                            System.out.println("\nStudent Found:");
                            System.out.println("Name: " + student.getName());
                            System.out.println("Grade: " + student.getGrade());
                            System.out.println("Letter Grade: " + student.getLetterGrade());
                            found = true;
                        }
                    }
                    if (!found) System.out.println("Student not found.");
                }
                case 8 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        input.close();
    }
}