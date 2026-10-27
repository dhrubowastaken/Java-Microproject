package spaceintel.model;

/**
 * ============================================================================
 * CLASS: IntelligenceReport.java
 * PURPOSE: Represents an executive intelligence dossier (Module 9 & Reports).
 * EXPLANATION FOR BEGINNERS:
 * Command personnel read synthesized summaries instead of thousands of raw sensor logs.
 * A report contains a title, author, region, executive summary, threat assessment level,
 * and overall confidence rating.
 * ============================================================================
 */
public class IntelligenceReport {

    private String reportId;
    private String title;
    private String author;
    private String regionId;
    private String executiveSummary;
    private String threatAssessment; // STABLE, WATCH, WARNING, CRITICAL
    private double confidenceRating;
    private String createdAt;

    public IntelligenceReport() {
    }

    public IntelligenceReport(String reportId, String title, String author, String regionId, 
                              String executiveSummary, String threatAssessment, 
                              double confidenceRating, String createdAt) {
        this.reportId = reportId;
        this.title = title;
        this.author = author;
        this.regionId = regionId;
        this.executiveSummary = executiveSummary;
        this.threatAssessment = threatAssessment;
        this.confidenceRating = confidenceRating;
        this.createdAt = createdAt;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(String executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public String getThreatAssessment() {
        return threatAssessment;
    }

    public void setThreatAssessment(String threatAssessment) {
        this.threatAssessment = threatAssessment;
    }

    public double getConfidenceRating() {
        return confidenceRating;
    }

    public void setConfidenceRating(double confidenceRating) {
        this.confidenceRating = confidenceRating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return reportId + " - " + title + " (" + threatAssessment + ")";
    }
}
