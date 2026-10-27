package spaceintel.util;

import spaceintel.db.DatabaseManager;
import spaceintel.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ============================================================================
 * CLASS: ReportExporter.java
 * PURPOSE: Generates intelligence and audit reports in CSV, JSON, and TXT formats (Module 9).
 * EXPLANATION FOR BEGINNERS:
 * Real intelligence analysts need to export data for briefings and field units.
 * This class fetches records from the database and formats them cleanly into text files,
 * saving them to the "reports/" directory and logging the export in Report_History.
 * ============================================================================
 */
public class ReportExporter {

    private static final String REPORTS_DIR = "reports";

    static {
        File dir = new File(REPORTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Generates a specified report type in the chosen format.
     */
    public static File generateReport(String reportType, String format) throws Exception {
        DatabaseManager db = DatabaseManager.getInstance();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String cleanType = reportType.replaceAll("\\s+", "_");
        String filename = REPORTS_DIR + File.separator + cleanType + "_" + timestamp + "." + format.toLowerCase();
        File file = new File(filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            if ("csv".equalsIgnoreCase(format)) {
                writeCsvReport(writer, reportType, db);
            } else if ("json".equalsIgnoreCase(format)) {
                writeJsonReport(writer, reportType, db);
            } else {
                writeTxtReport(writer, reportType, db);
            }
        }

        // Record in Table 16: Report_History
        db.addReportHistory(reportType, SessionManager.getUsername(), format.toUpperCase(), file.getAbsolutePath());
        AuditService.log("REPORT_EXPORT", reportType, "Generated file: " + file.getName());

        return file;
    }

    private static void writeTxtReport(PrintWriter w, String reportType, DatabaseManager db) {
        w.println("================================================================================");
        w.println("ABC NATIONAL GEOSPATIAL INTELLIGENCE CENTRE - XOVIAN AEROSPACE");
        w.println("CLASSIFIED INTELLIGENCE REPORT: " + reportType.toUpperCase());
        w.println("Generated At: " + new Date());
        w.println("Analyst / Officer: " + SessionManager.getFullName() + " (" + SessionManager.getRole() + ")");
        w.println("================================================================================\n");

        if (reportType.contains("Observation")) {
            List<Observation> list = db.getAllObservations();
            w.println(String.format("%-15s | %-12s | %-18s | %-12s | %-12s | %-8s",
                    "OBS ID", "REGION", "OBSERVATION TYPE", "VALUE", "SOURCE", "STATUS"));
            w.println("--------------------------------------------------------------------------------");
            for (Observation o : list) {
                w.println(String.format("%-15s | %-12s | %-18s | %-12s | %-12s | %-8s",
                        o.getObservationId(), o.getRegionId(), o.getObservationType(),
                        o.getMeasuredValue() + " " + o.getUnit(), o.getSourceId(), o.getStatus()));
            }
        } else if (reportType.contains("Reliability") || reportType.contains("Source")) {
            List<SourceCredibility> list = db.getAllSourceCredibility();
            w.println(String.format("%-14s | %-12s | %-12s | %-12s | %-12s",
                    "SOURCE ID", "RELIABILITY", "ACCURACY", "FREQUENCY", "STATUS"));
            w.println("--------------------------------------------------------------------------------");
            for (SourceCredibility sc : list) {
                w.println(String.format("%-14s | %-11.1f%% | %-11.1f%% | %-12s | %-12s",
                        sc.getSourceId(), sc.getReliabilityScore(), sc.getHistoricalAccuracy(),
                        sc.getUpdateFrequency(), sc.getValidationStatus()));
            }
        } else if (reportType.contains("Anomaly")) {
            List<Anomaly> list = db.getAllAnomalies();
            w.println(String.format("%-8s | %-12s | %-24s | %-10s | %-10s",
                    "ID", "SOURCE", "ANOMALY TYPE", "SEVERITY", "STATUS"));
            w.println("--------------------------------------------------------------------------------");
            for (Anomaly a : list) {
                w.println(String.format("%-8d | %-12s | %-24s | %-10s | %-10s",
                        a.getAnomalyId(), a.getSourceId(), a.getAnomalyType(), a.getSeverity(), a.getResolutionStatus()));
                w.println("   Details: " + a.getDescription());
            }
        } else {
            // General Intelligence Summary
            List<IntelligenceReport> list = db.getAllIntelligenceReports();
            for (IntelligenceReport r : list) {
                w.println("REPORT ID: " + r.getReportId());
                w.println("TITLE:     " + r.getTitle());
                w.println("AUTHOR:    " + r.getAuthor() + " | THREAT LEVEL: " + r.getThreatAssessment());
                w.println("AI RATING: " + r.getConfidenceRating() + "%");
                w.println("SUMMARY:   " + r.getExecutiveSummary());
                w.println("--------------------------------------------------------------------------------");
            }
        }
        w.println("\n[END OF REPORT - OFFICIAL XEROSPACE INTELLIGENCE DOSSIER]");
    }

    private static void writeCsvReport(PrintWriter w, String reportType, DatabaseManager db) {
        if (reportType.contains("Observation")) {
            w.println("ObservationId,Timestamp,Latitude,Longitude,Type,Value,Unit,SourceId,RegionId,Status");
            for (Observation o : db.getAllObservations()) {
                w.println(String.format("%s,%s,%.4f,%.4f,\"%s\",%.2f,%s,%s,%s,%s",
                        o.getObservationId(), o.getTimestamp(), o.getLatitude(), o.getLongitude(),
                        o.getObservationType(), o.getMeasuredValue(), o.getUnit(), o.getSourceId(), o.getRegionId(), o.getStatus()));
            }
        } else if (reportType.contains("Reliability") || reportType.contains("Source")) {
            w.println("SourceId,ReliabilityScore,HistoricalAccuracy,UpdateFrequency,ValidationStatus,ConfidenceLevel");
            for (SourceCredibility sc : db.getAllSourceCredibility()) {
                w.println(String.format("%s,%.2f,%.2f,%s,%s,%s",
                        sc.getSourceId(), sc.getReliabilityScore(), sc.getHistoricalAccuracy(),
                        sc.getUpdateFrequency(), sc.getValidationStatus(), sc.getConfidenceLevel()));
            }
        } else {
            w.println("AnomalyId,ObservationId,SourceId,Type,Severity,DetectedValue,ResolutionStatus,Description");
            for (Anomaly a : db.getAllAnomalies()) {
                w.println(String.format("%d,%s,%s,\"%s\",%s,%.2f,%s,\"%s\"",
                        a.getAnomalyId(), a.getObservationId(), a.getSourceId(), a.getAnomalyType(),
                        a.getSeverity(), a.getDetectedValue(), a.getResolutionStatus(), a.getDescription()));
            }
        }
    }

    private static void writeJsonReport(PrintWriter w, String reportType, DatabaseManager db) {
        w.println("{");
        w.println("  \"reportTitle\": \"" + reportType + "\",");
        w.println("  \"generatedBy\": \"" + SessionManager.getUsername() + "\",");
        w.println("  \"timestamp\": \"" + new Date() + "\",");
        w.println("  \"records\": [");

        List<Observation> list = db.getAllObservations();
        for (int i = 0; i < list.size(); i++) {
            Observation o = list.get(i);
            w.println("    {");
            w.println("      \"observationId\": \"" + o.getObservationId() + "\",");
            w.println("      \"type\": \"" + o.getObservationType() + "\",");
            w.println("      \"value\": " + o.getMeasuredValue() + ",");
            w.println("      \"unit\": \"" + o.getUnit() + "\",");
            w.println("      \"source\": \"" + o.getSourceId() + "\",");
            w.println("      \"region\": \"" + o.getRegionId() + "\",");
            w.println("      \"status\": \"" + o.getStatus() + "\"");
            w.print("    }");
            if (i < list.size() - 1) {
                w.println(",");
            } else {
                w.println();
            }
        }
        w.println("  ]");
        w.println("}");
    }
}
