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
        student = new Student("John Doe");
    }

    /**
     * Test student initialization with valid data.
     */
    @Test
    public void testStudentInitialization() {
        assertEquals("John Doe", student.getName());
        assertEquals(-1, student.getId());  // Not yet saved to database
        assertEquals(0.0, student.getGrade(), 0.01);
    }

    /**
     * Test getting student name.
     */
    @Test
    public void testGetStudentName() {
        assertEquals("John Doe", student.getName());
    }

    /**
     * Test setting student name.
     */
    @Test
    public void testSetStudentName() {
        student.setName("Jane Smith");
        assertEquals("Jane Smith", student.getName());
    }

    /**
     * Test adding a valid grade.
     */
    @Test
    public void testAddValidGrade() {
        student.addGrade(85.0);
        assertEquals(85.0, student.getGrade(), 0.01);
        assertEquals(1, student.getGradeCount());
    }

    /**
     * Test adding multiple grades and calculating average.
     */
    @Test
    public void testAddMultipleGrades() {
        student.addGrade(90.0);
        student.addGrade(80.0);
        student.addGrade(100.0);
        
        double average = student.calculateAverage();
        assertEquals(90.0, average, 0.01);
        assertEquals(3, student.getGradeCount());
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
        assertEquals("F", Student.getLetterGrade(59));
        assertEquals("D", Student.getLetterGrade(60));
        assertEquals("D", Student.getLetterGrade(69));
        assertEquals("C", Student.getLetterGrade(70));
        assertEquals("C", Student.getLetterGrade(79));
        assertEquals("B", Student.getLetterGrade(80));
        assertEquals("B", Student.getLetterGrade(89));
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
     * Test getting highest grade.
     */
    @Test
    public void testGetHighestGrade() {
        student.addGrade(75.0);
        student.addGrade(95.0);
        student.addGrade(85.0);
        
        assertEquals(95.0, student.getHighestGrade(), 0.01);
    }

    /**
     * Test getting lowest grade.
     */
    @Test
    public void testGetLowestGrade() {
        student.addGrade(75.0);
        student.addGrade(95.0);
        student.addGrade(85.0);
        
        assertEquals(75.0, student.getLowestGrade(), 0.01);
    }

    /**
     * Test grade history tracking.
     */
    @Test
    public void testGradeHistory() {
        student.addGrade(80.0);
        student.addGrade(85.0);
        student.addGrade(90.0);
        
        assertEquals(3, student.getGradeHistory().size());
        assertTrue(student.getGradeHistory().contains(80.0));
        assertTrue(student.getGradeHistory().contains(85.0));
        assertTrue(student.getGradeHistory().contains(90.0));
    }

    /**
     * Test invalid grade (negative).
     */
    @Test
    public void testInvalidNegativeGrade() {
        student.addGrade(-10.0);
        // Should not add invalid grade
        assertEquals(0, student.getGradeCount());
    }

    /**
     * Test invalid grade (above 100).
     */
    @Test
    public void testInvalidAboveHundredGrade() {
        student.addGrade(105.0);
        // Should not add invalid grade
        assertEquals(0, student.getGradeCount());
    }

    /**
     * Test subject tracking.
     */
    @Test
    public void testSubjectTracking() {
        student.setSubject("Mathematics");
        assertEquals("Mathematics", student.getSubject());
    }

    /**
     * Test default subject.
     */
    @Test
    public void testDefaultSubject() {
        assertEquals("General", student.getSubject());
    }

    /**
     * Test student toString method.
     */
    @Test
    public void testStudentToString() {
        student.addGrade(85.0);
        String result = student.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("85"));
    }

    /**
     * Test multiple student instances are independent.
     */
    @Test
    public void testMultipleStudentInstances() {
        Student student2 = new Student("Jane Smith");
        
        student.addGrade(90.0);
        student2.addGrade(80.0);
        
        assertEquals(90.0, student.getGrade(), 0.01);
        assertEquals(80.0, student2.getGrade(), 0.01);
        assertEquals(1, student.getGradeCount());
        assertEquals(1, student2.getGradeCount());
    }

    /**
     * Test student with database ID.
     */
    @Test
    public void testStudentWithDatabaseId() {
        Student dbStudent = new Student(1, "Bob Johnson", 85.5);
        
        assertEquals(1, dbStudent.getId());
        assertEquals("Bob Johnson", dbStudent.getName());
        assertEquals(85.5, dbStudent.getGrade(), 0.01);
        assertTrue(dbStudent.isSaved());
    }

    /**
     * Test setId method.
     */
    @Test
    public void testSetStudentId() {
        assertEquals(-1, student.getId());
        student.setId(5);
        assertEquals(5, student.getId());
        assertTrue(student.isSaved());
    }
}
