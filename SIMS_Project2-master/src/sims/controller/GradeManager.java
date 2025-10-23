package sims.controller;

import sims.model.Grade;
import java.util.ArrayList;

public class GradeManager implements Manager<Grade> {

    private ArrayList<Grade> grades = new ArrayList<>();

    public GradeManager() {
        grades = FileHandler.readGrades();
    }

    @Override
    public void add(Grade g) {
        grades.add(g);
        FileHandler.writeGrades(grades);
        System.out.println("Grade added successfully: " + g);
    }

    @Override
    public void delete(String id) {
        String[] parts = id.split("-");
        if (parts.length != 2) {
            System.out.println("Delete failed! Format should be: StudentID-CourseID");
            return;
        }
        String stuId = parts[0];
        String courseId = parts[1];

        boolean removed = grades.removeIf(g -> g.getStuID().equals(stuId) && g.getCourseID().equals(courseId));
        if (removed) {
            FileHandler.writeGrades(grades);
            System.out.println("Deleted successfully! StudentID " + stuId + ", CourseID " + courseId);
        } else {
            System.out.println("Delete failed! Record not found: " + stuId + "," + courseId);
        }
    }

    @Override
    public void update(Grade g) {
        for (int i = 0; i < grades.size(); i++) {
            if (grades.get(i).getStuID().equals(g.getStuID()) &&
                    grades.get(i).getCourseID().equals(g.getCourseID())) {
                grades.set(i, g);
                FileHandler.writeGrades(grades);
                System.out.println("Updated successfully: " + g);
                return;
            }
        }
        System.out.println("Update failed! Record not found: " + g.getStuID() + "," + g.getCourseID());
    }

    @Override
    public Grade search(String id) {
        String[] parts = id.split("-");
        if (parts.length != 2) {
            System.out.println("Search failed! Format should be: StudentID-CourseID");
            return null;
        }
        String stuId = parts[0];
        String courseId = parts[1];

        for (Grade g : grades) {
            if (g.getStuID().equals(stuId) && g.getCourseID().equals(courseId)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public void displayAll() {
        if (grades.isEmpty()) {
            System.out.println("No grade records found!");
        } else {
            System.out.println("===== Grade List =====");
            System.out.println("StudentID,CourseID,Score");
            for (Grade g : grades) {
                System.out.println(g);
            }
        }
    }

    public void displayStuGrades(String stuId) {
        ArrayList<Grade> list = searchStuAll(stuId);
        if (list.isEmpty()) {
            System.out.println("No grades found for student " + stuId);
        } else {
            System.out.println("===== Grades for Student " + stuId + " =====");
            System.out.println("StudentID,CourseID,Score");
            for (Grade g : list) {
                System.out.println(g);
            }
        }
    }

    public void displayCourseGrades(String courseId) {
        ArrayList<Grade> list = searchCourseAll(courseId);
        if (list.isEmpty()) {
            System.out.println("No grades found for course " + courseId);
        } else {
            System.out.println("===== Grades for Course " + courseId + " =====");
            System.out.println("StudentID,CourseID,Score");
            for (Grade g : list) {
                System.out.println(g);
            }
        }
    }

    private ArrayList<Grade> searchStuAll(String id) {
        ArrayList<Grade> selectGrades = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getStuID().equals(id)) {
                selectGrades.add(g);
            }
        }
        return selectGrades;
    }

    private ArrayList<Grade> searchCourseAll(String id) {
        ArrayList<Grade> selectGrades = new ArrayList<>();
        for (Grade g : grades) {
            if (g.getCourseID().equals(id)) {
                selectGrades.add(g);
            }
        }
        return selectGrades;
    }
}
