package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.engine.AnomalyEngine;
import spaceintel.model.Anomaly;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 8: AnomalyDashboardView.java
 * PURPOSE: Automated Geospatial Anomaly Detection & Incident Resolution (Module 6).
 * EXPLANATION FOR BEGINNERS:
 * Space data contains dirty data, noise, and transmission errors.
 * This screen displays all 5 mandatory defect types:
 * 1. Missing Data
 * 2. Duplicate Observations
 * 3. Conflicting Measurements
 * 4. Outlier Values
 * 5. Delayed Updates
 * 
 * Analysts can click "Run Live Anomaly Scanner" to automatically discover
 * new faults in the database and click "Resolve Anomaly" to fix them!
 * ============================================================================
 */
public class AnomalyDashboardView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> filterCombo;
    private JLabel summaryLabel;

    public AnomalyDashboardView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Anomaly Scanner",
                "Detect and resolve bad data like sensor outliers, duplicates, conflicting readings, and missing values."
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

        JLabel filterLbl = new JLabel("Filter by Error Type:");
        filterLbl.setFont(UITheme.FONT_BOLD);
        filterLbl.setForeground(UITheme.TEXT_MUTED);

        String[] categories = {
                "All Categories",
                "Outlier Values",
                "Conflicting Measurements",
                "Duplicate Observations",
                "Delayed Updates",
                "Missing Data"
        };
        filterCombo = new JComboBox<>(categories);
        UITheme.styleComboBox(filterCombo);
        filterCombo.addActionListener(e -> loadAnomalies());

        leftFilters.add(filterLbl);
        leftFilters.add(filterCombo);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setBackground(UITheme.CARD_BG);

        JButton scanBtn = UITheme.createPrimaryButton("Run Scanner");
        scanBtn.setBackground(UITheme.PURPLE_AI);
        scanBtn.addActionListener(e -> runScanner());

        JButton resolveBtn = UITheme.createPrimaryButton("Mark as Resolved");
        resolveBtn.setBackground(UITheme.GREEN_SUCCESS);
        resolveBtn.addActionListener(e -> resolveSelected());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadAnomalies());

        rightActions.add(scanBtn);
        rightActions.add(resolveBtn);
        rightActions.add(refreshBtn);

        topBar.add(leftFilters, BorderLayout.WEST);
        topBar.add(rightActions, BorderLayout.EAST);
        content.add(topBar, BorderLayout.NORTH);

        // Center Table Card
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        String[] cols = {"ID", "Obs ID", "Source Platform", "Anomaly Type", "Severity", "Detected Val", "Expected Range", "Status", "Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Bottom Summary Bar
        summaryLabel = new JLabel("Loading anomalies...");
        summaryLabel.setFont(UITheme.FONT_SMALL);
        summaryLabel.setForeground(UITheme.CYAN);
        tableCard.add(summaryLabel, BorderLayout.SOUTH);

        content.add(tableCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadAnomalies();
    }

    private void runScanner() {
        AnomalyEngine engine = new AnomalyEngine();
        int found = engine.scanAndPersist();
        AuditService.log("ANOMALY_SCAN", "Anomalies", "Discovered " + found + " new defect cases");
        JOptionPane.showMessageDialog(this,
                "Anomaly Scan completed successfully!\nDiscovered & catalogued " + found + " telemetry anomalies.",
                "AI Anomaly Scan Results", JOptionPane.INFORMATION_MESSAGE);
        loadAnomalies();
    }

    private void resolveSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an anomaly row to resolve.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        boolean ok = DatabaseManager.getInstance().resolveAnomaly(id);
        if (ok) {
            AuditService.log("ANOMALY_RESOLVED", "Anomalies", "Resolved incident #" + id);
            JOptionPane.showMessageDialog(this, "Anomaly #" + id + " marked as RESOLVED.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadAnomalies();
        }
    }

    public void loadAnomalies() {
        tableModel.setRowCount(0);
        String selectedFilter = (String) filterCombo.getSelectedItem();
        List<Anomaly> list = DatabaseManager.getInstance().getAllAnomalies();

        int openCount = 0;
        int criticalCount = 0;

        for (Anomaly a : list) {
            boolean matches = "All Categories".equals(selectedFilter) || a.getAnomalyType().equalsIgnoreCase(selectedFilter);
            if (matches) {
                tableModel.addRow(new Object[]{
                        a.getAnomalyId(),
                        a.getObservationId() != null ? a.getObservationId() : "N/A",
                        a.getSourceId(),
                        a.getAnomalyType(),
                        a.getSeverity(),
                        a.getDetectedValue(),
                        a.getExpectedRange(),
                        a.getResolutionStatus(),
                        a.getDescription()
                });

                if ("OPEN".equalsIgnoreCase(a.getResolutionStatus())) {
                    openCount++;
                }
                if ("CRITICAL".equalsIgnoreCase(a.getSeverity())) {
                    criticalCount++;
                }
            }
        }

        summaryLabel.setText("Active Filter: " + selectedFilter + " | Total Displayed: " + tableModel.getRowCount() +
                " | Unresolved (OPEN): " + openCount + " | Critical Severity: " + criticalCount);
    }
}
