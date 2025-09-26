import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Course {
    private final String courseId;
    private final String courseName;

    public Course(String id, String name) {
        this.courseId = id;
        this.courseName = name;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }

    @Override
    public String toString() { return courseId + " - " + courseName; }
}

class Student {
    private final int id;
    private String name;
    private String password;  
    private final List<Course> enrolledCourses;
    private Faculty mentor;

    public Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.enrolledCourses = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }

    public void setName(String n) { this.name = n; }
    public void setPassword(String p) { this.password = p; }

    public List<Course> getEnrolledCourses() { return enrolledCourses; }

    public void enrollCourse(Course c) { enrolledCourses.add(c); }

    public Faculty getMentor() { return mentor; }
    public void setMentor(Faculty f) { this.mentor = f; }

    public void displayStudent() {
        System.out.println("Student ID: " + id + ", Name: " + name);
        System.out.println("Enrolled Courses:");
        if (enrolledCourses.isEmpty()) {
            System.out.println("  - None");
        } else {
            for (Course c : enrolledCourses) {
                System.out.println("  - " + c.getCourseId() + " " + c.getCourseName());
            }
        }
        if (mentor != null) {
            System.out.println("Mentor: " + mentor.getName() + " (Subject: " + mentor.getSubject() + ")");
        } else {
            System.out.println("Mentor: None assigned");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id + "," + name + ",Courses:[");
        for (int i = 0; i < enrolledCourses.size(); i++) {
            if (i > 0) sb.append(';');
            sb.append(enrolledCourses.get(i).getCourseId());
        }
        sb.append("], Mentor:" + (mentor != null ? mentor.getName() : "None"));
        return sb.toString();
    }
}

class Faculty {
    private final int id;
    private final String name;
    private final String subject;
    private final List<Student> mentees;

    public Faculty(int id, String name, String subject) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.mentees = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public List<Student> getMentees() { return mentees; }

    public void addMentee(Student s) { mentees.add(s); }

    public void displayFaculty() {
        System.out.println("Faculty ID: " + id + ", Name: " + name + ", Subject: " + subject);
        System.out.println("Mentoring Students:");
        if (mentees.isEmpty()) {
            System.out.println("  - None");
        } else {
            for (Student s : mentees) {
                System.out.println("  - " + s.getId() + " " + s.getName());
            }
        }
    }
}

class Admin extends Student {
    public Admin(int id, String name, String password) {
        super(id, name, password);
    }
    public void manageSystem() {
        System.out.println("Admin " + getName() + " has system access.");
    }
}

public class Myhello {
    private static final List<Student> students = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Faculty> teachers = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty.");
        }
    }

    public static void addCourse() {
        String id = readNonEmpty("Enter Course ID: ");
        String name = readNonEmpty("Enter Course Name: ");
        courses.add(new Course(id, name));
        System.out.println("Course added.");
    }

    public static void addStudent() {
        int id = readInt("Enter Student ID (number): ");
        String name = readNonEmpty("Enter Student Name: ");
        String pass = readNonEmpty("Set Password: "); // ✅ password input
        students.add(new Student(id, name, pass));
        System.out.println("Student added with login credentials.");
    }

    public static void addFaculty() {
        int id = readInt("Enter Faculty ID (number): ");
        String name = readNonEmpty("Enter Faculty Name: ");
        String subject = readNonEmpty("Enter Subject: ");
        teachers.add(new Faculty(id, name, subject));
        System.out.println("Faculty added.");
    }

    
    public static Student studentLogin() {
        int id = readInt("Enter Student ID: ");
        String pass = readNonEmpty("Enter Password: ");
        for (Student s : students) {
            if (s.getId() == id && s.getPassword().equals(pass)) {
                System.out.println("Login successful. Welcome " + s.getName() + "!");
                return s;
            }
        }
        System.out.println("Invalid ID or Password.");
        return null;
    }

    public static void assignMentor() {
        if (students.isEmpty() || teachers.isEmpty()) {
            System.out.println("Add at least one student and one teacher first.");
            return;
        }

        System.out.println("Students:");
        for (Student s : students) System.out.println("  " + s.getId() + " - " + s.getName());
        int sid = readInt("Enter Student ID to assign mentor: ");

        Student selStu = null;
        for (Student s : students) if (s.getId() == sid) { selStu = s; break; }
        if (selStu == null) { System.out.println("Student not found."); return; }

        System.out.println("Teachers:");
        for (Faculty f : teachers) System.out.println("  " + f.getId() + " - " + f.getName() + " (" + f.getSubject() + ")");
        int fid = readInt("Enter Faculty ID to assign as mentor: ");

        Faculty selFac = null;
        for (Faculty f : teachers) if (f.getId() == fid) { selFac = f; break; }
        if (selFac == null) { System.out.println("Faculty not found."); return; }

        selStu.setMentor(selFac);
        selFac.addMentee(selStu);
        System.out.println("Assigned " + selFac.getName() + " as mentor to " + selStu.getName() + ".");
    }

    public static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }
        for (Student s : students) {
            s.displayStudent();
            System.out.println("-----------------------");
        }
    }

    public static void displayAllFaculty() {
        if (teachers.isEmpty()) {
            System.out.println("No faculty to display.");
            return;
        }
        for (Faculty f : teachers) {
            f.displayFaculty();
            System.out.println("-----------------------");
        }
    }

    public static void saveDataToFile() {
        try (FileWriter fw = new FileWriter("students.txt")) {
            for (Student s : students) fw.write(s.toString() + System.lineSeparator());
            System.out.println("Data saved to students.txt");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void enrollStudent() {
        Student selStu = studentLogin(); 
        if (selStu == null) return;

        if (courses.isEmpty()) {
            System.out.println("No courses available. Add a course first.");
            return;
        }

        System.out.println("Courses:");
        for (Course c : courses) System.out.println("  " + c.getCourseId() + " - " + c.getCourseName());
        String cid = readNonEmpty("Enter Course ID to enroll: ");

        Course selCourse = null;
        for (Course c : courses) if (c.getCourseId().equals(cid)) { selCourse = c; break; }
        if (selCourse == null) { System.out.println("Course not found."); return; }

        selStu.enrollCourse(selCourse);
        System.out.println("Enrolled " + selStu.getName() + " in " + selCourse.getCourseName() + ".");
    }

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println();
            System.out.println("===== Student Course Management System =====");
            System.out.println("1. Add Course");
            System.out.println("2. Add Student");
            System.out.println("3. Add Faculty");
            System.out.println("4. Student Login & Enroll in Course");
            System.out.println("5. Assign Mentor (Faculty to Student)");
            System.out.println("6. Display All Students");
            System.out.println("7. Display All Faculty");
            System.out.println("8. Save Data to File");
            System.out.println("9. Exit");
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addCourse(); break;
                case 2: addStudent(); break;
                case 3: addFaculty(); break;
                case 4: enrollStudent(); break;
                case 5: assignMentor(); break;
                case 6: displayAllStudents(); break;
                case 7: displayAllFaculty(); break;
                case 8: saveDataToFile(); break;
                case 9: System.out.println("Goodbye."); break;
                default: System.out.println("Invalid choice.");
            }
        } while (choice != 9);
    }
}
