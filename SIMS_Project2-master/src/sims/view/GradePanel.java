package sims.view;

import sims.controller.GradeManager;
import sims.controller.StudentManager;
import sims.controller.CourseManager;
import sims.model.Grade;
import sims.model.Student;
import sims.model.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Grade Management Panel with CRUD operations (Manual Code)
 */
public class GradePanel extends JPanel {
    
    private GradeManager gradeManager;
    private StudentManager studentManager;
    private CourseManager courseManager;
    
    // Input fields
    private JComboBox<String> cmbStudentID;
    private JComboBox<String> cmbCourseID;
    private JTextField txtScore;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnClear;
    private JButton btnFilterStudent;
    private JButton btnFilterCourse;
    
    // Table
    private JTable table;
    private DefaultTableModel tableModel;
    
    public GradePanel() {
        // 初始化 Manager
        // (重要：这些 Manager 必须是为 GUI 重构过的版本)
        gradeManager = new GradeManager();
        studentManager = new StudentManager();
        courseManager = new CourseManager();
        
        initComponents();
        loadComboBoxData();
        loadTableData(gradeManager.getAllGrades()); // 初始加载所有数据
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel (North)
        add(createInputPanel(), BorderLayout.NORTH);
        
        // Table panel (Center)
        add(createTablePanel(), BorderLayout.CENTER);
        
        // Button panel (South)
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Grade Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        cmbStudentID = new JComboBox<>();
        cmbStudentID.setEditable(true); // 允许输入ID进行搜索
        panel.add(cmbStudentID, gbc);
        
        // Course ID
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 3;
        cmbCourseID = new JComboBox<>();
        cmbCourseID.setEditable(true); // 允许输入ID进行搜索
        panel.add(cmbCourseID, gbc);
        
        // Score
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Score:"), gbc);
        gbc.gridx = 1;
        txtScore = new JTextField(20);
        panel.add(txtScore, gbc);
        
        // Refresh button for combo boxes
        gbc.gridx = 2; gbc.gridy = 1;
        JButton btnRefreshData = new JButton("Refresh Lists");
        btnRefreshData.addActionListener(e -> loadComboBoxData());
        panel.add(btnRefreshData, gbc);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"Student ID", "Course ID", "Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFieldsFromTable();
            }
        });
        
        return new JScrollPane(table);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Main buttons
        JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh All");
        btnClear = new JButton("Clear");
        
        // Add action listeners
        btnAdd.addActionListener(e -> addGrade());
        btnUpdate.addActionListener(e -> updateGrade());
        btnDelete.addActionListener(e -> deleteGrade());
        btnSearch.addActionListener(e -> searchGrade());
        btnRefresh.addActionListener(e -> loadTableData(gradeManager.getAllGrades()));
        btnClear.addActionListener(e -> clearFields());
        
        mainButtons.add(btnAdd);
        mainButtons.add(btnUpdate);
        mainButtons.add(btnDelete);
        mainButtons.add(btnSearch);
        mainButtons.add(btnRefresh);
        mainButtons.add(btnClear);
        
        // Filter buttons
        JPanel filterButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnFilterStudent = new JButton("Filter by Student");
        btnFilterCourse = new JButton("Filter by Course");
        
        btnFilterStudent.addActionListener(e -> filterByStudent());
        btnFilterCourse.addActionListener(e -> filterByCourse());
        
        filterButtons.add(btnFilterStudent);
        filterButtons.add(btnFilterCourse);
        
        panel.add(mainButtons, BorderLayout.NORTH);
        panel.add(filterButtons, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // --- (这里是您缺失的代码) ---

    /**
     * Fills the input fields with data from the selected table row
     */
    private void fillFieldsFromTable() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        
        cmbStudentID.setSelectedItem(table.getValueAt(row, 0).toString());
        cmbCourseID.setSelectedItem(table.getValueAt(row, 1).toString());
        txtScore.setText(table.getValueAt(row, 2).toString());
        
        // Disable combo boxes as they form the primary key
        cmbStudentID.setEnabled(false);
        cmbCourseID.setEnabled(false);
    }
    
    /**
     * Clears all input fields
     */
    private void clearFields() {
        cmbStudentID.setSelectedItem("");
        cmbCourseID.setSelectedItem("");
        txtScore.setText("");
        
        // Re-enable combo boxes
        cmbStudentID.setEnabled(true);
        cmbCourseID.setEnabled(true);
        
        table.clearSelection();
    }
    
    /**
     * Loads (or re-loads) data into the Student and Course combo boxes
     */
    private void loadComboBoxData() {
        // Clear existing items
        cmbStudentID.removeAllItems();
        cmbCourseID.removeAllItems();
        
        // Load student IDs
        ArrayList<Student> students = studentManager.getAllStudents();
        for (Student s : students) {
            cmbStudentID.addItem(s.getStuID());
        }
        
        // Load course IDs
        ArrayList<Course> courses = courseManager.getAllCourses();
        for (Course c : courses) {
            cmbCourseID.addItem(c.getCourseID());
        }
        
        clearFields();
    }

    /**
     * Loads a given list of grades into the table
     * @param grades The list of grades to display
     */
    private void loadTableData(ArrayList<Grade> grades) {
        tableModel.setRowCount(0); // Clear table
        
        if (grades.isEmpty()) {
            return;
        }
        
        for (Grade g : grades) {
            tableModel.addRow(new Object[]{
                g.getStuID(),
                g.getCourseID(),
                g.getScore()
            });
        }
    }
    
    /**
     * Validates the input fields
     * @return true if valid, false otherwise
     */
    private boolean validateInput() {
        if (cmbStudentID.getSelectedItem() == null || cmbStudentID.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cmbCourseID.getSelectedItem() == null || cmbCourseID.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtScore.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Score cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addGrade() {
        if (!validateInput()) return;
        
        try {
            Grade grade = new Grade(
                cmbStudentID.getSelectedItem().toString().trim(),
                cmbCourseID.getSelectedItem().toString().trim(),
                Double.parseDouble(txtScore.getText().trim())
            );
            
            if (gradeManager.add(grade)) {
                loadTableData(gradeManager.getAllGrades());
                clearFields();
                JOptionPane.showMessageDialog(this, "Grade added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                 JOptionPane.showMessageDialog(this, "Failed to add grade. Record may already exist.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Score must be a valid number!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- (这是您被切断的方法的完整版本) ---
    private void updateGrade() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to update!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        try {
            Grade grade = new Grade(
                cmbStudentID.getSelectedItem().toString().trim(), // This will be from the disabled field
                cmbCourseID.getSelectedItem().toString().trim(), // This will be from the disabled field
                Double.parseDouble(txtScore.getText().trim())
            );
            
            if (gradeManager.update(grade)) {
                loadTableData(gradeManager.getAllGrades());
                clearFields();
                JOptionPane.showMessageDialog(this, "Grade updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update grade. Record not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Score must be a valid number!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGrade() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a grade to delete!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this grade record?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String stuId = cmbStudentID.getSelectedItem().toString();
            String courseId = cmbCourseID.getSelectedItem().toString();
            String compositeId = stuId + "-" + courseId;
            
            if (gradeManager.delete(compositeId)) {
                loadTableData(gradeManager.getAllGrades());
                clearFields();
                JOptionPane.showMessageDialog(this, "Grade deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete grade. Record not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchGrade() {
        if (!validateInput()) return;
        
        String stuId = cmbStudentID.getSelectedItem().toString().trim();
        String courseId = cmbCourseID.getSelectedItem().toString().trim();
        String compositeId = stuId + "-" + courseId;
        
        Grade g = gradeManager.search(compositeId);
        
        if (g != null) {
            // Load this single grade into the table
            ArrayList<Grade> result = new ArrayList<>();
            result.add(g);
            loadTableData(result);
            
            // Fill fields
            cmbStudentID.setSelectedItem(g.getStuID());
            cmbCourseID.setSelectedItem(g.getCourseID());
            txtScore.setText(String.valueOf(g.getScore()));
            
        } else {
            JOptionPane.showMessageDialog(this, "Grade record not found.", 
                "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void filterByStudent() {
        if (cmbStudentID.getSelectedItem() == null || cmbStudentID.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a Student ID to filter by.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String stuId = cmbStudentID.getSelectedItem().toString().trim();
        loadTableData(gradeManager.getGradesByStudent(stuId));
    }
    
    private void filterByCourse() {
         if (cmbCourseID.getSelectedItem() == null || cmbCourseID.getSelectedItem().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a Course ID to filter by.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String courseId = cmbCourseID.getSelectedItem().toString().trim();
        loadTableData(gradeManager.getGradesByCourse(courseId));
    }
}