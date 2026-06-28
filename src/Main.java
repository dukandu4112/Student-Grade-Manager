import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GradeManager manager = new GradeManager();

        int choice = 0;

        while (choice != 5) {
            System.out.println("\n===== Student Grade Manager =====");
            System.out.println("1. Add student");
            System.out.println("2. Add grade");
            System.out.println("3. Show average grade");
            System.out.println("4. Show all students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                System.out.print("Enter student name: ");
                String name = input.nextLine();

                manager.addStudent(name);

                System.out.println("Student added successfully.");
            } else if (choice == 2) {
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
            } else if (choice == 3) {
                if (manager.getStudents().isEmpty()) {
                    System.out.println("No students available.");
                } else {
                    System.out.println("Average grade: " + manager.calculateAverage());
                }
            } else if (choice == 4) {
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
            } else if (choice == 5) {
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        input.close();
    }
}