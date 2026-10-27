package spaceintel.model;

/**
 * ============================================================================
 * CLASS: Evidence.java
 * PURPOSE: Holds supporting and contradicting facts backing an AI decision (Module 7).
 * EXPLANATION FOR BEGINNERS:
 * Explainable AI (XAI) requires transparency. When the AI tells a commander:
 * "Issue a Cyclone Warning" or "Reject this sensor temperature", it must present:
 * 1. Supporting Observation (e.g. Radar agrees with satellite pressure drop)
 * 2. Contradicting Observation (e.g. Ground station doesn't see what the UAV saw)
 * 3. Confidence Explanation (plain English why we trust it)
 * 4. Recommended Operational Action
 * ============================================================================
 */
public class Evidence {

    private int evidenceId;
    private int assessmentId;
    private String supportingObservation;
    private String contradictingObservation;
    private String confidenceExplanation;
    private String recommendation;
    private String recordedAt;

    public Evidence() {
    }

    public Evidence(int evidenceId, int assessmentId, String supportingObservation, 
                    String contradictingObservation, String confidenceExplanation, 
                    String recommendation, String recordedAt) {
        this.evidenceId = evidenceId;
        this.assessmentId = assessmentId;
        this.supportingObservation = supportingObservation;
        this.contradictingObservation = contradictingObservation;
        this.confidenceExplanation = confidenceExplanation;
        this.recommendation = recommendation;
        this.recordedAt = recordedAt;
    }

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getSupportingObservation() {
        return supportingObservation;
    }

    public void setSupportingObservation(String supportingObservation) {
        this.supportingObservation = supportingObservation;
    }

    public String getContradictingObservation() {
        return contradictingObservation;
    }

    public void setContradictingObservation(String contradictingObservation) {
        this.contradictingObservation = contradictingObservation;
    }

    public String getConfidenceExplanation() {
        return confidenceExplanation;
    }

    public void setConfidenceExplanation(String confidenceExplanation) {
        this.confidenceExplanation = confidenceExplanation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "Evidence #" + evidenceId + " (Assessment " + assessmentId + "): " + recommendation;
    }
}
