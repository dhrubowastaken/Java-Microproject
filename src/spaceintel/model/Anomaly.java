package spaceintel.model;

/**
 * ============================================================================
 * CLASS: Anomaly.java
 * PURPOSE: Stores detected geospatial defects and telemetry issues (Module 6).
 * EXPLANATION FOR BEGINNERS:
 * Space data pipelines constantly suffer from 5 major anomaly types:
 * 1. Missing Data (packets lost during satellite downlink)
 * 2. Duplicate Observations (same frame ingested twice)
 * 3. Conflicting Measurements (two sensors at the same spot read very different values)
 * 4. Outlier Values (values way outside physically possible limits)
 * 5. Delayed Updates (a sensor stopped transmitting)
 * ============================================================================
 */
public class Anomaly {

    private int anomalyId;
    private String observationId;
    private String sourceId;
    private String anomalyType;     // Missing Data, Duplicate Observations, Conflicting Measurements, Outlier Values, Delayed Updates
    private String severity;        // LOW, MEDIUM, HIGH, CRITICAL
    private double detectedValue;
    private String expectedRange;
    private String description;
    private String detectedAt;
    private String resolutionStatus; // OPEN, INVESTIGATING, RESOLVED

    public Anomaly() {
    }

    public Anomaly(int anomalyId, String observationId, String sourceId, String anomalyType, 
                   String severity, double detectedValue, String expectedRange, 
                   String description, String detectedAt, String resolutionStatus) {
        this.anomalyId = anomalyId;
        this.observationId = observationId;
        this.sourceId = sourceId;
        this.anomalyType = anomalyType;
        this.severity = severity;
        this.detectedValue = detectedValue;
        this.expectedRange = expectedRange;
        this.description = description;
        this.detectedAt = detectedAt;
        this.resolutionStatus = resolutionStatus;
    }

    public int getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(int anomalyId) {
        this.anomalyId = anomalyId;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(String anomalyType) {
        this.anomalyType = anomalyType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public double getDetectedValue() {
        return detectedValue;
    }

    public void setDetectedValue(double detectedValue) {
        this.detectedValue = detectedValue;
    }

    public String getExpectedRange() {
        return expectedRange;
    }

    public void setExpectedRange(String expectedRange) {
        this.expectedRange = expectedRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(String detectedAt) {
        this.detectedAt = detectedAt;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + anomalyType + " (" + sourceId + "): " + description;
    }
}
