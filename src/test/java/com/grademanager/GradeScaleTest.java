package com.grademanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeScaleTest {
    @Test void convertsBoundariesCorrectly() {
        assertEquals("A", GradeScale.letterGrade(100));
        assertEquals("A", GradeScale.letterGrade(90));
        assertEquals("B", GradeScale.letterGrade(89.99));
        assertEquals("C", GradeScale.letterGrade(70));
        assertEquals("D", GradeScale.letterGrade(60));
        assertEquals("F", GradeScale.letterGrade(59.99));
        assertEquals("F", GradeScale.letterGrade(0));
    }

    @Test void rejectsOutOfRangeGrades() {
        assertThrows(IllegalArgumentException.class, () -> GradeScale.letterGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> GradeScale.letterGrade(101));
    }
}
