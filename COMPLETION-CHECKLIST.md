# 100% Completion Checklist

Use this checklist after replacing the old repository contents.

- [x] One canonical Maven source tree under `src/main/java/com/grademanager`.
- [x] Removed duplicate legacy `src/Main.java` architecture.
- [x] Removed text-file persistence from the finished design.
- [x] SQLite persistence implemented.
- [x] Students and grade records are separate database entities.
- [x] Multiple grades per student implemented.
- [x] Subject tracking implemented.
- [x] Grade history implemented.
- [x] Student averages implemented.
- [x] Class average implemented.
- [x] Letter-grade conversion implemented.
- [x] Highest/lowest grade analytics implemented.
- [x] Student search implemented.
- [x] Rename student implemented.
- [x] Delete student with grade cascade implemented.
- [x] Edit and delete individual grades implemented.
- [x] Sorting by name/highest/lowest average implemented.
- [x] CSV export implemented.
- [x] Input/domain validation implemented.
- [x] Prepared SQL statements used.
- [x] Database foreign keys enabled.
- [x] JUnit tests added for models, grade scale, database CRUD, analytics, duplicate handling, and cascade delete.
- [x] GitHub Actions CI cleaned and simplified.
- [x] `.gitignore` covers database/build/export/IDE/generated files.
- [x] Maven Shade configured not to create `dependency-reduced-pom.xml`.
- [x] README rewritten to match actual behavior.
- [x] Architecture documentation added.
- [x] Contributing guide included.
- [x] MIT license included.
- [x] Java source compilation verified locally.
- [ ] After uploading: run `mvn clean verify` and confirm GitHub Actions is green.
- [ ] After uploading: delete legacy tracked files such as `cleaned`, `students.txt`, `dependency-reduced-pom.xml`, old `src/Main.java`, old `docs/scripts` that no longer apply.
- [ ] After CI passes: create GitHub release `v1.0.0` using `target/student-grade-manager.jar`.
