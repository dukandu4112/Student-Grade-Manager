package com.grademanager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public final class GradeManager {
    private final DatabaseManager database;

    public GradeManager(DatabaseManager database) {
        this.database = database;
    }

    public Student addStudent(String name) throws SQLException { return database.addStudent(name); }
    public List<Student> listStudents() throws SQLException { return database.getAllStudents(); }
    public List<Student> searchStudents(String query) throws SQLException { return database.searchStudents(query); }
    public Optional<Student> findStudent(int id) throws SQLException { return database.findStudentById(id); }
    public boolean renameStudent(int id, String name) throws SQLException { return database.renameStudent(id, name); }
    public boolean deleteStudent(int id) throws SQLException { return database.deleteStudent(id); }

    public GradeRecord addGrade(int studentId, double grade, String subject) throws SQLException {
        return database.addGrade(studentId, grade, subject);
    }

    public List<GradeRecord> gradeHistory(int studentId) throws SQLException { return database.getGradesForStudent(studentId); }
    public boolean editGrade(int gradeId, double grade, String subject) throws SQLException { return database.updateGrade(gradeId, grade, subject); }
    public boolean deleteGrade(int gradeId) throws SQLException { return database.deleteGrade(gradeId); }
    public double studentAverage(int studentId) throws SQLException { return database.getStudentAverage(studentId); }
    public double classAverage() throws SQLException { return database.getClassAverage(); }
    public Optional<GradeRecord> highestGrade(int studentId) throws SQLException { return database.getHighestGradeForStudent(studentId); }
    public Optional<GradeRecord> lowestGrade(int studentId) throws SQLException { return database.getLowestGradeForStudent(studentId); }
    public List<DatabaseManager.StudentSummary> summaries(String sort) throws SQLException { return database.getStudentSummaries(sort); }
}
