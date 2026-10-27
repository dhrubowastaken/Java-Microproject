package spaceintel.engine;

import spaceintel.db.DatabaseManager;
import spaceintel.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CLASS: AnomalyEngine.java
 * PURPOSE: Automated Geospatial Anomaly Detection Engine (Module 6).
 * EXPLANATION FOR BEGINNERS:
 * Space data pipelines receive high volumes of telemetry. This engine scans
 * data to detect the 5 mandatory anomaly types:
 * 
 * 1. OUTLIER VALUES: Telemetry values physically impossible or statistically extreme
 *    (e.g., a Himalayan glacier station reporting +25°C in mid-winter).
 * 2. DUPLICATE OBSERVATIONS: The same satellite packet ingested multiple times
 *    within seconds.
 * 3. CONFLICTING MEASUREMENTS: Two sensors in the same region reporting wildly
 *    contradictory readings (>40% variance).
 * 4. DELAYED UPDATES: Sources that have stopped sending heartbeats or telemetry.
 * 5. MISSING DATA: Observations with empty or corrupt coordinate fields.
 * ============================================================================
 */
public class AnomalyEngine {

    private final DatabaseManager db;

    public AnomalyEngine() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Executes a full automated scan across all observations and sources,
     * discovering anomalies and recording them into the Anomalies table.
     */
    public List<Anomaly> runFullAnomalyScan() {
        List<Anomaly> discovered = new ArrayList<>();
        List<Observation> observations = db.getAllObservations();
        List<DataSource> sources = db.getAllDataSources();

        // -------------------------------------------------------------
        // SCAN 1: Outlier Values (Physical Limit Violations)
        // -------------------------------------------------------------
        for (Observation obs : observations) {
            String type = obs.getObservationType();
            double val = obs.getMeasuredValue();

            // Glacial Temperature Check (Should be <= 10°C in high altitude)
            if (type.toLowerCase().contains("glacial") && val > 15.0) {
                Anomaly a = new Anomaly(0, obs.getObservationId(), obs.getSourceId(),
                        "Outlier Values", "CRITICAL", val, "-50.0 to 10.0 °C",
                        "High-altitude glaciological telemetry reads " + val + " °C, which exceeds freezing thresholds by 25°C+.",
                        null, "OPEN");
                discovered.add(a);
            }
            // Thermal Wildfire limits
            else if (type.toLowerCase().contains("thermal") && (val < -10.0 || val > 150.0)) {
                Anomaly a = new Anomaly(0, obs.getObservationId(), obs.getSourceId(),
                        "Outlier Values", "HIGH", val, "-10.0 to 150.0 °C",
                        "Thermal sensor reading (" + val + " °C) is outside normal environmental infrared bounds.",
                        null, "OPEN");
                discovered.add(a);
            }
            // Atmospheric Pressure (Standard earth surface range: 870 to 1084 hPa)
            else if (type.toLowerCase().contains("pressure") && (val < 800.0 || val > 1100.0)) {
                Anomaly a = new Anomaly(0, obs.getObservationId(), obs.getSourceId(),
                        "Outlier Values", "CRITICAL", val, "870 to 1085 hPa",
                        "Atmospheric barometric pressure reading (" + val + " hPa) is physically impossible.",
                        null, "OPEN");
                discovered.add(a);
            }
            // NDVI Vegetation Index must be between -1.0 and +1.0
            else if (type.toLowerCase().contains("ndvi") && (val < -1.0 || val > 1.0)) {
                Anomaly a = new Anomaly(0, obs.getObservationId(), obs.getSourceId(),
                        "Outlier Values", "HIGH", val, "-1.0 to 1.0",
                        "NDVI satellite reflectance index (" + val + ") violates mathematical bounds.",
                        null, "OPEN");
                discovered.add(a);
            }
        }

        // -------------------------------------------------------------
        // SCAN 2: Duplicate Observations
        // -------------------------------------------------------------
        for (int i = 0; i < observations.size(); i++) {
            Observation a = observations.get(i);
            for (int j = i + 1; j < observations.size(); j++) {
                Observation b = observations.get(j);
                if (a.getSourceId().equalsIgnoreCase(b.getSourceId())
                        && Math.abs(a.getLatitude() - b.getLatitude()) < 0.0001
                        && Math.abs(a.getLongitude() - b.getLongitude()) < 0.0001
                        && Math.abs(a.getMeasuredValue() - b.getMeasuredValue()) < 0.001) {
                    Anomaly anom = new Anomaly(0, b.getObservationId(), b.getSourceId(),
                            "Duplicate Observations", "MEDIUM", b.getMeasuredValue(), "Unique per time-window",
                            "Duplicate telemetry packet ingested from " + b.getSourceId() + " matching " + a.getObservationId(),
                            null, "OPEN");
                    discovered.add(anom);
                }
            }
        }

        // -------------------------------------------------------------
        // SCAN 3: Conflicting Measurements
        // -------------------------------------------------------------
        for (int i = 0; i < observations.size(); i++) {
            Observation a = observations.get(i);
            for (int j = i + 1; j < observations.size(); j++) {
                Observation b = observations.get(j);
                if (!a.getSourceId().equalsIgnoreCase(b.getSourceId())
                        && a.getRegionId().equalsIgnoreCase(b.getRegionId())
                        && a.getObservationType().equalsIgnoreCase(b.getObservationType())) {

                    double diff = Math.abs(a.getMeasuredValue() - b.getMeasuredValue());
                    double maxVal = Math.max(1.0, Math.max(Math.abs(a.getMeasuredValue()), Math.abs(b.getMeasuredValue())));
                    if ((diff / maxVal) > 0.40) { // More than 40% variance
                        Anomaly anom = new Anomaly(0, b.getObservationId(), b.getSourceId(),
                                "Conflicting Measurements", "HIGH", b.getMeasuredValue(),
                                "Variance <= 20%",
                                "Telemetry conflict: " + a.getSourceId() + " reports " + a.getMeasuredValue() +
                                        " but " + b.getSourceId() + " reports " + b.getMeasuredValue() +
                                        " in region " + a.getRegionId(),
                                null, "OPEN");
                        discovered.add(anom);
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // SCAN 4: Delayed Updates / Stale Hardware
        // -------------------------------------------------------------
        for (DataSource ds : sources) {
            if ("DEGRADED".equalsIgnoreCase(ds.getStatus()) || "OFFLINE".equalsIgnoreCase(ds.getStatus())) {
                Anomaly anom = new Anomaly(0, null, ds.getSourceId(),
                        "Delayed Updates", "MEDIUM", 0.0, "Healthy Signal",
                        "Source " + ds.getSourceName() + " flagged as " + ds.getStatus() + "; telemetry downlink interrupted.",
                        null, "OPEN");
                discovered.add(anom);
            }
        }

        // -------------------------------------------------------------
        // SCAN 5: Missing Data
        // -------------------------------------------------------------
        for (Observation obs : observations) {
            if (obs.getLatitude() == 0.0 && obs.getLongitude() == 0.0) {
                Anomaly anom = new Anomaly(0, obs.getObservationId(), obs.getSourceId(),
                        "Missing Data", "HIGH", 0.0, "Valid Geo-Coordinates",
                        "Observation " + obs.getObservationId() + " missing latitude/longitude coordinates (Null Island coordinates).",
                        null, "OPEN");
                discovered.add(anom);
            }
        }

        return discovered;
    }

    /**
     * Executes the scan and saves newly found anomalies to the database.
     */
    public int scanAndPersist() {
        List<Anomaly> list = runFullAnomalyScan();
        int added = 0;
        for (Anomaly a : list) {
            if (db.addAnomaly(a)) {
                added++;
            }
        }
        return added;
    }
}
