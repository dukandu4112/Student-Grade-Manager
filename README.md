# Student Grade Manager

A comprehensive Java application for managing student grades with persistent SQLite database storage. This application allows educators and administrators to efficiently track student information, record grades, calculate averages, and manage academic performance data.

## 📋 Features

- **Student Management**
  - Add new students to the database
  - Search for existing students
  - Delete students and their associated grade records
  - View complete student roster

- **Grade Tracking**
  - Record multiple grades per student
  - Maintain complete grade history for each student
  - View all grades for a specific student
  - Automatic average calculation

- **Academic Analytics**
  - Calculate individual student averages
  - Generate class-wide average statistics
  - Automatic letter grade assignment (A, B, C, D, F)
  - View highest and lowest grades per student

- **Data Persistence**
  - SQLite database for reliable data storage
  - Automatic database initialization
  - Persistent storage across sessions
  - No data loss on application restart

- **User-Friendly Interface**
  - Interactive command-line menu system
  - Input validation and error handling
  - Confirmation prompts for destructive operations
  - Clear, formatted output

## 🛠️ Requirements

- **Java 17** or higher
- **Maven 3.6.0** or higher
- **SQLite JDBC 3.44.0.0** (automatically installed via Maven)

## 📥 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/dukandu4112/Student-Grade-Manager.git
cd Student-Grade-Manager
```

### 2. Build the Project
```bash
mvn clean install
```

This command will:
- Download all dependencies (including SQLite JDBC)
- Compile all Java source files
- Run any tests (if available)
- Create a JAR file with all dependencies included

### 3. Verify the Build
```bash
ls -l target/student-grade-manager.jar
```

You should see the JAR file listed in the `target/` directory.

## 🚀 Usage

### Running the Application
```bash
java -jar target/student-grade-manager.jar
```

### Main Menu Options

```
===== Student Grade Manager =====
1. Add student           - Register a new student in the system
2. Add grade             - Record a grade for a student
3. Show average grade    - View individual or class average
4. Show all students     - Display complete student roster
5. Edit student grade    - Add a new grade (maintains history)
6. Delete student        - Remove a student from the system
7. Search student        - Find and view student details
8. Exit                  - Close the application
```

### Example Workflow

```bash
# Start the application
java -jar target/student-grade-manager.jar

# 1. Add students
Enter your choice: 1
Enter student name: Alice Johnson
✓ Student 'Alice Johnson' added successfully.

# 2. Add grades
Enter your choice: 2
[List of students displayed]
Enter student number: 1
Enter grade (0-100): 95
Enter subject (optional, press Enter for 'General'): Mathematics
✓ Grade 95 added successfully for Alice Johnson.

# 3. View grades
Enter your choice: 3
Enter student number (or 0 for class average): 1
===== Student Average =====
Name: Alice Johnson
Average Grade: 95.00
Letter Grade: A

# 4. Exit
Enter your choice: 8
Goodbye!
```

## 📁 Project Structure

```
Student-Grade-Manager/
├── src/main/java/
│   ├── Main.java              # CLI interface and menu system
│   ├── Student.java           # Student data model with database support
│   ├── DatabaseManager.java   # Database operations and queries
│   ├── GradeManager.java       # Business logic for grade calculations
│   └── StudentDisplay.java    # Formatting and display utilities
├── target/
│   ├── classes/               # Compiled Java classes
│   └── student-grade-manager.jar  # Executable JAR file
├── pom.xml                    # Maven build configuration
├── README.md                  # This file
└── students.db                # SQLite database (created on first run)

```

## 🗄️ Database Schema

The application automatically creates an SQLite database with the following tables:

### Students Table
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Grades Table
```sql
CREATE TABLE grades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    grade REAL NOT NULL,
    subject TEXT DEFAULT 'General',
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

## 🔧 Building and Customization

### Update Java Version
Edit `pom.xml` and change the `<maven.compiler.source>` and `<maven.compiler.target>` properties:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

### Rebuild After Changes
```bash
mvn clean install
```

### Run with Different Main Class
If you add a new main class:
```bash
mvn clean package
java -cp target/student-grade-manager.jar com.grademanager.NewMainClass
```

## 📊 Grade Scale Reference

The application uses the following letter grade scale:

| Grade Range | Letter Grade |
|-------------|--------------|
| 90 - 100   | A            |
| 80 - 89    | B            |
| 70 - 79    | C            |
| 60 - 69    | D            |
| 0 - 59     | F            |

## 🐛 Troubleshooting

### Issue: "No such file or directory: pom.xml"
**Solution:** Make sure you're in the project root directory
```bash
cd Student-Grade-Manager
```

### Issue: "switch rules are not supported in -source 11"
**Solution:** Update Java version in `pom.xml` to 17 or higher and rebuild
```bash
mvn clean install
```

### Issue: "JAR will be empty - no content was marked for inclusion"
**Solution:** Ensure Java files are in `src/main/java/` directory
```bash
mkdir -p src/main/java
mv src/*.java src/main/java/
```

### Issue: Database file not created
**Solution:** The database is automatically created in the working directory on first run. Ensure you have write permissions in the directory where you run the JAR.

## 🚀 Future Enhancements

Potential features for future versions:

- [ ] **GUI Interface** - JavaFX graphical user interface
- [ ] **CSV Export** - Export student data to CSV files
- [ ] **Statistics Dashboard** - Visual grade distribution charts
- [ ] **Bulk Import** - Import students from CSV files
- [ ] **Grade Weights** - Assign weights to different assignment types
- [ ] **Attendance Tracking** - Track student attendance records
- [ ] **Parent Portal** - Web interface for parents to view grades
- [ ] **Email Notifications** - Automated grade notifications
- [ ] **Multi-class Support** - Manage multiple class sections
- [ ] **Unit Tests** - Comprehensive test suite with JUnit

## 📝 License

This project is part of a Computer Science portfolio. Feel free to use, modify, and distribute as needed for educational purposes.

## 👨‍💻 Author

**dukandu4112** - Computer Science Student

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs or issues
- Suggest new features
- Submit pull requests with improvements
- Improve documentation

## 📞 Support

For questions or issues, please open a GitHub issue in the repository.

---

**Last Updated:** July 3, 2026  
**Version:** 1.0.0  
**Status:** ✅ Production Ready
