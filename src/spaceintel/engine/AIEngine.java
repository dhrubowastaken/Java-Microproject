package spaceintel.engine;

import spaceintel.db.DatabaseManager;
import spaceintel.model.*;

import java.util.List;

/**
 * ============================================================================
 * CLASS: AIEngine.java
 * PURPOSE: Rule-Based Explainable AI Assessment Engine (Module 5).
 * EXPLANATION FOR BEGINNERS:
 * In a mission-critical space system, an AI model shouldn't just be a "black box".
 * It must explain WHY a confidence percentage was given.
 * 
 * OUR MATHEMATICAL FORMULA:
 * 1. Reliability Score (R): How reliable is this satellite/drone historically?
 *    (Extracted from Table 5: Source_Credibility)
 * 2. Consistency Score (C): Do other sensors in this region agree with this number?
 *    (Calculated by checking variance with nearby observations in Table 3)
 * 3. Overall Confidence Score:
 *    Confidence = (0.5 * Reliability) + (0.5 * Consistency)
 * 
 * 4. Transparent Recommendation:
 *    - >= 85.0% : PROCEED (Multi-source verification confirmed)
 *    - >= 60.0% : CORROBORATE (Requires secondary satellite or drone pass)
 *    - <  60.0% : REJECT (Telemetry drift or physical anomaly detected)
 * ============================================================================
 */
public class AIEngine {

    private final DatabaseManager db;

    public AIEngine() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Evaluates an observation and generates a complete Confidence Assessment.
     */
    public ConfidenceAssessment evaluateObservation(Observation obs) {
        // Step 1: Look up hardware reliability score for this source
        double reliability = 80.0; // Default baseline
        List<SourceCredibility> credList = db.getAllSourceCredibility();
        for (SourceCredibility sc : credList) {
            if (sc.getSourceId().equalsIgnoreCase(obs.getSourceId())) {
                reliability = sc.getReliabilityScore();
                break;
            }
        }

        // Step 2: Compare against other sensors in the same region (Consistency Check)
        double consistency = 85.0; // Default if first sensor in region
        String matchedSource = null;
        double matchedValue = 0.0;

        List<Observation> allObs = db.getAllObservations();
        for (Observation other : allObs) {
            // Find another observation in the same region with the same observation type
            if (!other.getObservationId().equals(obs.getObservationId())
                    && other.getRegionId().equalsIgnoreCase(obs.getRegionId())
                    && other.getObservationType().equalsIgnoreCase(obs.getObservationType())) {

                matchedSource = other.getSourceId();
                matchedValue = other.getMeasuredValue();

                // Compute absolute percentage difference
                double diff = Math.abs(obs.getMeasuredValue() - other.getMeasuredValue());
                double base = Math.max(1.0, Math.abs(obs.getMeasuredValue()));
                double percentDiff = (diff / base) * 100.0;

                consistency = Math.max(10.0, Math.min(100.0, 100.0 - percentDiff));
                break;
            }
        }

        // Step 3: Compute weighted final confidence score
        double confidence = (0.50 * reliability) + (0.50 * consistency);
        // Round to 1 decimal place
        confidence = Math.round(confidence * 10.0) / 10.0;

        // Step 4: Formulate explainable recommendation & evidence summary
        String decision;
        String evidenceSummary;

        if (confidence >= 85.0) {
            decision = "PROCEED";
            if (matchedSource != null) {
                evidenceSummary = "High confidence: Observation corroborated by " + matchedSource + " (" + matchedValue + " " + obs.getUnit() + "). Multi-source agreement confirmed.";
            } else {
                evidenceSummary = "High confidence: Source hardware certified with " + reliability + "% historical uptime.";
            }
        } else if (confidence >= 60.0) {
            decision = "CORROBORATE";
            evidenceSummary = "Moderate confidence: Telemetry shows acceptable variance. Recommend secondary drone or satellite sweep.";
        } else {
            decision = "REJECT";
            if (matchedSource != null) {
                evidenceSummary = "Telemetry conflict: Value deviates severely from " + matchedSource + " (" + matchedValue + " " + obs.getUnit() + "). Possible hardware glitch.";
            } else {
                evidenceSummary = "Low reliability source rating (" + reliability + "%). Telemetry unverified by secondary assets.";
            }
        }

        ConfidenceAssessment assessment = new ConfidenceAssessment();
        assessment.setObservationId(obs.getObservationId());
        assessment.setConfidenceScore(confidence);
        assessment.setReliabilityScore(reliability);
        assessment.setConsistencyScore(Math.round(consistency * 10.0) / 10.0);
        assessment.setEvidenceSummary(evidenceSummary);
        assessment.setRecommendedDecision(decision);

        return assessment;
    }

    /**
     * Re-evaluates all raw observations and persists results into database.
     */
    public int assessAllRawObservations() {
        int count = 0;
        List<Observation> observations = db.getAllObservations();
        for (Observation obs : observations) {
            ConfidenceAssessment ca = evaluateObservation(obs);
            int newId = db.addConfidenceAssessment(ca);
            if (newId > 0) {
                // Also create an Evidence record (Module 7)
                Evidence ev = new Evidence();
                ev.setAssessmentId(newId);
                ev.setSupportingObservation("Telemetry ingest from " + obs.getSourceId() + " at lat=" + obs.getLatitude() + ", lon=" + obs.getLongitude());
                ev.setContradictingObservation(ca.getConsistencyScore() < 60.0 ? "Conflicting regional sensors detected" : "None");
                ev.setConfidenceExplanation(ca.getEvidenceSummary());
                ev.setRecommendation(ca.getRecommendedDecision() + " operational deployment based on confidence rating " + ca.getConfidenceScore() + "%");
                db.addEvidence(ev);
                count++;
            }
        }
        return count;
    }
}
