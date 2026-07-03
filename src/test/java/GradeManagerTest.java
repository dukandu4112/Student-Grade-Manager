import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit tests for the GradeManager class.
 * Tests grade management, student operations, and average calculations.
 */
public class GradeManagerTest {

    private GradeManager gradeManager;

    @Before
    public void setUp() {
        gradeManager = new GradeManager();
    }

    /**
     * Test initialization of GradeManager.
     */
    @Test
    public void testGradeManagerInitialization() {
        assertNotNull(gradeManager);
        assertTrue(gradeManager.getStudents().isEmpty());
    }

    /**
     * Test adding a single student.
     */
    @Test
    public void testAddSingleStudent() {
        gradeManager.addStudent("Alice Johnson");
        assertEquals(1, gradeManager.getStudents().size());
        assertEquals("Alice Johnson", gradeManager.getStudents().get(0).getName());
    }

    /**
     * Test adding multiple students.
     */
    @Test
    public void testAddMultipleStudents() {
        gradeManager.addStudent("Student One");
        gradeManager.addStudent("Student Two");
        gradeManager.addStudent("Student Three");
        
        assertEquals(3, gradeManager.getStudents().size());
    }

    /**
     * Test adding a Student object directly.
     */
    @Test
    public void testAddStudentObject() {
        Student student = new Student("Bob Smith");
        gradeManager.addStudent(student);
        
        assertEquals(1, gradeManager.getStudents().size());
        assertEquals("Bob Smith", gradeManager.getStudents().get(0).getName());
    }

    /**
     * Test calculating average with single student.
     */
    @Test
    public void testCalculateAverageSingleStudent() {
        gradeManager.addStudent("Emma Davis");
        gradeManager.getStudents().get(0).addGrade(85.0);
        
        double average = gradeManager.calculateAverage();
        assertEquals(85.0, average, 0.01);
    }

    /**
     * Test calculating average with multiple students.
     */
    @Test
    public void testCalculateAverageMultipleStudents() {
        gradeManager.addStudent("Student A");
        gradeManager.addStudent("Student B");
        gradeManager.addStudent("Student C");
        
        gradeManager.getStudents().get(0).addGrade(90.0);
        gradeManager.getStudents().get(1).addGrade(80.0);
        gradeManager.getStudents().get(2).addGrade(100.0);
        
        double average = gradeManager.calculateAverage();
        assertEquals(90.0, average, 0.01);
    }

    /**
     * Test calculating average with empty student list.
     */
    @Test
    public void testCalculateAverageEmptyList() {
        double average = gradeManager.calculateAverage();
        assertEquals(0.0, average, 0.01);
    }

    /**
     * Test calculating average with students having no grades.
     */
    @Test
    public void testCalculateAverageNoGrades() {
        gradeManager.addStudent("Charlie Brown");
        gradeManager.addStudent("Diana Prince");
        
        double average = gradeManager.calculateAverage();
        assertEquals(0.0, average, 0.01);
    }

    /**
     * Test calculating average with decimal grades.
     */
    @Test
    public void testCalculateAverageDecimalGrades() {
        gradeManager.addStudent("Frank Miller");
        gradeManager.addStudent("Grace Lee");
        
        gradeManager.getStudents().get(0).addGrade(87.5);
        gradeManager.getStudents().get(1).addGrade(92.5);
        
        double average = gradeManager.calculateAverage();
        assertEquals(90.0, average, 0.01);
    }

    /**
     * Test get students returns correct list.
     */
    @Test
    public void testGetStudents() {
        gradeManager.addStudent("Henry Wilson");
        gradeManager.addStudent("Ivy Martinez");
        
        ArrayList<Student> students = gradeManager.getStudents();
        assertEquals(2, students.size());
        assertEquals("Henry Wilson", students.get(0).getName());
        assertEquals("Ivy Martinez", students.get(1).getName());
    }

    /**
     * Test students maintain independence.
     */
    @Test
    public void testStudentIndependence() {
        gradeManager.addStudent("Jack Taylor");
        gradeManager.addStudent("Karen Anderson");
        
        gradeManager.getStudents().get(0).addGrade(95.0);
        gradeManager.getStudents().get(1).addGrade(75.0);
        
        assertEquals(95.0, gradeManager.getStudents().get(0).getGrade(), 0.01);
        assertEquals(75.0, gradeManager.getStudents().get(1).getGrade(), 0.01);
    }

    /**
     * Test with mixed grades.
     */
    @Test
    public void testMixedGrades() {
        gradeManager.addStudent("Leo Thompson");
        gradeManager.addStudent("Monica Green");
        gradeManager.addStudent("Nancy White");
        
        // First student gets multiple grades
        gradeManager.getStudents().get(0).addGrade(90.0);
        gradeManager.getStudents().get(0).addGrade(80.0);
        
        // Second student gets single grade
        gradeManager.getStudents().get(1).addGrade(85.0);
        
        // Third student has no grades
        
        // Class average should be (85.0 + 85.0 + 0.0) / 3 = 56.67
        double average = gradeManager.calculateAverage();
        assertEquals(56.67, average, 0.1);
    }

    /**
     * Test performance with large number of students.
     */
    @Test
    public void testLargeNumberOfStudents() {
        // Add 50 students
        for (int i = 1; i <= 50; i++) {
            gradeManager.addStudent("Student " + i);
            gradeManager.getStudents().get(i - 1).addGrade(50.0 + (i % 50));
        }
        
        assertEquals(50, gradeManager.getStudents().size());
        double average = gradeManager.calculateAverage();
        assertTrue(average > 0 && average <= 100);
    }

    /**
     * Test letter grade assignment through student integration.
     */
    @Test
    public void testLetterGradeIntegration() {
        gradeManager.addStudent("Oscar Black");
        gradeManager.getStudents().get(0).addGrade(92.0);
        
        Student student = gradeManager.getStudents().get(0);
        assertEquals("A", student.getLetterGrade());
    }

    /**
     * Test highest grade across all students.
     */
    @Test
    public void testHighestGradeIntegration() {
        gradeManager.addStudent("Peter Red");
        gradeManager.addStudent("Quinn Blue");
        
        gradeManager.getStudents().get(0).addGrade(75.0);
        gradeManager.getStudents().get(0).addGrade(95.0);
        gradeManager.getStudents().get(1).addGrade(88.0);
        
        assertEquals(95.0, gradeManager.getStudents().get(0).getHighestGrade(), 0.01);
        assertEquals(88.0, gradeManager.getStudents().get(1).getHighestGrade(), 0.01);
    }

    /**
     * Test lowest grade across all students.
     */
    @Test
    public void testLowestGradeIntegration() {
        gradeManager.addStudent("Rachel Green");
        gradeManager.addStudent("Steven Black");
        
        gradeManager.getStudents().get(0).addGrade(75.0);
        gradeManager.getStudents().get(0).addGrade(95.0);
        gradeManager.getStudents().get(1).addGrade(60.0);
        
        assertEquals(75.0, gradeManager.getStudents().get(0).getLowestGrade(), 0.01);
        assertEquals(60.0, gradeManager.getStudents().get(1).getLowestGrade(), 0.01);
    }

    /**
     * Test boundary values for average calculation.
     */
    @Test
    public void testAverageBoundaryValues() {
        gradeManager.addStudent("Tom White");
        gradeManager.addStudent("Uma Yellow");
        
        gradeManager.getStudents().get(0).addGrade(0.0);      // Minimum
        gradeManager.getStudents().get(1).addGrade(100.0);    // Maximum
        
        double average = gradeManager.calculateAverage();
        assertEquals(50.0, average, 0.01);
    }

    /**
     * Test all same grades.
     */
    @Test
    public void testAllSameGrades() {
        gradeManager.addStudent("Victor Purple");
        gradeManager.addStudent("Wendy Orange");
        gradeManager.addStudent("Xavier Pink");
        
        gradeManager.getStudents().get(0).addGrade(80.0);
        gradeManager.getStudents().get(1).addGrade(80.0);
        gradeManager.getStudents().get(2).addGrade(80.0);
        
        double average = gradeManager.calculateAverage();
        assertEquals(80.0, average, 0.01);
    }
}
