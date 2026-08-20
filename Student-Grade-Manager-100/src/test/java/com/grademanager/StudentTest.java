package com.grademanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test void trimsStudentName() {
        assertEquals("Alice Johnson", new Student("  Alice Johnson  ").getName());
    }

    @Test void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Student("   "));
    }
}
