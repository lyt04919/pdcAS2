package sims.controller;

import sims.model.Student;
import java.sql.*;
import java.util.ArrayList;

/**
 * Student Manager with Database operations
 * Implements Manager interface for CRUD operations
 */
public class StudentManager implements Manager<Student> {
    
    @Override
    public void add(Student s) {
        // Check if student already exists
        if (search(s.getStuID()) != null) {
            System.out.println("✗ Add failed! Student ID already exists: " + s.getStuID());
            return;
        }
        
        String sql = "INSERT INTO Students (stuID, name, gender, major, year) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, s.getStuID());
            pstmt.setString(2, s.getName());
            pstmt.setString(3, s.getGender());
            pstmt.setString(4, s.getMajor());
            pstmt.setString(5, s.getYear());
            pstmt.executeUpdate();
            System.out.println("✓ Student added successfully: " + s);
        } catch (SQLException e) {
            System.err.println("✗ Error adding student: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Students WHERE stuID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Deleted successfully! StudentID: " + id);
            } else {
                System.out.println("✗ Delete failed! Student ID not found: " + id);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error deleting student: " + e.getMessage());
        }
    }

    @Override
    public void update(Student s) {
        String sql = "UPDATE Students SET name=?, gender=?, major=?, year=? WHERE stuID=?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getGender());
            pstmt.setString(3, s.getMajor());
            pstmt.setString(4, s.getYear());
            pstmt.setString(5, s.getStuID());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✓ Updated successfully: " + s);
            } else {
                System.out.println("✗ Update failed! Student ID not found: " + s.getStuID());
            }
        } catch (SQLException e) {
            System.err.println("✗ Error updating student: " + e.getMessage());
        }
    }

    @Override
    public Student search(String id) {
        String sql = "SELECT * FROM Students WHERE stuID = ?";
        try (PreparedStatement pstmt = DBHelper.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getString("stuID"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("major"),
                    rs.getString("year")
                );
            }
        } catch (SQLException e) {
            System.err.println("✗ Error searching student: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void displayAll() {
        ArrayList<Student> students = getAllStudents();
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

    /**
     * Get all students from database
     * @return ArrayList of all students
     */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students ORDER BY stuID";
        try (Statement stmt = DBHelper.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getString("stuID"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getString("major"),
                    rs.getString("year")
                );
                students.add(s);
            }
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving students: " + e.getMessage());
        }
        return students;
    }
}