package sims.controller;

import java.sql.*;

/**
 * Database Helper class for Derby Embedded mode
 * Manages database connections and initialization
 * Implements Singleton pattern
 */
public class DBHelper {
    // Database will be created in project directory
    private static final String DB_NAME = "simsDB";
    private static final String DB_URL = "jdbc:derby:" + DB_NAME + ";create=true";
    private static Connection connection = null;

    /**
     * Get database connection (Singleton pattern)
     * @return Connection object or null if failed
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load Derby embedded driver
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("✓ Database connected successfully!");
                initializeTables();
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Derby driver not found: " + e.getMessage());
            System.err.println("Please add Derby library (Java DB Driver) to project!");
            return null;
        } catch (SQLException e) {
            System.err.println("✗ Database connection error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Initialize database tables if they don't exist
     */
    private static void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create Students table
            try {
                String createStudents = "CREATE TABLE Students (" +
                        "stuID VARCHAR(20) PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL, " +
                        "gender VARCHAR(10), " +
                        "major VARCHAR(50), " +
                        "year VARCHAR(10))";
                stmt.execute(createStudents);
                System.out.println("✓ Students table created");
            } catch (SQLException e) {
                if (!e.getSQLState().equals("X0Y32")) { // Table already exists
                    throw e;
                }
            }

            // Create Courses table
            try {
                String createCourses = "CREATE TABLE Courses (" +
                        "courseID VARCHAR(20) PRIMARY KEY, " +
                        "courseName VARCHAR(100) NOT NULL, " +
                        "credit DOUBLE)";
                stmt.execute(createCourses);
                System.out.println("✓ Courses table created");
            } catch (SQLException e) {
                if (!e.getSQLState().equals("X0Y32")) {
                    throw e;
                }
            }

            // Create Grades table with foreign key constraints
            try {
                String createGrades = "CREATE TABLE Grades (" +
                        "stuID VARCHAR(20), " +
                        "courseID VARCHAR(20), " +
                        "score DOUBLE, " +
                        "PRIMARY KEY (stuID, courseID), " +
                        "FOREIGN KEY (stuID) REFERENCES Students(stuID) ON DELETE CASCADE, " +
                        "FOREIGN KEY (courseID) REFERENCES Courses(courseID) ON DELETE CASCADE)";
                stmt.execute(createGrades);
                System.out.println("✓ Grades table created");
            } catch (SQLException e) {
                if (!e.getSQLState().equals("X0Y32")) {
                    throw e;
                }
            }

        } catch (SQLException e) {
            System.err.println("✗ Error initializing tables: " + e.getMessage());
        }
    }

    /**
     * Execute a prepared statement
     * @param query SQL query with parameters
     * @return PreparedStatement object
     * @throws SQLException if query fails
     */
    public static PreparedStatement prepareStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    /**
     * Close database connection properly
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
                
                // Shutdown Derby properly
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException e) {
                    // Expected exception on shutdown
                    if (e.getSQLState().equals("XJ015")) {
                        System.out.println("✓ Derby shutdown successfully");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error closing connection: " + e.getMessage());
        }
    }
}