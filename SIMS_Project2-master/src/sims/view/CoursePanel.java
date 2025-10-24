package sims.view;

import sims.controller.CourseManager;
import sims.model.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Course Management Panel with CRUD operations
 */
public class CoursePanel extends JPanel {
    
    private CourseManager courseManager;
    
    // Input fields
    private JTextField txtCourseID;
    private JTextField txtCourseName;
    private JTextField txtCredit;
    
    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnClear;
    
    // Table
    private JTable table;
    private DefaultTableModel tableModel;
    
    public CoursePanel() {
        courseManager = new CourseManager();
        initComponents();
        loadTableData();
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
        panel.setBorder(BorderFactory.createTitledBorder("Course Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Course ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        txtCourseID = new JTextField(20);
        panel.add(txtCourseID, gbc);
        
        // Course Name
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 3;
        txtCourseName = new JTextField(20);
        panel.add(txtCourseName, gbc);
        
        // Credit
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Credit:"), gbc);
        gbc.gridx = 1;
        txtCredit = new JTextField(20);
        panel.add(txtCredit, gbc);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"Course ID", "Course Name", "Credit"};
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear");
        
        // Add action listeners
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnSearch.addActionListener(e -> searchCourse());
        btnRefresh.addActionListener(e -> loadTableData());
        btnClear.addActionListener(e -> clearFields());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnRefresh);
        panel.add(btnClear);
        
        return panel;
    }
    
    private void addCourse() {
        if (!validateInput()) return;
        
        try {
            Course course = new Course(
                txtCourseID.getText().trim(),
                txtCourseName.getText().trim(),
                Double.parseDouble(txtCredit.getText().trim())
            );
            
            courseManager.add(course);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Course added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credit must be a valid number!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCourse() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to update!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        try {
            Course course = new Course(
                txtCourseID.getText().trim(),
                txtCourseName.getText().trim(),
                Double.parseDouble(txtCredit.getText().trim())
            );
            
            courseManager.update(course);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Course updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credit must be a valid number!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCourse() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this course?\nThis will also delete all related grades!", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String courseID = txtCourseID.getText().trim();
            courseManager.delete(courseID);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Course deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchCourse() {
        String courseID = txtCourseID.getText().trim();
        if (courseID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Course ID to search!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Course course = courseManager.search(courseID);
        if (course != null) {
            txtCourseName.setText(course.getCourseName());
            txtCredit.setText(String.valueOf(course.getCredit()));
        } else {
            JOptionPane.showMessageDialog(this, "Course not found!", 
                "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<Course> courses = courseManager.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                c.getCourseID(), c.getCourseName(), c.getCredit()
            });
        }
    }
    
    private void fillFieldsFromTable() {
        int row = table.getSelectedRow();
        txtCourseID.setText(tableModel.getValueAt(row, 0).toString());
        txtCourseName.setText(tableModel.getValueAt(row, 1).toString());
        txtCredit.setText(tableModel.getValueAt(row, 2).toString());
    }
    
    private void clearFields() {
        txtCourseID.setText("");
        txtCourseName.setText("");
        txtCredit.setText("");
        table.clearSelection();
    }
    
    private boolean validateInput() {
        if (txtCourseID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtCourseName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course Name cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtCredit.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Credit cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate credit is a positive number
        try {
            double credit = Double.parseDouble(txtCredit.getText().trim());
            if (credit <= 0) {
                JOptionPane.showMessageDialog(this, "Credit must be a positive number!", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credit must be a valid number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
}