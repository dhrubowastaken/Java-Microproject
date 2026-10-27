package spaceintel.model;

/**
 * ============================================================================
 * CLASS: DataSource.java
 * PURPOSE: Represents an observation platform in the system (Module 2).
 * EXPLANATION FOR BEGINNERS:
 * Space Intelligence gathers data from multiple types of hardware:
 * Satellites, Drones, Weather Stations, GPS Stations, GIS databases, and Ground Sensors.
 * This class stores the platform ID, type, owning organization, coverage area, and status.
 * ============================================================================
 */
public class DataSource {

    // --- Attributes ---
    private String sourceId;      // e.g., "SAT-CARTO3"
    private String sourceName;    // e.g., "Cartosat-3 Optical Constellation"
    private String sourceType;    // Satellite, Drone, GIS, Weather Station, GPS, Sensor
    private String organization;  // ISRO, ESA, NASA, IMD, Xovian Aerospace
    private String coverageArea;  // e.g., "Pan-India High Res"
    private String status;        // ACTIVE, DEGRADED, CALIBRATING, OFFLINE

    // Default Constructor
    public DataSource() {
    }

    // Parameterized Constructor
    public DataSource(String sourceId, String sourceName, String sourceType, String organization, String coverageArea, String status) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceType = sourceType;
        this.organization = organization;
        this.coverageArea = coverageArea;
        this.status = status;
    }

    // --- Getters & Setters ---
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(String coverageArea) {
        this.coverageArea = coverageArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return sourceId + " - " + sourceName + " (" + sourceType + ")";
    }
}
