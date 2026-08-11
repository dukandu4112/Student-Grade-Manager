import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GradeManager {

    private final List<Student> students;

    public GradeManager() {
        this.students = new ArrayList<>();
    }

    public void addStudent(String name) {
        students.add(new Student(name));
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Returns the students list (modifiable) so callers can sort/remove for compatibility.
     * Prefer using manager helper methods (removeStudent, sortByName) where possible.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Returns an unmodifiable view of the students list when callers need a read-only view.
     */
    public List<Student> getStudentsUnmodifiable() {
        return Collections.unmodifiableList(students);
    }

    public double calculateAverage() {
        if (students.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Student student : students) {
            total += student.getGrade();
        }

        return total / students.size();
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("\nStudent List:");
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println((i + 1) + ". " + student.getName()
                    + " - Grade: " + student.getGrade()
                    + " - Letter Grade: " + student.getLetterGrade());
        }
    }

    public void displayAverage() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            System.out.println("Average grade: " + calculateAverage());
        }
    }

    public void searchStudent(String searchName) {
        boolean found = false;

        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(searchName)) {
                System.out.println("\nStudent Found:");
                System.out.println("Name: " + student.getName());
                System.out.println("Grade: " + student.getGrade());
                System.out.println("Letter Grade: " + student.getLetterGrade());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }

    // Manager helper methods to manipulate the collection safely
    public void removeStudent(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
        }
    }

    public boolean removeStudentByName(String name) {
        return students.removeIf(s -> s.getName().equalsIgnoreCase(name));
    }

    public void clearStudents() {
        students.clear();
    }

    public void sortByName() {
        students.sort(Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER));
    }
}
