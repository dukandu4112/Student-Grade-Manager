import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {

    public static void saveStudents(GradeManager manager) {
        try {
            FileWriter writer = new FileWriter("students.txt");

            for (Student student : manager.getStudents()) {
                writer.write(student.getName() + "," + student.getGrade() + "\n");
            }

            writer.close();
            System.out.println("Student data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving student data.");
        }
    }

    public static void loadStudents(GradeManager manager) {
        try {
            File file = new File("students.txt");

            if (!file.exists()) {
                return;
            }

            Scanner fileReader = new Scanner(file);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                String name = parts[0];
                double grade = Double.parseDouble(parts[1]);

                Student student = new Student(name);
                student.setGrade(grade);

                manager.addStudent(student);
            }

            fileReader.close();

        } catch (IOException e) {
            System.out.println("Error loading student data.");
        }
    }
}
