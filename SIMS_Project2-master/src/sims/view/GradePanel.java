package sims.view;

import sims.controller.DBHelper;
import sims.model.Grade;
import sims.controller.GradeManager;
import sims.controller.StudentManager;
import sims.controller.CourseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class GradePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtScore;
    private JComboBox<String> cmbStudentID, cmbCourseID;
    private GradeManager gradeManager;
    private StudentManager studentManager;
    private CourseManager courseManager;

    public GradePanel() {
        setLayout(new BorderLayout());

        gradeManager = new GradeManager();
        studentManager = new StudentManager();
        courseManager = new CourseManager();

        // === 顶部操作区 ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnFilterStu = new JButton("Filter by Student");
        JButton btnFilterCourse = new JButton("Filter by Course");

        topPanel.add(btnAdd);
        topPanel.add(btnUpdate);
        topPanel.add(btnDelete);
        topPanel.add(btnRefresh);
        topPanel.add(btnFilterStu);
        topPanel.add(btnFilterCourse);
        add(topPanel, BorderLayout.NORTH);

        // === 表格区 ===
        tableModel = new DefaultTableModel(new Object[]{"Student ID", "Course ID", "Score"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // === 底部编辑区 ===
        JPanel bottomPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        cmbStudentID = new JComboBox<>();
        cmbCourseID = new JComboBox<>();
        txtScore = new JTextField();

        bottomPanel.add(new JLabel("Student ID:"));
        bottomPanel.add(new JLabel("Course ID:"));
        bottomPanel.add(new JLabel("Score:"));
        bottomPanel.add(cmbStudentID);
        bottomPanel.add(cmbCourseID);
        bottomPanel.add(txtScore);
        add(bottomPanel, BorderLayout.SOUTH);

        // === 初始化 ===
        loadComboBoxData();
        loadTableData();

        // === 事件绑定 ===
        btnAdd.addActionListener(e -> addGrade());
        btnUpdate.addActionListener(e -> updateGrade());
        btnDelete.addActionListener(e -> deleteGrade());
        btnRefresh.addActionListener(e -> loadTableData());
        btnFilterStu.addActionListener(e -> filterByStudent());
        btnFilterCourse.addActionListener(e -> filterByCourse());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    cmbStudentID.setSelectedItem(tableModel.getValueAt(row, 0).toString());
                    cmbCourseID.setSelectedItem(tableModel.getValueAt(row, 1).toString());
                    txtScore.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
    }

    // === 加载下拉框 ===
    private void loadComboBoxData() {
        cmbStudentID.removeAllItems();
        try (ResultSet rs = DBHelper.executeQuery("SELECT stuID FROM Students")) {
            while (rs.next()) cmbStudentID.addItem(rs.getString("stuID"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }

        cmbCourseID.removeAllItems();
        try (ResultSet rs = DBHelper.executeQuery("SELECT courseID FROM Courses")) {
            while (rs.next()) cmbCourseID.addItem(rs.getString("courseID"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    // === 加载成绩表 ===
    private void loadTableData() {
        tableModel.setRowCount(0);
        try (ResultSet rs = DBHelper.executeQuery("SELECT * FROM Grades")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading grades: " + e.getMessage());
        }
    }

    // === 添加成绩 ===
    private void addGrade() {
        String stuID = (String) cmbStudentID.getSelectedItem();
        String courseID = (String) cmbCourseID.getSelectedItem();
        String scoreStr = txtScore.getText().trim();
        if (stuID == null || courseID == null || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        try {
            double score = Double.parseDouble(scoreStr);
            String sql = "INSERT INTO Grades (stuID, courseID, score) VALUES (?, ?, ?)";
            PreparedStatement pstmt = DBHelper.prepareStatement(sql);
            pstmt.setString(1, stuID);
            pstmt.setString(2, courseID);
            pstmt.setDouble(3, score);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Grade added successfully!");
            loadTableData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding grade: " + e.getMessage());
        }
    }

    // === 修改成绩 ===
    private void updateGrade() {
        String stuID = (String) cmbStudentID.getSelectedItem();
        String courseID = (String) cmbCourseID.getSelectedItem();
        String scoreStr = txtScore.getText().trim();
        if (stuID == null || courseID == null || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        try {
            double score = Double.parseDouble(scoreStr);
            String sql = "UPDATE Grades SET score = ? WHERE stuID = ? AND courseID = ?";
            PreparedStatement pstmt = DBHelper.prepareStatement(sql);
            pstmt.setDouble(1, score);
            pstmt.setString(2, stuID);
            pstmt.setString(3, courseID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Grade updated successfully!");
            loadTableData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating grade: " + e.getMessage());
        }
    }

    // === 删除成绩 ===
    private void deleteGrade() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        String stuID = tableModel.getValueAt(row, 0).toString();
        String courseID = tableModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Confirm delete?", "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM Grades WHERE stuID = ? AND courseID = ?";
                PreparedStatement pstmt = DBHelper.prepareStatement(sql);
                pstmt.setString(1, stuID);
                pstmt.setString(2, courseID);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Grade deleted successfully!");
                loadTableData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting grade: " + e.getMessage());
            }
        }
    }

    // === 按学生过滤 ===
    private void filterByStudent() {
        String stuId = JOptionPane.showInputDialog(this, "Enter Student ID to filter:");
        if (stuId == null || stuId.trim().isEmpty()) return;

        tableModel.setRowCount(0);
        try (PreparedStatement pstmt = DBHelper.prepareStatement("SELECT * FROM Grades WHERE stuID = ?")) {
            pstmt.setString(1, stuId.trim());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtering by student: " + e.getMessage());
        }
    }

    // === 按课程过滤 ===
    private void filterByCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to filter:");
        if (courseId == null || courseId.trim().isEmpty()) return;

        tableModel.setRowCount(0);
        try (PreparedStatement pstmt = DBHelper.prepareStatement("SELECT * FROM Grades WHERE courseID = ?")) {
            pstmt.setString(1, courseId.trim());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("stuID"),
                    rs.getString("courseID"),
                    rs.getDouble("score")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtering by course: " + e.getMessage());
        }
    }
}
