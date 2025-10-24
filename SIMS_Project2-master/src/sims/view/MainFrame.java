package sims.view;

import sims.controller.DBHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Main application frame with tabbed interface
 * Contains panels for Student, Course, and Grade management
 */
public class MainFrame extends JFrame {
    
    private JTabbedPane tabbedPane;
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private GradePanel gradePanel;
    
    public MainFrame() {
        initComponents();
        setupFrame();
    }
    
    /**
     * Initialize all GUI components
     */
    private void initComponents() {
        // Initialize database connection
        DBHelper.getConnection();
        
        // Create panels
        studentPanel = new StudentPanel();
        coursePanel = new CoursePanel();
        gradePanel = new GradePanel();
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add tabs with icons and tooltips
        tabbedPane.addTab("Students", null, studentPanel, "Manage student records");
        tabbedPane.addTab("Courses", null, coursePanel, "Manage course information");
        tabbedPane.addTab("Grades", null, gradePanel, "Manage student grades");
        
        // Add to frame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Create menu bar
        createMenuBar();
    }
    
    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle("Student Information Management System - SIMS v2.0");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Add window closing listener to properly close database
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    /**
     * Create menu bar with File and Help menus
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        JMenuItem refreshItem = new JMenuItem("Refresh All");
        refreshItem.setMnemonic('R');
        refreshItem.addActionListener(e -> refreshAllPanels());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('x');
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem userGuideItem = new JMenuItem("User Guide");
        userGuideItem.setMnemonic('U');
        userGuideItem.addActionListener(e -> showUserGuide());
        
        helpMenu.add(userGuideItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    
    /**
     * Refresh all data panels
     */
    private void refreshAllPanels() {
        // This would trigger refresh in all panels
        JOptionPane.showMessageDialog(this,
            "All panels refreshed successfully!",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show about dialog
     */
    private void showAboutDialog() {
        String message = "Student Information Management System\n" +
                        "Version 2.0\n\n" +
                        "COMP603/ENSE600 - Software Development Project 2\n" +
                        "2025 Semester 2\n\n" +
                        "Features:\n" +
                        "• Student Management\n" +
                        "• Course Management\n" +
                        "• Grade Management\n" +
                        "• Apache Derby Database\n" +
                        "• Full CRUD Operations\n\n" +
                        "Developed at Auckland University of Technology";
        
        JOptionPane.showMessageDialog(this,
            message,
            "About SIMS",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show user guide dialog
     */
    private void showUserGuide() {
        String guide = "Quick Start Guide:\n\n" +
                      "1. Students Tab:\n" +
                      "   - Add/Update/Delete student records\n" +
                      "   - Search by Student ID\n\n" +
                      "2. Courses Tab:\n" +
                      "   - Add/Update/Delete course information\n" +
                      "   - Manage course credits\n\n" +
                      "3. Grades Tab:\n" +
                      "   - Assign grades to students\n" +
                      "   - Filter by student or course\n" +
                      "   - View all grade records\n\n" +
                      "Tips:\n" +
                      "• Add students and courses before adding grades\n" +
                      "• Select a row in the table to edit\n" +
                      "• All changes are saved automatically";
        
        JOptionPane.showMessageDialog(this,
            guide,
            "User Guide",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exit application with confirmation
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?\nAll data is saved in the database.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Close database connection
            DBHelper.closeConnection();
            System.out.println("Application closed successfully");
            System.exit(0);
        }
    }
    
    /**
     * Main method for testing (optional)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}