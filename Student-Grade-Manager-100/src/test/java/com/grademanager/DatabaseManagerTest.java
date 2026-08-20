package com.grademanager;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    @Test void fullCrudAndAnalyticsWorkflow() throws Exception {
        try (DatabaseManager db = new DatabaseManager("jdbc:sqlite::memory:")) {
            GradeManager manager = new GradeManager(db);
            Student alice = manager.addStudent("Alice Johnson");
            Student bob = manager.addStudent("Bob Smith");

            GradeRecord g1 = manager.addGrade(alice.getId(), 95, "Mathematics");
            manager.addGrade(alice.getId(), 85, "English");
            manager.addGrade(bob.getId(), 70, "Mathematics");

            assertEquals(90.0, manager.studentAverage(alice.getId()), 0.0001);
            assertEquals(83.3333, manager.classAverage(), 0.001);
            assertEquals(2, manager.gradeHistory(alice.getId()).size());
            assertEquals(95.0, manager.highestGrade(alice.getId()).orElseThrow().grade());
            assertEquals(85.0, manager.lowestGrade(alice.getId()).orElseThrow().grade());

            assertTrue(manager.editGrade(g1.id(), 100, "Advanced Mathematics"));
            assertEquals(92.5, manager.studentAverage(alice.getId()), 0.0001);
            assertEquals(1, manager.searchStudents("alice").size());
            assertTrue(manager.renameStudent(bob.getId(), "Robert Smith"));
            assertEquals("Robert Smith", manager.findStudent(bob.getId()).orElseThrow().getName());

            assertTrue(manager.deleteGrade(g1.id()));
            assertEquals(1, manager.gradeHistory(alice.getId()).size());
            assertTrue(manager.deleteStudent(bob.getId()));
            assertTrue(manager.findStudent(bob.getId()).isEmpty());
        }
    }

    @Test void duplicateNamesAreRejectedCaseInsensitively() throws Exception {
        try (DatabaseManager db = new DatabaseManager("jdbc:sqlite::memory:")) {
            db.addStudent("Alice");
            assertThrows(SQLException.class, () -> db.addStudent("alice"));
        }
    }

    @Test void deletingStudentCascadesGrades() throws Exception {
        try (DatabaseManager db = new DatabaseManager("jdbc:sqlite::memory:")) {
            Student s = db.addStudent("Casey");
            db.addGrade(s.getId(), 91, "Science");
            assertTrue(db.deleteStudent(s.getId()));
            assertTrue(db.getGradesForStudent(s.getId()).isEmpty());
        }
    }
}
