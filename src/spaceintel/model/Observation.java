package spaceintel.model;

/**
 * ============================================================================
 * CLASS: Observation.java
 * PURPOSE: Holds a single geospatial observation or telemetry record (Module 3).
 * EXPLANATION FOR BEGINNERS:
 * When a satellite or drone scans a location, it captures:
 * 1. Coordinates (latitude and longitude)
 * 2. The type of measurement (Thermal Anomaly, NDVI vegetation, Atmospheric Pressure, etc.)
 * 3. The measured numeric value (e.g., 64.8 °C or 972 hPa)
 * 4. The source ID and Region ID
 * 5. Current status (RAW, VERIFIED, CONFLICTING, ANOMALOUS)
 * ============================================================================
 */
public class Observation {

    private String observationId;
    private String timestamp;
    private double latitude;
    private double longitude;
    private String observationType;
    private double measuredValue;
    private String unit;
    private String sourceId;
    private String regionId;
    private String status;

    public Observation() {
    }

    public Observation(String observationId, String timestamp, double latitude, double longitude, 
                       String observationType, double measuredValue, String unit, 
                       String sourceId, String regionId, String status) {
        this.observationId = observationId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.observationType = observationType;
        this.measuredValue = measuredValue;
        this.unit = unit;
        this.sourceId = sourceId;
        this.regionId = regionId;
        this.status = status;
    }

    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getObservationType() {
        return observationType;
    }

    public void setObservationType(String observationType) {
        this.observationType = observationType;
    }

    public double getMeasuredValue() {
        return measuredValue;
    }

    public void setMeasuredValue(double measuredValue) {
        this.measuredValue = measuredValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return observationId + ": " + observationType + " (" + measuredValue + " " + unit + ") from " + sourceId;
    }
}
