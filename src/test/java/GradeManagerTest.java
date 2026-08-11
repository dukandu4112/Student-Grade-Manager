import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeManagerTest {

    @Test
    void addStudentAndCalculateAverage() {
        GradeManager gm = new GradeManager();
        gm.addStudent("Alice");
        gm.addStudent("Bob");

        // set grades via Student API
        gm.getStudents().get(0).setGrade(90.0);
        gm.getStudents().get(1).setGrade(70.0);

        double avg = gm.calculateAverage();
        assertEquals(80.0, avg, 0.0001, "Average should be (90 + 70) / 2 = 80");
    }
}
