package com.grademanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeRecordTest {
    @Test void defaultsBlankSubjectToGeneral() {
        assertEquals("General", new GradeRecord(1, 88, " ").subject());
    }

    @Test void rejectsInvalidGrade() {
        assertThrows(IllegalArgumentException.class, () -> new GradeRecord(1, 120, "Math"));
    }
}
