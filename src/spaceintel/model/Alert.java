package spaceintel.model;

/**
 * ============================================================================
 * CLASS: Alert.java
 * PURPOSE: Represents an operational alarm or disaster advisory (Screen 12 & Alerts).
 * EXPLANATION FOR BEGINNERS:
 * When anomalies or critical geospatial events occur (wildfires, cyclones, sensor failures),
 * the system dispatches an alert. Analysts can view, filter, and acknowledge these alerts.
 * ============================================================================
 */
public class Alert {

    private int alertId;
    private String alertCode;
    private String regionId;
    private String severity;       // INFO, ADVISORY, WARNING, EMERGENCY
    private String title;
    private String message;
    private boolean isAcknowledged; // false = unread, true = acknowledged
    private String issuedAt;

    public Alert() {
    }

    public Alert(int alertId, String alertCode, String regionId, String severity, 
                 String title, String message, boolean isAcknowledged, String issuedAt) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.regionId = regionId;
        this.severity = severity;
        this.title = title;
        this.message = message;
        this.isAcknowledged = isAcknowledged;
        this.issuedAt = issuedAt;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(String alertCode) {
        this.alertCode = alertCode;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAcknowledged() {
        return isAcknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        isAcknowledged = acknowledged;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + title;
    }
}
