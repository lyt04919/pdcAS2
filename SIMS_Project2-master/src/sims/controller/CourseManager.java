package sims.controller;

import sims.model.Course;
import java.util.ArrayList;

public class CourseManager implements Manager<Course> {

    private ArrayList<Course> courses;

    public CourseManager() {
        courses = FileHandler.readCourses();
    }

    @Override
    public void add(Course c) {
        if (search(c.getCourseID()) != null) {
            System.out.println("Add failed! Course ID already exists: " + c.getCourseID());
            return;
        }
        courses.add(c);
        FileHandler.writeCourses(courses);
        System.out.println("Course added successfully: " + c);
    }

    @Override
    public void delete(String id) {
        boolean removed = courses.removeIf(c -> c.getCourseID().equals(id));
        if (removed) {
            FileHandler.writeCourses(courses);
            System.out.println("Deleted successfully! Course ID: " + id);
        } else {
            System.out.println("Delete failed! Course ID not found: " + id);
        }
    }

    @Override
    public void update(Course c) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseID().equals(c.getCourseID())) {
                courses.set(i, c);
                FileHandler.writeCourses(courses);
                System.out.println("Updated successfully: " + c);
                return;
            }
        }
        System.out.println("Update failed! Course ID not found: " + c.getCourseID());
    }

    @Override
    public Course search(String id) {
        for (Course c : courses) {
            if (c.getCourseID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void displayAll() {
        if (courses.isEmpty()) {
            System.out.println("No course records found!");
        } else {
            System.out.println("==== Course List ====");
            System.out.println("CourseID,CourseName,Credit");
            for (Course c : courses) {
                System.out.println(c);
            }
        }
    }
}
