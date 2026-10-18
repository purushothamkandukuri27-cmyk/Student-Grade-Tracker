import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * CodeAlpha Java Internship - Task 1: Student Grade Tracker
 * Console-based program to input and manage student grades.
 * Uses ArrayLists to store data, and calculates average, highest and lowest scores.
 */
public class StudentGradeTracker {

    // ---------- Student class ----------
    static class Student {
        private String name;
        private final ArrayList<Double> grades = new ArrayList<>();

        Student(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        ArrayList<Double> getGrades() {
            return grades;
        }

        void addGrade(double grade) {
            grades.add(grade);
        }

        boolean hasGrades() {
            return !grades.isEmpty();
        }

        double getAverage() {
            if (grades.isEmpty()) return 0;
            double sum = 0;
            for (double g : grades) sum += g;
            return sum / grades.size();
        }

        double getHighest() {
            double max = grades.get(0);
            for (double g : grades) if (g > max) max = g;
            return max;
        }

        double getLowest() {
            double min = grades.get(0);
            for (double g : grades) if (g < min) min = g;
            return min;
        }

        String getLetterGrade() {
            double avg = getAverage();
            if (avg >= 90) return "A";
            if (avg >= 80) return "B";
            if (avg >= 70) return "C";
            if (avg >= 60) return "D";
            return "F";
        }
    }

    // ---------- Data ----------
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // ---------- Main ----------
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      STUDENT GRADE TRACKER");
        System.out.println("=====================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGradesToStudent();
                case 3 -> viewAllStudents();
                case 4 -> updateStudentName();
                case 5 -> removeStudent();
                case 6 -> printSummaryReport();
                case 7 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1-7.");
            }
            System.out.println();
        }
        scanner.close();
    }

    // ---------- Menu ----------
    private static void printMenu() {
        System.out.println("---------------- MENU ----------------");
        System.out.println("1. Add a new student");
        System.out.println("2. Add grades to a student");
        System.out.println("3. View all students");
        System.out.println("4. Rename a student");
        System.out.println("5. Remove a student");
        System.out.println("6. Print summary report");
        System.out.println("7. Exit");
        System.out.println("--------------------------------------");
    }

    // ---------- Features ----------
    private static void addStudent() {
        String name = readLine("Enter student name: ").trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (findStudent(name) != null) {
            System.out.println("A student with that name already exists.");
            return;
        }
        Student s = new Student(name);
        students.add(s);
        System.out.println("Student '" + name + "' added.");

        String more = readLine("Add grades now? (y/n): ").trim().toLowerCase();
        if (more.equals("y")) {
            enterGrades(s);
        }
    }

    private static void addGradesToStudent() {
        Student s = chooseStudent();
        if (s != null) enterGrades(s);
    }

    private static void enterGrades(Student s) {
        System.out.println("Enter grades (0-100) for " + s.getName() + ". Type -1 to stop.");
        while (true) {
            double grade = readDouble("  Grade: ");
            if (grade == -1) break;
            if (grade < 0 || grade > 100) {
                System.out.println("  Grade must be between 0 and 100.");
                continue;
            }
            s.addGrade(grade);
        }
        System.out.println("Grades saved. Total grades for " + s.getName() + ": " + s.getGrades().size());
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students recorded yet.");
            return;
        }
        System.out.println("Registered students:");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.println((i + 1) + ". " + s.getName() + " -> grades: " + s.getGrades());
        }
    }

    private static void updateStudentName() {
        Student s = chooseStudent();
        if (s == null) return;
        String newName = readLine("Enter new name: ").trim();
        if (newName.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        s.setName(newName);
        System.out.println("Name updated to '" + newName + "'.");
    }

    private static void removeStudent() {
        Student s = chooseStudent();
        if (s == null) return;
        students.remove(s);
        System.out.println("Student '" + s.getName() + "' removed.");
    }

    private static void printSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No students recorded yet.");
            return;
        }

        System.out.println("================================== SUMMARY REPORT ==================================");
        System.out.printf("%-20s %8s %10s %10s %10s %8s%n",
                "Name", "Grades", "Average", "Highest", "Lowest", "Letter");
        System.out.println("------------------------------------------------------------------------------------");

        double classTotal = 0;
        int classCount = 0;
        Student topStudent = null;
        Student bottomStudent = null;
        double overallHigh = Double.NEGATIVE_INFINITY;
        double overallLow = Double.POSITIVE_INFINITY;

        for (Student s : students) {
            if (s.hasGrades()) {
                System.out.printf("%-20s %8d %10.2f %10.2f %10.2f %8s%n",
                        s.getName(), s.getGrades().size(), s.getAverage(),
                        s.getHighest(), s.getLowest(), s.getLetterGrade());

                for (double g : s.getGrades()) {
                    classTotal += g;
                    classCount++;
                }
                if (s.getHighest() > overallHigh) {
                    overallHigh = s.getHighest();
                    topStudent = s;
                }
                if (s.getLowest() < overallLow) {
                    overallLow = s.getLowest();
                    bottomStudent = s;
                }
            } else {
                System.out.printf("%-20s %8d %10s %10s %10s %8s%n",
                        s.getName(), 0, "-", "-", "-", "-");
            }
        }

        System.out.println("------------------------------------------------------------------------------------");
        if (classCount == 0) {
            System.out.println("No grades have been entered yet.");
        } else {
            System.out.printf("Total students       : %d%n", students.size());
            System.out.printf("Class average        : %.2f%n", classTotal / classCount);
            System.out.printf("Highest score        : %.2f (%s)%n", overallHigh, topStudent.getName());
            System.out.printf("Lowest score         : %.2f (%s)%n", overallLow, bottomStudent.getName());
        }
        System.out.println("====================================================================================");
    }

    // ---------- Helpers ----------
    private static Student findStudent(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    private static Student chooseStudent() {
        if (students.isEmpty()) {
            System.out.println("No students recorded yet. Add a student first.");
            return null;
        }
        viewAllStudents();
        int index = readInt("Select student number: ");
        if (index < 1 || index > students.size()) {
            System.out.println("Invalid student number.");
            return null;
        }
        return students.get(index - 1);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // clear newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
