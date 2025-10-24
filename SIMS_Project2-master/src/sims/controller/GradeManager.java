package sims.controller;

import sims.model.Grade;
import java.sql.*;
import java.util.ArrayList;

/**
 * Grade Manager with Database operations
 */
public class GradeManager implements Manager<Grade> {

    @Override
    public void add(Grade g) {
        // Check if grade already exists
        if (search(g.getStuID() + "-" + g.getCourseID()) != null) {
            System.out.println("Add failed! Grade already exists for Student: " + 
                g.getStuID() + ", Course: " + g.getCourseID());
            return;
        }
        
        String sql = "INSERT INTO Grades (stuID, courseID, score) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, g.getStuID());
            pstmt.setString(2, g.getCourseID());
            pstmt.setDouble(3, g.getScore());
            pstmt.executeUpdate();
            System.out.println("Grade added successfully: " + g);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503")) {
                System.err.println("Error: Student ID or Course ID does not exist!");
            } else {
                System.err.println("Error adding grade: " + e.getMessage());
            }
        }
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

        String sql = "DELETE FROM Grades WHERE stuID = ? AND courseID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, stuId);
            pstmt.setString(2, courseId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Deleted successfully! StudentID: " + stuId + ", CourseID: " + courseId);
            } else {
                System.out.println("Delete failed! Record not found: " + stuId + ", " + courseId);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting grade: " + e.getMessage());
        }
    }

    @Override
    public void update(Grade g) {
        String sql = "UPDATE Grades SET score=? WHERE stuID=? AND courseID=?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setDouble(1, g.getScore());
            pstmt.setString(2, g.getStuID());
            pstmt.setString(3, g.getCourseID());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Updated successfully: " + g);
            } else {
                System.out.println("Update failed! Record not found: " + g.getStuID() + ", " + g.getCourseID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating grade: " + e.getMessage());
        }
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

        String sql = "SELECT * FROM Grades WHERE stuID = ? AND courseID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, stuId);
            pstmt.setString(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Grade(
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error searching grade: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void displayAll() {
        ArrayList<Grade> grades = getAllGrades();
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

    /**
     * Get all grades from database
     */
    public ArrayList<Grade> getAllGrades() {
        ArrayList<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM Grades ORDER BY stuID, courseID";
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Grade g = new Grade(
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                );
                grades.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving grades: " + e.getMessage());
        }
        return grades;
    }

    /**
     * Get all grades for a specific student
     */
    public ArrayList<Grade> getGradesByStudent(String stuId) {
        ArrayList<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM Grades WHERE stuID = ? ORDER BY courseID";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, stuId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Grade g = new Grade(
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                );
                grades.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student grades: " + e.getMessage());
        }
        return grades;
    }

    /**
     * Get all grades for a specific course
     */
    public ArrayList<Grade> getGradesByCourse(String courseId) {
        ArrayList<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM Grades WHERE courseID = ? ORDER BY stuID";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Grade g = new Grade(
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                );
                grades.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving course grades: " + e.getMessage());
        }
        return grades;
    }
    
}