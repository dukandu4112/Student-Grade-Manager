import java.util.ArrayList;

public class GradeManager {

    private ArrayList<Student> students;

    public GradeManager() {
        students = new ArrayList<>();
    }

    public void addStudent(String name) {
        students.add(new Student(name));
    }
    public void addStudent(Student student) {
    students.add(student);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public double calculateAverage() {

        if (students.isEmpty())
            return 0;

        double total = 0;

        for (Student student : students) {
            total += student.getGrade();
        }

        return total / students.size();
    }
    public void displayAllStudents() {
    if (students.isEmpty()) {
        System.out.println("No students available.");
    } else {
        System.out.println("\nStudent List:");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);

            System.out.println((i + 1) + ". " + student.getName()
                    + " - Grade: " + student.getGrade()
                    + " - Letter Grade: " + student.getLetterGrade());
        }
    }
  } 
}