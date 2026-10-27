package spaceintel.model;

/**
 * ============================================================================
 * CLASS: ConfidenceAssessment.java
 * PURPOSE: Holds AI-calculated confidence outputs for an observation (Module 5).
 * EXPLANATION FOR BEGINNERS:
 * Rather than just trusting raw sensor numbers, our AI engine computes:
 * 1. Reliability Score (how trustworthy is this sensor hardware?)
 * 2. Consistency Score (do other nearby sensors agree with it?)
 * 3. Confidence Score (weighted blend of reliability + consistency)
 * 4. Evidence Summary (human-readable justification for the score)
 * 5. Recommended Decision (PROCEED, CORROBORATE, REJECT, ESCALATE)
 * ============================================================================
 */
public class ConfidenceAssessment {

    private int assessmentId;
    private String observationId;
    private double confidenceScore;
    private double reliabilityScore;
    private double consistencyScore;
    private String evidenceSummary;
    private String recommendedDecision; // PROCEED, CORROBORATE, REJECT, ESCALATE
    private String assessedAt;

    public ConfidenceAssessment() {
    }

    public ConfidenceAssessment(int assessmentId, String observationId, double confidenceScore, 
                                double reliabilityScore, double consistencyScore, 
                                String evidenceSummary, String recommendedDecision, String assessedAt) {
        this.assessmentId = assessmentId;
        this.observationId = observationId;
        this.confidenceScore = confidenceScore;
        this.reliabilityScore = reliabilityScore;
        this.consistencyScore = consistencyScore;
        this.evidenceSummary = evidenceSummary;
        this.recommendedDecision = recommendedDecision;
        this.assessedAt = assessedAt;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public double getReliabilityScore() {
        return reliabilityScore;
    }

    public void setReliabilityScore(double reliabilityScore) {
        this.reliabilityScore = reliabilityScore;
    }

    public double getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public String getEvidenceSummary() {
        return evidenceSummary;
    }

    public void setEvidenceSummary(String evidenceSummary) {
        this.evidenceSummary = evidenceSummary;
    }

    public String getRecommendedDecision() {
        return recommendedDecision;
    }

    public void setRecommendedDecision(String recommendedDecision) {
        this.recommendedDecision = recommendedDecision;
    }

    public String getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(String assessedAt) {
        this.assessedAt = assessedAt;
    }

    @Override
    public String toString() {
        return "Assessment for " + observationId + ": " + confidenceScore + "% -> " + recommendedDecision;
    }
}
