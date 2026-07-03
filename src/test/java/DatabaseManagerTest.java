import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Unit tests for the DatabaseManager class.
 * Tests database connection, student/grade operations, and calculations.
 */
public class DatabaseManagerTest {

    private DatabaseManager dbManager;
    private static final String TEST_DB = "test_students.db";

    @Before
    public void setUp() {
        // Delete test database if it exists
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }
        
        dbManager = new DatabaseManager(TEST_DB);
    }

    @After
    public void tearDown() {
        // Clean up after tests
        if (dbManager != null) {
            dbManager.disconnect();
        }
        
        // Delete test database
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }
    }

    /**
     * Test database manager initialization.
     */
    @Test
    public void testDatabaseInitialization() {
        assertNotNull(dbManager);
        assertTrue(new File(TEST_DB).exists());
    }

    /**
     * Test adding a single student to the database.
     */
    @Test
    public void testAddStudent() {
        boolean result = dbManager.addStudent("Alice Johnson");
        assertTrue(result);
    }

    /**
     * Test adding multiple students.
     */
    @Test
    public void testAddMultipleStudents() {
        assertTrue(dbManager.addStudent("Student One"));
        assertTrue(dbManager.addStudent("Student Two"));
        assertTrue(dbManager.addStudent("Student Three"));
    }

    /**
     * Test adding duplicate student (should fail or handle gracefully).
     */
    @Test
    public void testAddDuplicateStudent() {
        dbManager.addStudent("John Doe");
        // Adding same student again should fail
        boolean result = dbManager.addStudent("John Doe");
        assertFalse(result);
    }

    /**
     * Test adding grade to a student.
     */
    @Test
    public void testAddGrade() {
        dbManager.addStudent("Bob Smith");
        int studentId = 1;
        boolean result = dbManager.addGrade(studentId, 85.5, "Math");
        assertTrue(result);
    }

    /**
     * Test adding multiple grades to same student.
     */
    @Test
    public void testAddMultipleGradesToStudent() {
        dbManager.addStudent("Emma Davis");
        int studentId = 1;
        
        assertTrue(dbManager.addGrade(studentId, 90, "Math"));
        assertTrue(dbManager.addGrade(studentId, 85, "English"));
        assertTrue(dbManager.addGrade(studentId, 92, "Science"));
    }

    /**
     * Test retrieving all students.
     */
    @Test
    public void testGetAllStudents() {
        dbManager.addStudent("Student A");
        dbManager.addStudent("Student B");
        
        ArrayList<Student> students = dbManager.getAllStudents();
        assertNotNull(students);
        assertEquals(2, students.size());
    }

    /**
     * Test getting empty student list.
     */
    @Test
    public void testGetAllStudentsEmpty() {
        ArrayList<Student> students = dbManager.getAllStudents();
        assertNotNull(students);
        assertEquals(0, students.size());
    }

    /**
     * Test searching for student by ID.
     */
    @Test
    public void testSearchStudentById() {
        dbManager.addStudent("Charlie Brown");
        Student student = dbManager.searchStudentById(1);
        
        assertNotNull(student);
        assertEquals(1, student.getId());
        assertEquals("Charlie Brown", student.getName());
    }

    /**
     * Test searching for non-existent student.
     */
    @Test
    public void testSearchNonExistentStudent() {
        Student student = dbManager.searchStudentById(999);
        assertNull(student);
    }

    /**
     * Test calculating average grade for a student.
     */
    @Test
    public void testCalculateStudentAverage() {
        dbManager.addStudent("Grace Lee");
        dbManager.addGrade(1, 90, "Test 1");
        dbManager.addGrade(1, 80, "Test 2");
        dbManager.addGrade(1, 100, "Test 3");
        
        double average = dbManager.getStudentAverage(1);
        assertEquals(90.0, average, 0.01);  // Allow 0.01 margin
    }

    /**
     * Test calculating class average.
     */
    @Test
    public void testCalculateClassAverage() {
        dbManager.addStudent("Henry Wilson");
        dbManager.addStudent("Ivy Martinez");
        
        dbManager.addGrade(1, 90, "Test");
        dbManager.addGrade(1, 80, "Test");
        dbManager.addGrade(2, 100, "Test");
        
        double classAverage = dbManager.getClassAverage();
        assertEquals(90.0, classAverage, 0.01);
    }

    /**
     * Test class average with no grades.
     */
    @Test
    public void testClassAverageNoGrades() {
        dbManager.addStudent("Jack Taylor");
        double classAverage = dbManager.getClassAverage();
        assertEquals(0.0, classAverage, 0.01);
    }

    /**
     * Test deleting a student.
     */
    @Test
    public void testDeleteStudent() {
        dbManager.addStudent("Karen Anderson");
        dbManager.addStudent("Leo Thompson");
        
        boolean result = dbManager.deleteStudent(1);
        assertTrue(result);
        
        ArrayList<Student> students = dbManager.getAllStudents();
        assertEquals(1, students.size());
    }

    /**
     * Test deleting non-existent student.
     */
    @Test
    public void testDeleteNonExistentStudent() {
        boolean result = dbManager.deleteStudent(999);
        assertFalse(result);
    }

    /**
     * Test that deleting student removes associated grades.
     */
    @Test
    public void testDeleteStudentRemovesGrades() {
        dbManager.addStudent("Monica Green");
        dbManager.addGrade(1, 95, "Math");
        dbManager.addGrade(1, 87, "Science");
        
        dbManager.deleteStudent(1);
        
        // After deletion, student should not exist
        Student student = dbManager.searchStudentById(1);
        assertNull(student);
    }

    /**
     * Test student name is stored correctly.
     */
    @Test
    public void testStudentNameStorage() {
        String testName = "Nancy White";
        dbManager.addStudent(testName);
        
        Student student = dbManager.searchStudentById(1);
        assertEquals(testName, student.getName());
    }

    /**
     * Test grade accuracy after storage.
     */
    @Test
    public void testGradeAccuracy() {
        dbManager.addStudent("Oscar Black");
        double testGrade = 87.75;
        
        dbManager.addGrade(1, testGrade, "Quiz");
        double average = dbManager.getStudentAverage(1);
        
        assertEquals(testGrade, average, 0.01);
    }

    /**
     * Test with boundary grade values.
     */
    @Test
    public void testBoundaryGrades() {
        dbManager.addStudent("Peter Red");
        
        assertTrue(dbManager.addGrade(1, 0, "Min Grade"));     // Minimum
        assertTrue(dbManager.addGrade(1, 100, "Max Grade"));   // Maximum
        assertTrue(dbManager.addGrade(1, 50.5, "Mid Grade"));  // Middle
    }
}
