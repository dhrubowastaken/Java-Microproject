package spaceintel.util;

import spaceintel.db.DatabaseManager;

/**
 * ============================================================================
 * CLASS: AuditService.java
 * PURPOSE: Records security and operational events into Table 14 (Audit_Log).
 * EXPLANATION FOR BEGINNERS:
 * For government and defense compliance, every critical analyst action
 * (e.g. Ingesting observation, running anomaly scan, publishing intelligence report)
 * must be permanently timestamped with the user's name.
 * ============================================================================
 */
public class AuditService {

    public static void log(String action, String entityAffected, String details) {
        String username = SessionManager.getUsername();
        DatabaseManager.getInstance().logAudit(username, action, entityAffected, details);
        System.out.println("[AUDIT LOG] " + username + " -> " + action + " on " + entityAffected + " (" + details + ")");
    }
}
