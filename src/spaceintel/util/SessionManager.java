package spaceintel.util;

import spaceintel.model.User;

/**
 * ============================================================================
 * CLASS: SessionManager.java
 * PURPOSE: Maintains the currently logged-in user session (Module 1).
 * EXPLANATION FOR BEGINNERS:
 * When a user logs in, we keep their User object in memory so any screen
 * can check who is logged in and what role they hold.
 * 
 * ROLES SUPPORTED:
 * 1. Administrator - Full access to all screens and user management
 * 2. Geospatial Analyst - Ingests observations, views credibility & confidence
 * 3. Intelligence Officer - Reviews intelligence dashboards, reports, and alerts
 * 4. Disaster Management Officer - Coordinates emergency alerts and regional scans
 * 5. Data Scientist - Reviews anomaly statistics and AI confidence parameters
 * ============================================================================
 */
public class SessionManager {

    private static User currentUser;

    public static synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    public static synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public static synchronized void logout() {
        currentUser = null;
    }

    public static synchronized String getRole() {
        return currentUser != null ? currentUser.getRole() : "GUEST";
    }

    public static synchronized String getUsername() {
        return currentUser != null ? currentUser.getUsername() : "Anonymous";
    }

    public static synchronized String getFullName() {
        return currentUser != null ? currentUser.getFullName() : "Guest User";
    }

    public static synchronized boolean isAdmin() {
        return currentUser != null && "Administrator".equalsIgnoreCase(currentUser.getRole());
    }
}
