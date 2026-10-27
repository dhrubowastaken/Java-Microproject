package spaceintel.model;

/**
 * ============================================================================
 * CLASS: SourceCredibility.java
 * PURPOSE: Manages the trust and historical accuracy of a data source (Module 4).
 * EXPLANATION FOR BEGINNERS:
 * Not every satellite or sensor is equally reliable. A military optical satellite
 * or calibrated geodetic GPS has 98%+ reliability, while an unmaintained mountain
 * sensor might drift down to 68%. This class stores these trust scores.
 * ============================================================================
 */
public class SourceCredibility {

    private int credibilityId;
    private String sourceId;
    private double reliabilityScore;    // 0.0 to 100.0%
    private double historicalAccuracy;   // 0.0 to 100.0%
    private String updateFrequency;     // Real-time, 15 min, 1 Hour, Daily
    private String validationStatus;    // CERTIFIED, PROVISIONAL, UNDER_REVIEW
    private String confidenceLevel;     // HIGH, MEDIUM, LOW
    private String lastEvaluated;

    public SourceCredibility() {
    }

    public SourceCredibility(int credibilityId, String sourceId, double reliabilityScore, 
                             double historicalAccuracy, String updateFrequency, 
                             String validationStatus, String confidenceLevel, String lastEvaluated) {
        this.credibilityId = credibilityId;
        this.sourceId = sourceId;
        this.reliabilityScore = reliabilityScore;
        this.historicalAccuracy = historicalAccuracy;
        this.updateFrequency = updateFrequency;
        this.validationStatus = validationStatus;
        this.confidenceLevel = confidenceLevel;
        this.lastEvaluated = lastEvaluated;
    }

    public int getCredibilityId() {
        return credibilityId;
    }

    public void setCredibilityId(int credibilityId) {
        this.credibilityId = credibilityId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public double getReliabilityScore() {
        return reliabilityScore;
    }

    public void setReliabilityScore(double reliabilityScore) {
        this.reliabilityScore = reliabilityScore;
    }

    public double getHistoricalAccuracy() {
        return historicalAccuracy;
    }

    public void setHistoricalAccuracy(double historicalAccuracy) {
        this.historicalAccuracy = historicalAccuracy;
    }

    public String getUpdateFrequency() {
        return updateFrequency;
    }

    public void setUpdateFrequency(String updateFrequency) {
        this.updateFrequency = updateFrequency;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getLastEvaluated() {
        return lastEvaluated;
    }

    public void setLastEvaluated(String lastEvaluated) {
        this.lastEvaluated = lastEvaluated;
    }

    @Override
    public String toString() {
        return sourceId + " [Score: " + reliabilityScore + "%, Status: " + validationStatus + "]";
    }
}
