package sims.view;

import sims.controller.StudentManager;
import sims.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Student Management Panel with CRUD operations
 */
public class StudentPanel extends JPanel {
    
    private StudentManager studentManager;
    
    // Input fields
    private JTextField txtStudentID;
    private JTextField txtName;
    private JComboBox<String> cmbGender;
    private JTextField txtMajor;
    private JTextField txtYear;
    
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
    
    public StudentPanel() {
        studentManager = new StudentManager();
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
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentID = new JTextField(15);
        panel.add(txtStudentID, gbc);
        
        // Name
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        txtName = new JTextField(15);
        panel.add(txtName, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(cmbGender, gbc);
        
        // Major
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Major:"), gbc);
        gbc.gridx = 3;
        txtMajor = new JTextField(15);
        panel.add(txtMajor, gbc);
        
        // Year
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        txtYear = new JTextField(15);
        panel.add(txtYear, gbc);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"Student ID", "Name", "Gender", "Major", "Year"};
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
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudent());
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
    
    private void addStudent() {
        if (!validateInput()) return;
        
        Student student = new Student(
            txtStudentID.getText().trim(),
            txtName.getText().trim(),
            (String) cmbGender.getSelectedItem(),
            txtMajor.getText().trim(),
            txtYear.getText().trim()
        );
        
        studentManager.add(student);
        loadTableData();
        clearFields();
        JOptionPane.showMessageDialog(this, "Student added successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateStudent() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        Student student = new Student(
            txtStudentID.getText().trim(),
            txtName.getText().trim(),
            (String) cmbGender.getSelectedItem(),
            txtMajor.getText().trim(),
            txtYear.getText().trim()
        );
        
        studentManager.update(student);
        loadTableData();
        clearFields();
        JOptionPane.showMessageDialog(this, "Student updated successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteStudent() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String studentID = txtStudentID.getText().trim();
            studentManager.delete(studentID);
            loadTableData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchStudent() {
        String studentID = txtStudentID.getText().trim();
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to search!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Student student = studentManager.search(studentID);
        if (student != null) {
            txtName.setText(student.getName());
            cmbGender.setSelectedItem(student.getGender());
            txtMajor.setText(student.getMajor());
            txtYear.setText(student.getYear());
        } else {
            JOptionPane.showMessageDialog(this, "Student not found!", 
                "Not Found", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<Student> students = studentManager.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getStuID(), s.getName(), s.getGender(), s.getMajor(), s.getYear()
            });
        }
    }
    
    private void fillFieldsFromTable() {
        int row = table.getSelectedRow();
        txtStudentID.setText(tableModel.getValueAt(row, 0).toString());
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        cmbGender.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        txtMajor.setText(tableModel.getValueAt(row, 3).toString());
        txtYear.setText(tableModel.getValueAt(row, 4).toString());
    }
    
    private void clearFields() {
        txtStudentID.setText("");
        txtName.setText("");
        cmbGender.setSelectedIndex(0);
        txtMajor.setText("");
        txtYear.setText("");
        table.clearSelection();
    }
    
    private boolean validateInput() {
        if (txtStudentID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtMajor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Major cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtYear.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Year cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}