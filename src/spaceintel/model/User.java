package spaceintel.model;

/**
 * ============================================================================
 * CLASS: User.java
 * PURPOSE: Represents a user in the Space Intelligence System (Module 1).
 * EXPLANATION FOR BEGINNERS:
 * A model class (or POJO) holds data in memory. Here we store user credentials,
 * the person's full name, and their operational role for Role-Based Access Control.
 * ============================================================================
 */
public class User {

    // --- Attributes corresponding to Users table columns ---
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role;       // Administrator, Geospatial Analyst, Intelligence Officer, Disaster Management Officer, Data Scientist
    private String department;
    private String email;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor to quickly create a user object
    public User(int userId, String username, String password, String fullName, String role, String department, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
        this.email = email;
    }

    // --- Getters and Setters (Encapsulation) ---
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Helper method to check if this user is an Administrator
    public boolean isAdmin() {
        return "Administrator".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}
