package sims.controller;

import sims.model.Student;
import java.util.ArrayList;

public class StudentManager implements Manager<Student> {
    private ArrayList<Student> students = new ArrayList<>();

    public StudentManager() {
        students = FileHandler.readStudents();
    }

    @Override
    public void add(Student s) {
        if (search(s.getStuID()) != null) {
            System.out.println("Add failed! Student ID already exists: " + s.getStuID());
            return;
        }
        students.add(s);
        FileHandler.writeStudents(students);
        System.out.println("Student added successfully: " + s);
    }

    @Override
    public void delete(String id) {
        boolean removed = students.removeIf(s -> s.getStuID().equals(id));
        if (removed) {
            FileHandler.writeStudents(students);
            System.out.println("Deleted successfully! StudentID: " + id);
        } else {
            System.out.println("Delete failed! Student ID not found: " + id);
        }
    }

    @Override
    public void update(Student s) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStuID().equals(s.getStuID())) {
                students.set(i, s);
                FileHandler.writeStudents(students);
                System.out.println("Updated successfully: " + s);
                return;
            }
        }
        System.out.println("Update failed! Student ID not found: " + s.getStuID());
    }

    @Override
    public Student search(String id) {
        for (Student student : students) {
            if (student.getStuID().equals(id)) {
                return student;
            }
        }
        return null;
    }

     @Override
    public void displayAll() {
        if (students.isEmpty()) {
            System.out.println("No student records found!");
        } else {
            System.out.println("==== Student List ====");
            System.out.println("StudentID,Name,Gender,Major,Year");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }
}
