import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Student class.
 * Tests student initialization, grade management, and grade calculations.
 */
public class StudentTest {

    private Student student;

    @Before
    public void setUp() {
        student = new Student(1, "John Doe");
    }

    /**
     * Test student initialization with valid data.
     */
    @Test
    public void testStudentInitialization() {
        assertEquals(1, student.getId());
        assertEquals("John Doe", student.getName());
    }

    /**
     * Test getting student ID.
     */
    @Test
    public void testGetStudentId() {
        assertEquals(1, student.getId());
    }

    /**
     * Test getting student name.
     */
    @Test
    public void testGetStudentName() {
        assertEquals("John Doe", student.getName());
    }

    /**
     * Test letter grade assignment for A (90-100).
     */
    @Test
    public void testLetterGradeA() {
        assertEquals("A", Student.getLetterGrade(95));
        assertEquals("A", Student.getLetterGrade(90));
        assertEquals("A", Student.getLetterGrade(100));
    }

    /**
     * Test letter grade assignment for B (80-89).
     */
    @Test
    public void testLetterGradeB() {
        assertEquals("B", Student.getLetterGrade(85));
        assertEquals("B", Student.getLetterGrade(80));
        assertEquals("B", Student.getLetterGrade(89));
    }

    /**
     * Test letter grade assignment for C (70-79).
     */
    @Test
    public void testLetterGradeC() {
        assertEquals("C", Student.getLetterGrade(75));
        assertEquals("C", Student.getLetterGrade(70));
        assertEquals("C", Student.getLetterGrade(79));
    }

    /**
     * Test letter grade assignment for D (60-69).
     */
    @Test
    public void testLetterGradeD() {
        assertEquals("D", Student.getLetterGrade(65));
        assertEquals("D", Student.getLetterGrade(60));
        assertEquals("D", Student.getLetterGrade(69));
    }

    /**
     * Test letter grade assignment for F (0-59).
     */
    @Test
    public void testLetterGradeF() {
        assertEquals("F", Student.getLetterGrade(50));
        assertEquals("F", Student.getLetterGrade(0));
        assertEquals("F", Student.getLetterGrade(59));
    }

    /**
     * Test letter grade for boundary values.
     */
    @Test
    public void testLetterGradeBoundaries() {
        // Boundary between F and D
        assertEquals("F", Student.getLetterGrade(59.9));
        assertEquals("D", Student.getLetterGrade(60));
        
        // Boundary between D and C
        assertEquals("D", Student.getLetterGrade(69.9));
        assertEquals("C", Student.getLetterGrade(70));
        
        // Boundary between C and B
        assertEquals("C", Student.getLetterGrade(79.9));
        assertEquals("B", Student.getLetterGrade(80));
        
        // Boundary between B and A
        assertEquals("B", Student.getLetterGrade(89.9));
        assertEquals("A", Student.getLetterGrade(90));
    }

    /**
     * Test letter grade for perfect and failing scores.
     */
    @Test
    public void testLetterGradeExtremes() {
        assertEquals("A", Student.getLetterGrade(100));  // Perfect score
        assertEquals("F", Student.getLetterGrade(0));    // Failing score
    }

    /**
     * Test letter grade with negative input (edge case).
     */
    @Test
    public void testLetterGradeNegative() {
        assertEquals("F", Student.getLetterGrade(-10));
    }

    /**
     * Test letter grade with grade above 100 (edge case).
     */
    @Test
    public void testLetterGradeAboveHundred() {
        assertEquals("A", Student.getLetterGrade(105));
    }

    /**
     * Test toString method returns non-null value.
     */
    @Test
    public void testStudentToString() {
        assertNotNull(student.toString());
        assertTrue(student.toString().contains("John Doe"));
    }

    /**
     * Test creating multiple students with different data.
     */
    @Test
    public void testMultipleStudents() {
        Student student2 = new Student(2, "Jane Smith");
        Student student3 = new Student(3, "Bob Johnson");

        assertEquals(1, student.getId());
        assertEquals(2, student2.getId());
        assertEquals(3, student3.getId());

        assertEquals("John Doe", student.getName());
        assertEquals("Jane Smith", student2.getName());
        assertEquals("Bob Johnson", student3.getName());
    }
}
