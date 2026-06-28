import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<String> studentNames = new ArrayList<>();
        ArrayList<Double> studentGrades = new ArrayList<>();

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

                studentNames.add(name);
                studentGrades.add(0.0);

                System.out.println("Student added successfully.");
            } else if (choice == 2) {
                System.out.print("Enter student number: ");
                int studentNumber = input.nextInt();

                if (studentNumber >= 1 && studentNumber <= studentNames.size()) {
                    System.out.print("Enter grade: ");
                    double grade = input.nextDouble();

                    studentGrades.set(studentNumber - 1, grade);

                    System.out.println("Grade added successfully.");
                } else {
                    System.out.println("Invalid student number.");
                }
            } else if (choice == 3) {
                if (studentGrades.size() == 0) {
                    System.out.println("No students available.");
                } else {
                    double total = 0;

                    for (double grade : studentGrades) {
                        total += grade;
                    }

                    double average = total / studentGrades.size();

                    System.out.println("Average grade: " + average);
                }
            } else if (choice == 4) {
                if (studentNames.size() == 0) {
                    System.out.println("No students available.");
                } else {
                    System.out.println("\nStudent List:");

                    for (int i = 0; i < studentNames.size(); i++) {
                        System.out.println((i + 1) + ". " + studentNames.get(i)
                                + " - Grade: " + studentGrades.get(i));
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