package spaceintel.model;

/**
 * ============================================================================
 * CLASS: Region.java
 * PURPOSE: Represents a monitored geographic territory (Module 3 & Regional Monitoring).
 * EXPLANATION FOR BEGINNERS:
 * Observations belong to a geographical region (e.g. Dehradun Forest, Bay of Bengal).
 * We track central coordinates (latitude, longitude), terrain type, and current threat level.
 * ============================================================================
 */
public class Region {

    private String regionId;
    private String regionName;
    private double centerLat;
    private double centerLon;
    private String terrainType; // Forest, Coastal, Desert, Mountain, Urban
    private String threatLevel; // NORMAL, ELEVATED, CRITICAL
    private String description;

    public Region() {
    }

    public Region(String regionId, String regionName, double centerLat, double centerLon, String terrainType, String threatLevel, String description) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.terrainType = terrainType;
        this.threatLevel = threatLevel;
        this.description = description;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(double centerLon) {
        this.centerLon = centerLon;
    }

    public String getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }

    public String getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return regionName + " [" + regionId + "]";
    }
}
