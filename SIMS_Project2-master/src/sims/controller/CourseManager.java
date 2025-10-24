package sims.controller;

import sims.model.Course;
import java.sql.*;
import java.util.ArrayList;

/**
 * Course Manager with Database operations
 */
public class CourseManager implements Manager<Course> {
    
    @Override
    public void add(Course c) {
        if (search(c.getCourseID()) != null) {
            System.out.println("Add failed! Course ID already exists: " + c.getCourseID());
            return;
        }
        
        String sql = "INSERT INTO Courses (courseID, courseName, credit) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, c.getCourseID());
            pstmt.setString(2, c.getCourseName());
            pstmt.setDouble(3, c.getCredit());
            pstmt.executeUpdate();
            System.out.println("Course added successfully: " + c);
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Courses WHERE courseID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Deleted successfully! CourseID: " + id);
            } else {
                System.out.println("Delete failed! Course ID not found: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
        }
    }

    @Override
    public void update(Course c) {
        String sql = "UPDATE Courses SET courseName=?, credit=? WHERE courseID=?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, c.getCourseName());
            pstmt.setDouble(2, c.getCredit());
            pstmt.setString(3, c.getCourseID());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Updated successfully: " + c);
            } else {
                System.out.println("Update failed! Course ID not found: " + c.getCourseID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
        }
    }

    @Override
    public Course search(String id) {
        String sql = "SELECT * FROM Courses WHERE courseID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Course(
                    rs.getString("courseID"),
                    rs.getString("courseName"),
                    rs.getDouble("credit")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching course: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void displayAll() {
        ArrayList<Course> courses = getAllCourses();
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

    /**
     * Get all courses from database
     */
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses ORDER BY courseID";
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course c = new Course(
                    rs.getString("courseID"),
                    rs.getString("courseName"),
                    rs.getDouble("credit")
                );
                courses.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
        }
        return courses;
    }
}