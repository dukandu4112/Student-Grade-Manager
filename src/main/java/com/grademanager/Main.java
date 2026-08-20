package com.grademanager;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Scanner;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        Path dbPath = Path.of(System.getProperty("grademanager.db", "students.db"));
        try (DatabaseManager database = new DatabaseManager(dbPath);
             Scanner scanner = new Scanner(System.in)) {
            new ConsoleApp(new GradeManager(database), scanner).run();
        } catch (SQLException e) {
            System.err.println("Unable to start Student Grade Manager: " + e.getMessage());
            System.exit(1);
        }
    }
}
