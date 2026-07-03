import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit tests for the GradeManager class.
 * Tests grade calculations, averages, and letter grade assignments.
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
    }

    /**
     * Test calculating average of single grade.
     */
    @Test
    public void testAverageSingleGrade() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(85.0);
        
        double average = gradeManager.calculateAverage(grades);
        assertEquals(85.0, average, 0.01);
    }

    /**
     * Test calculating average of multiple grades.
     */
    @Test
    public void testAverageMultipleGrades() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(90.0);
        grades.add(80.0);
        grades.add(100.0);
        grades.add(70.0);
        
        double average = gradeManager.calculateAverage(grades);
        assertEquals(85.0, average, 0.01);
    }

    /**
     * Test average with decimal grades.
     */
    @Test
    public void testAverageDecimalGrades() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(87.5);
        grades.add(92.3);
        grades.add(88.7);
        
        double average = gradeManager.calculateAverage(grades);
        assertEquals(89.5, average, 0.1);
    }

    /**
     * Test average of empty grade list.
     */
    @Test
    public void testAverageEmptyList() {
        ArrayList<Double> grades = new ArrayList<>();
        double average = gradeManager.calculateAverage(grades);
        assertEquals(0.0, average, 0.01);
    }

    /**
     * Test letter grade for perfect score.
     */
    @Test
    public void testLetterGradePerfect() {
        double average = 100.0;
        String letterGrade = gradeManager.getLetterGrade(average);
        assertEquals("A", letterGrade);
    }

    /**
     * Test letter grade for failing score.
     */
    @Test
    public void testLetterGradeFailing() {
        double average = 45.0;
        String letterGrade = gradeManager.getLetterGrade(average);
        assertEquals("F", letterGrade);
    }

    /**
     * Test letter grade for A range.
     */
    @Test
    public void testLetterGradeARange() {
        assertEquals("A", gradeManager.getLetterGrade(90));
        assertEquals("A", gradeManager.getLetterGrade(95));
        assertEquals("A", gradeManager.getLetterGrade(100));
    }

    /**
     * Test letter grade for B range.
     */
    @Test
    public void testLetterGradeBRange() {
        assertEquals("B", gradeManager.getLetterGrade(80));
        assertEquals("B", gradeManager.getLetterGrade(85));
        assertEquals("B", gradeManager.getLetterGrade(89));
    }

    /**
     * Test letter grade for C range.
     */
    @Test
    public void testLetterGradeCRange() {
        assertEquals("C", gradeManager.getLetterGrade(70));
        assertEquals("C", gradeManager.getLetterGrade(75));
        assertEquals("C", gradeManager.getLetterGrade(79));
    }

    /**
     * Test letter grade for D range.
     */
    @Test
    public void testLetterGradeDRange() {
        assertEquals("D", gradeManager.getLetterGrade(60));
        assertEquals("D", gradeManager.getLetterGrade(65));
        assertEquals("D", gradeManager.getLetterGrade(69));
    }

    /**
     * Test letter grade for F range.
     */
    @Test
    public void testLetterGradeFRange() {
        assertEquals("F", gradeManager.getLetterGrade(0));
        assertEquals("F", gradeManager.getLetterGrade(30));
        assertEquals("F", gradeManager.getLetterGrade(59));
    }

    /**
     * Test letter grade boundaries.
     */
    @Test
    public void testLetterGradeBoundaries() {
        // Exact boundaries
        assertEquals("D", gradeManager.getLetterGrade(59.9));
        assertEquals("C", gradeManager.getLetterGrade(60.0));
        assertEquals("C", gradeManager.getLetterGrade(69.9));
        assertEquals("B", gradeManager.getLetterGrade(70.0));
        assertEquals("B", gradeManager.getLetterGrade(79.9));
        assertEquals("A", gradeManager.getLetterGrade(80.0));
    }

    /**
     * Test is valid grade (0-100).
     */
    @Test
    public void testIsValidGradeTrue() {
        assertTrue(gradeManager.isValidGrade(50));
        assertTrue(gradeManager.isValidGrade(0));
        assertTrue(gradeManager.isValidGrade(100));
        assertTrue(gradeManager.isValidGrade(75.5));
    }

    /**
     * Test is valid grade with invalid values.
     */
    @Test
    public void testIsValidGradeFalse() {
        assertFalse(gradeManager.isValidGrade(-1));
        assertFalse(gradeManager.isValidGrade(101));
        assertFalse(gradeManager.isValidGrade(-50));
        assertFalse(gradeManager.isValidGrade(150));
    }

    /**
     * Test highest grade in list.
     */
    @Test
    public void testHighestGrade() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(75.0);
        grades.add(92.0);
        grades.add(88.5);
        grades.add(85.0);
        
        double highest = gradeManager.getHighestGrade(grades);
        assertEquals(92.0, highest, 0.01);
    }

    /**
     * Test lowest grade in list.
     */
    @Test
    public void testLowestGrade() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(92.0);
        grades.add(75.0);
        grades.add(88.5);
        grades.add(85.0);
        
        double lowest = gradeManager.getLowestGrade(grades);
        assertEquals(75.0, lowest, 0.01);
    }

    /**
     * Test highest/lowest with single grade.
     */
    @Test
    public void testHighestLowestSingleGrade() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(85.0);
        
        assertEquals(85.0, gradeManager.getHighestGrade(grades), 0.01);
        assertEquals(85.0, gradeManager.getLowestGrade(grades), 0.01);
    }

    /**
     * Test with all same grades.
     */
    @Test
    public void testAllSameGrades() {
        ArrayList<Double> grades = new ArrayList<>();
        grades.add(80.0);
        grades.add(80.0);
        grades.add(80.0);
        
        double average = gradeManager.calculateAverage(grades);
        assertEquals(80.0, average, 0.01);
        assertEquals(80.0, gradeManager.getHighestGrade(grades), 0.01);
        assertEquals(80.0, gradeManager.getLowestGrade(grades), 0.01);
    }

    /**
     * Test grade scale consistency.
     */
    @Test
    public void testGradeScaleConsistency() {
        // Verify grade scale is consistent
        assertEquals("A", gradeManager.getLetterGrade(90));
        assertEquals("B", gradeManager.getLetterGrade(80));
        assertEquals("C", gradeManager.getLetterGrade(70));
        assertEquals("D", gradeManager.getLetterGrade(60));
        assertEquals("F", gradeManager.getLetterGrade(50));
    }

    /**
     * Test performance with large number of grades.
     */
    @Test
    public void testLargeNumberOfGrades() {
        ArrayList<Double> grades = new ArrayList<>();
        
        // Add 100 grades
        for (int i = 0; i < 100; i++) {
            grades.add(50.0 + (i % 50));  // Grades ranging from 50-100
        }
        
        double average = gradeManager.calculateAverage(grades);
        assertTrue(average > 0 && average <= 100);
        assertNotNull(gradeManager.getLetterGrade(average));
    }
}
