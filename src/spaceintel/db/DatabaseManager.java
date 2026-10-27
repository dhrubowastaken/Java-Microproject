package spaceintel.db;

import spaceintel.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * CLASS: DatabaseManager.java
 * PURPOSE: Central Database Access Layer managing all 16 Normalized Tables.
 * EXPLANATION FOR BEGINNERS:
 * JDBC (Java Database Connectivity) allows Java to talk to a database.
 * We connect to an embedded SQLite database file named "space_intel.db".
 * 
 * WHY THIS IS GREAT FOR BEGINNERS & VIVAS:
 * 1. You don't need to manually configure MySQL or XAMPP; the database file is
 *    created right inside your project directory!
 * 2. If the database file is missing, it reads "resources/schema.sql" automatically
 *    and sets up all 16 tables with realistic Xovian Aerospace data!
 * 3. We also provide "resources/schema.sql" which is 100% compatible with MySQL
 *    if your professor specifically asks for a MySQL dump.
 * ============================================================================
 */
public class DatabaseManager {

    // Database file location
    private static final String DB_FILE = "space_intel.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    // Singleton instance
    private static DatabaseManager instance;

    // Private constructor so only one instance exists (Singleton Design Pattern)
    private DatabaseManager() {
        initDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Gets a live database connection.
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseManager] SQLite JDBC Driver not found in classpath!");
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Step 1: Checks if database exists; if not, creates tables and seeds demo data.
     */
    public void initDatabase() {
        File dbFile = new File(DB_FILE);
        boolean exists = dbFile.exists() && dbFile.length() > 0;

        if (!exists) {
            System.out.println("[DatabaseManager] Initializing new database with 16 tables from schema.sql...");
            executeSchemaScript();
        } else {
            System.out.println("[DatabaseManager] Connected to existing space_intel.db database.");
        }
    }

    /**
     * Reads and executes SQL statements from "resources/schema.sql".
     */
    public void executeSchemaScript() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String scriptContent = "";
            File schemaFile = new File("resources/schema.sql");
            if (schemaFile.exists()) {
                scriptContent = new String(Files.readAllBytes(schemaFile.toPath()));
            } else {
                // Fallback to classpath if running from packaged jar
                InputStream is = getClass().getResourceAsStream("/resources/schema.sql");
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    scriptContent = sb.toString();
                }
            }

            if (scriptContent.isEmpty()) {
                System.err.println("[DatabaseManager] Error: schema.sql file could not be read.");
                return;
            }

            List<String> statements = parseSqlStatements(scriptContent);
            for (String cmd : statements) {
                if (!cmd.isEmpty()) {
                    try {
                        stmt.execute(cmd);
                    } catch (SQLException e) {
                        System.err.println("[DatabaseManager] SQL notice: " + e.getMessage() + " in: " + (cmd.length() > 50 ? cmd.substring(0, 50) + "..." : cmd));
                    }
                }
            }
            System.out.println("[DatabaseManager] Database setup successfully completed with 16 tables!");

        } catch (Exception e) {
            System.err.println("[DatabaseManager] Failed to execute schema script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses a SQL script into individual executable statements,
     * correctly handling semicolons inside single-quoted strings and stripping comments.
     */
    private List<String> parseSqlStatements(String script) {
        List<String> list = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuote = false;
        boolean inLineComment = false;

        for (int i = 0; i < script.length(); i++) {
            char c = script.charAt(i);

            // Handle line comments (-- ...)
            if (!inQuote && c == '-' && i + 1 < script.length() && script.charAt(i + 1) == '-') {
                inLineComment = true;
                i++; // skip next dash
                continue;
            }

            if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                }
                continue;
            }

            // Handle string literal quotes ('...')
            if (c == '\'') {
                inQuote = !inQuote;
                current.append(c);
                continue;
            }

            // Statement delimiter outside of quotes
            if (c == ';' && !inQuote) {
                String statement = current.toString().trim();
                if (!statement.isEmpty()) {
                    list.add(statement);
                }
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        String remaining = current.toString().trim();
        if (!remaining.isEmpty()) {
            list.add(remaining);
        }

        return list;
    }

    /**
     * Resets the entire database back to fresh demo data.
     */
    public void resetToDemoData() {
        File f = new File(DB_FILE);
        if (f.exists()) {
            f.delete();
        }
        executeSchemaScript();
    }

    // =========================================================================
    // MODULE 1: USER MANAGEMENT & AUTHENTICATION
    // =========================================================================

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("department"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY user_id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("department"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO Users (username, password, full_name, role, department, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getDepartment());
            ps.setString(6, user.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // MODULE 2: DATA SOURCE MANAGEMENT
    // =========================================================================

    public List<DataSource> getAllDataSources() {
        List<DataSource> list = new ArrayList<>();
        String sql = "SELECT * FROM Data_Sources ORDER BY source_id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DataSource(
                        rs.getString("source_id"),
                        rs.getString("source_name"),
                        rs.getString("source_type"),
                        rs.getString("organization"),
                        rs.getString("coverage_area"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addDataSource(DataSource ds) {
        String sql = "INSERT INTO Data_Sources (source_id, source_name, source_type, organization, coverage_area, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ds.getSourceId());
            ps.setString(2, ds.getSourceName());
            ps.setString(3, ds.getSourceType());
            ps.setString(4, ds.getOrganization());
            ps.setString(5, ds.getCoverageArea());
            ps.setString(6, ds.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // MODULE 3: OBSERVATION MANAGEMENT
    // =========================================================================

    public List<Observation> getAllObservations() {
        List<Observation> list = new ArrayList<>();
        String sql = "SELECT * FROM Observations ORDER BY timestamp DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Observation(
                        rs.getString("observation_id"),
                        rs.getString("timestamp"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("observation_type"),
                        rs.getDouble("measured_value"),
                        rs.getString("unit"),
                        rs.getString("source_id"),
                        rs.getString("region_id"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addObservation(Observation obs) {
        String sql = "INSERT INTO Observations (observation_id, latitude, longitude, observation_type, measured_value, unit, source_id, region_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obs.getObservationId());
            ps.setDouble(2, obs.getLatitude());
            ps.setDouble(3, obs.getLongitude());
            ps.setString(4, obs.getObservationType());
            ps.setDouble(5, obs.getMeasuredValue());
            ps.setString(6, obs.getUnit());
            ps.setString(7, obs.getSourceId());
            ps.setString(8, obs.getRegionId());
            ps.setString(9, obs.getStatus());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                // Also insert into Observation_History (Table 4)
                logObservationHistory(obs);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void logObservationHistory(Observation obs) {
        String sql = "INSERT INTO Observation_History (observation_id, latitude, longitude, measured_value, source_id, flag) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obs.getObservationId());
            ps.setDouble(2, obs.getLatitude());
            ps.setDouble(3, obs.getLongitude());
            ps.setDouble(4, obs.getMeasuredValue());
            ps.setString(5, obs.getSourceId());
            ps.setString(6, "NEW_INGESTION");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // MODULE 4: SOURCE CREDIBILITY MANAGEMENT
    // =========================================================================

    public List<SourceCredibility> getAllSourceCredibility() {
        List<SourceCredibility> list = new ArrayList<>();
        String sql = "SELECT * FROM Source_Credibility ORDER BY reliability_score DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new SourceCredibility(
                        rs.getInt("credibility_id"),
                        rs.getString("source_id"),
                        rs.getDouble("reliability_score"),
                        rs.getDouble("historical_accuracy"),
                        rs.getString("update_frequency"),
                        rs.getString("validation_status"),
                        rs.getString("confidence_level"),
                        rs.getString("last_evaluated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateSourceCredibility(SourceCredibility sc) {
        String sql = "UPDATE Source_Credibility SET reliability_score = ?, historical_accuracy = ?, update_frequency = ?, validation_status = ?, confidence_level = ?, last_evaluated = CURRENT_TIMESTAMP WHERE source_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, sc.getReliabilityScore());
            ps.setDouble(2, sc.getHistoricalAccuracy());
            ps.setString(3, sc.getUpdateFrequency());
            ps.setString(4, sc.getValidationStatus());
            ps.setString(5, sc.getConfidenceLevel());
            ps.setString(6, sc.getSourceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // MODULE 5: CONFIDENCE ASSESSMENT
    // =========================================================================

    public List<ConfidenceAssessment> getAllConfidenceAssessments() {
        List<ConfidenceAssessment> list = new ArrayList<>();
        String sql = "SELECT * FROM Confidence_Assessment ORDER BY assessed_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ConfidenceAssessment(
                        rs.getInt("assessment_id"),
                        rs.getString("observation_id"),
                        rs.getDouble("confidence_score"),
                        rs.getDouble("reliability_score"),
                        rs.getDouble("consistency_score"),
                        rs.getString("evidence_summary"),
                        rs.getString("recommended_decision"),
                        rs.getString("assessed_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addConfidenceAssessment(ConfidenceAssessment ca) {
        String sql = "INSERT INTO Confidence_Assessment (observation_id, confidence_score, reliability_score, consistency_score, evidence_summary, recommended_decision) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ca.getObservationId());
            ps.setDouble(2, ca.getConfidenceScore());
            ps.setDouble(3, ca.getReliabilityScore());
            ps.setDouble(4, ca.getConsistencyScore());
            ps.setString(5, ca.getEvidenceSummary());
            ps.setString(6, ca.getRecommendedDecision());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // =========================================================================
    // MODULE 6: ANOMALY DETECTION
    // =========================================================================

    public List<Anomaly> getAllAnomalies() {
        List<Anomaly> list = new ArrayList<>();
        String sql = "SELECT * FROM Anomalies ORDER BY detected_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Anomaly(
                        rs.getInt("anomaly_id"),
                        rs.getString("observation_id"),
                        rs.getString("source_id"),
                        rs.getString("anomaly_type"),
                        rs.getString("severity"),
                        rs.getDouble("detected_value"),
                        rs.getString("expected_range"),
                        rs.getString("description"),
                        rs.getString("detected_at"),
                        rs.getString("resolution_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addAnomaly(Anomaly a) {
        String sql = "INSERT INTO Anomalies (observation_id, source_id, anomaly_type, severity, detected_value, expected_range, description, resolution_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getObservationId());
            ps.setString(2, a.getSourceId());
            ps.setString(3, a.getAnomalyType());
            ps.setString(4, a.getSeverity());
            ps.setDouble(5, a.getDetectedValue());
            ps.setString(6, a.getExpectedRange());
            ps.setString(7, a.getDescription());
            ps.setString(8, a.getResolutionStatus() != null ? a.getResolutionStatus() : "OPEN");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean resolveAnomaly(int anomalyId) {
        String sql = "UPDATE Anomalies SET resolution_status = 'RESOLVED' WHERE anomaly_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, anomalyId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // MODULE 7: EVIDENCE MANAGEMENT
    // =========================================================================

    public List<Evidence> getAllEvidence() {
        List<Evidence> list = new ArrayList<>();
        String sql = "SELECT * FROM Evidence ORDER BY recorded_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Evidence(
                        rs.getInt("evidence_id"),
                        rs.getInt("assessment_id"),
                        rs.getString("supporting_observation"),
                        rs.getString("contradicting_observation"),
                        rs.getString("confidence_explanation"),
                        rs.getString("recommendation"),
                        rs.getString("recorded_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addEvidence(Evidence ev) {
        String sql = "INSERT INTO Evidence (assessment_id, supporting_observation, contradicting_observation, confidence_explanation, recommendation) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ev.getAssessmentId());
            ps.setString(2, ev.getSupportingObservation());
            ps.setString(3, ev.getContradictingObservation());
            ps.setString(4, ev.getConfidenceExplanation());
            ps.setString(5, ev.getRecommendation());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // REGIONS & ALERTS
    // =========================================================================

    public List<Region> getAllRegions() {
        List<Region> list = new ArrayList<>();
        String sql = "SELECT * FROM Regions ORDER BY region_id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Region(
                        rs.getString("region_id"),
                        rs.getString("region_name"),
                        rs.getDouble("center_lat"),
                        rs.getDouble("center_lon"),
                        rs.getString("terrain_type"),
                        rs.getString("threat_level"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Alert> getAllAlerts() {
        List<Alert> list = new ArrayList<>();
        String sql = "SELECT * FROM Alerts ORDER BY alert_id DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Alert(
                        rs.getInt("alert_id"),
                        rs.getString("alert_code"),
                        rs.getString("region_id"),
                        rs.getString("severity"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getInt("is_acknowledged") == 1,
                        rs.getString("issued_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean acknowledgeAlert(int alertId) {
        String sql = "UPDATE Alerts SET is_acknowledged = 1 WHERE alert_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // MODULE 8: DASHBOARD DATA & METRICS
    // =========================================================================

    public Map<String, String> getDashboardMetrics() {
        Map<String, String> metrics = new HashMap<>();
        String sql = "SELECT metric_id, metric_value FROM Dashboard_Data";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                metrics.put(rs.getString("metric_id"), rs.getString("metric_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metrics;
    }

    // =========================================================================
    // MODULE 9: INTELLIGENCE REPORTS & REPORT HISTORY
    // =========================================================================

    public List<IntelligenceReport> getAllIntelligenceReports() {
        List<IntelligenceReport> list = new ArrayList<>();
        String sql = "SELECT * FROM Intelligence_Reports ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new IntelligenceReport(
                        rs.getString("report_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("region_id"),
                        rs.getString("executive_summary"),
                        rs.getString("threat_assessment"),
                        rs.getDouble("confidence_rating"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addIntelligenceReport(IntelligenceReport r) {
        String sql = "INSERT INTO Intelligence_Reports (report_id, title, author, region_id, executive_summary, threat_assessment, confidence_rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getReportId());
            ps.setString(2, r.getTitle());
            ps.setString(3, r.getAuthor());
            ps.setString(4, r.getRegionId());
            ps.setString(5, r.getExecutiveSummary());
            ps.setString(6, r.getThreatAssessment());
            ps.setDouble(7, r.getConfidenceRating());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addReportHistory(String reportType, String generatedBy, String format, String path) {
        String sql = "INSERT INTO Report_History (report_type, generated_by, file_format, file_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reportType);
            ps.setString(2, generatedBy);
            ps.setString(3, format);
            ps.setString(4, path);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> getReportHistory() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM Report_History ORDER BY generated_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("history_id")));
                row.put("type", rs.getString("report_type"));
                row.put("author", rs.getString("generated_by"));
                row.put("format", rs.getString("file_format"));
                row.put("path", rs.getString("file_path"));
                row.put("time", rs.getString("generated_at"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // AUDIT LOGS, LOGIN LOGS & SYSTEM SETTINGS
    // =========================================================================

    public void logLogin(String username, String role, String status) {
        String sql = "INSERT INTO Login_Log (username, role, status) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, role);
            ps.setString(3, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logAudit(String userName, String action, String entityAffected, String details) {
        String sql = "INSERT INTO Audit_Log (user_name, action, entity_affected, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, action);
            ps.setString(3, entityAffected);
            ps.setString(4, details);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> getAuditLogs() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM Audit_Log ORDER BY timestamp DESC LIMIT 50";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", String.valueOf(rs.getInt("audit_id")));
                row.put("user", rs.getString("user_name"));
                row.put("action", rs.getString("action"));
                row.put("entity", rs.getString("entity_affected"));
                row.put("details", rs.getString("details"));
                row.put("time", rs.getString("timestamp"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, String> getSystemSettings() {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT setting_key, setting_value FROM System_Settings";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean updateSystemSetting(String key, String value) {
        String sql = "UPDATE System_Settings SET setting_value = ? WHERE setting_key = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
