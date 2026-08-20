package com.grademanager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CsvService {
    private CsvService() {}

    public static void exportData(GradeManager manager, Path output) throws SQLException, IOException {
        List<String> lines = new ArrayList<>();
        lines.add("student_id,student_name,grade_id,subject,grade,letter_grade");
        for (Student student : manager.listStudents()) {
            List<GradeRecord> grades = manager.gradeHistory(student.getId());
            if (grades.isEmpty()) {
                lines.add(student.getId() + "," + csv(student.getName()) + ",,,,N/A");
            } else {
                for (GradeRecord grade : grades) {
                    lines.add(student.getId() + "," + csv(student.getName()) + "," + grade.id() + "," + csv(grade.subject()) + "," +
                            String.format(java.util.Locale.US, "%.2f", grade.grade()) + "," + GradeScale.letterGrade(grade.grade()));
                }
            }
        }
        Files.write(output, lines, StandardCharsets.UTF_8);
    }

    private static String csv(String value) {
        String v = value == null ? "" : value;
        return '"' + v.replace("\"", "\"\"") + '"';
    }
}
