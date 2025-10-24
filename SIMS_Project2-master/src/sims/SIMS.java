package sims;

import sims.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for Student Information Management System
 * COMP603/ENSE600 Project 2
 * 2025 Semester 2
 * 
 * @author Your Name
 * @version 2.0
 */
public class SIMS {
    
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
            // Continue with default look and feel
        }
        
        // Launch GUI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("========================================");
            System.out.println("Student Information Management System");
            System.out.println("Version 2.0");
            System.out.println("========================================");
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}