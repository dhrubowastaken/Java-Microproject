package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.Alert;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 12: AlertsView.java
 * PURPOSE: Operational Emergency Alerts & Incident Dispatch Center (Screen 12).
 * EXPLANATION FOR BEGINNERS:
 * When dangerous natural disasters (cyclones, wildfires) or space sensor telemetry
 * breaks are identified, the system broadcasts alerts to field response units.
 * Officers can filter alerts by severity (EMERGENCY, WARNING, ADVISORY, INFO)
 * and acknowledge notifications to record that action was initiated.
 * ============================================================================
 */
public class AlertsView extends JPanel {

    private DefaultTableModel alertTableModel;
    private JTable alertTable;
    private JComboBox<String> severityFilter;

    public AlertsView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "System Alerts",
                "View active warnings triggered by critical sensor readings and acknowledge resolved incidents."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Top Filter & Action Bar
        JPanel topBar = UITheme.createCard();
        topBar.setLayout(new BorderLayout());

        JPanel leftFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftFilters.setBackground(UITheme.CARD_BG);

        JLabel filterLbl = new JLabel("Filter by Severity:");
        filterLbl.setFont(UITheme.FONT_BOLD);
        filterLbl.setForeground(UITheme.TEXT_MUTED);

        severityFilter = new JComboBox<>(new String[]{"All Severities", "EMERGENCY", "WARNING", "ADVISORY", "INFO"});
        UITheme.styleComboBox(severityFilter);
        severityFilter.addActionListener(e -> loadAlerts());

        leftFilters.add(filterLbl);
        leftFilters.add(severityFilter);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setBackground(UITheme.CARD_BG);

        JButton ackBtn = UITheme.createPrimaryButton("Acknowledge Alert");
        ackBtn.setBackground(UITheme.GREEN_SUCCESS);
        ackBtn.addActionListener(e -> acknowledgeSelected());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadAlerts());

        rightActions.add(ackBtn);
        rightActions.add(refreshBtn);

        topBar.add(leftFilters, BorderLayout.WEST);
        topBar.add(rightActions, BorderLayout.EAST);
        content.add(topBar, BorderLayout.NORTH);

        // Table Card
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        String[] cols = {"Alert ID", "Code", "Region", "Severity", "Alert Title", "Operational Message", "Status", "Issued At"};
        alertTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        alertTable = new JTable(alertTableModel);
        UITheme.styleTable(alertTable);

        JScrollPane scroll = new JScrollPane(alertTable);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(scroll, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadAlerts();
    }

    private void acknowledgeSelected() {
        int row = alertTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an alert from the table to acknowledge.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int alertId = (int) alertTableModel.getValueAt(row, 0);
        boolean ok = DatabaseManager.getInstance().acknowledgeAlert(alertId);
        if (ok) {
            AuditService.log("ALERT_ACKNOWLEDGED", "Alerts", "Acknowledged Alert #" + alertId);
            JOptionPane.showMessageDialog(this, "Alert #" + alertId + " acknowledged.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAlerts();
        }
    }

    public void loadAlerts() {
        alertTableModel.setRowCount(0);
        String selected = (String) severityFilter.getSelectedItem();
        List<Alert> list = DatabaseManager.getInstance().getAllAlerts();

        for (Alert a : list) {
            boolean matches = "All Severities".equals(selected) || a.getSeverity().equalsIgnoreCase(selected);
            if (matches) {
                alertTableModel.addRow(new Object[]{
                        a.getAlertId(),
                        a.getAlertCode(),
                        a.getRegionId(),
                        a.getSeverity(),
                        a.getTitle(),
                        a.getMessage(),
                        a.isAcknowledged() ? "ACKNOWLEDGED" : "UNREAD ACTION REQ",
                        a.getIssuedAt()
                });
            }
        }
    }
}
