package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.engine.AIEngine;
import spaceintel.model.ConfidenceAssessment;
import spaceintel.model.DataSource;
import spaceintel.model.Observation;
import spaceintel.model.Region;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 5: ObservationEntryView.java
 * PURPOSE: Manual and Automated Geospatial Observation Telemetry Ingestion (Module 3).
 * EXPLANATION FOR BEGINNERS:
 * Analysts record sensor data received from space orbits or ground teams.
 * This screen performs:
 * 1. Input Validation (Latitude: -90 to +90, Longitude: -180 to +180)
 * 2. Database Insertion (Observations & Observation_History tables)
 * 3. Real-Time AI Confidence Scoring (Calculates immediate confidence & recommendation!)
 * ============================================================================
 */
public class ObservationEntryView extends JPanel {

    private JTextField idField, latField, lonField, valField, unitField;
    private JComboBox<String> typeCombo, sourceCombo, regionCombo;
    private DefaultTableModel tableModel;

    public ObservationEntryView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Observation Entry",
                "Record new sensor readings and instantly calculate their AI confidence and reliability scores."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Data Entry Form
        JPanel formCard = UITheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(360, 0));

        JLabel formTitle = new JLabel("Record New Observation");
        formTitle.setFont(UITheme.FONT_HEADER);
        formTitle.setForeground(UITheme.CYAN);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(12));

        // Observation ID
        formCard.add(createFieldLabel("Observation ID:"));
        idField = UITheme.createTextField();
        idField.setText("OBS-2026-" + (int)(Math.random() * 900 + 100));
        formCard.add(idField);
        formCard.add(Box.createVerticalStrut(8));

        // Type
        formCard.add(createFieldLabel("Observation Type:"));
        String[] types = {
                "Thermal Anomaly",
                "Atmospheric Pressure",
                "NDVI Vegetation",
                "Optical Surface",
                "Flood Inundation",
                "Glacial Temperature"
        };
        typeCombo = new JComboBox<>(types);
        UITheme.styleComboBox(typeCombo);
        formCard.add(typeCombo);
        formCard.add(Box.createVerticalStrut(8));

        // Coordinates Panel (Lat + Lon side by side)
        JPanel coordsPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        coordsPanel.setBackground(UITheme.CARD_BG);

        JPanel latP = new JPanel(new BorderLayout());
        latP.setBackground(UITheme.CARD_BG);
        latP.add(createFieldLabel("Latitude (-90 to 90):"), BorderLayout.NORTH);
        latField = UITheme.createTextField();
        latField.setText("30.3200");
        latP.add(latField, BorderLayout.CENTER);

        JPanel lonP = new JPanel(new BorderLayout());
        lonP.setBackground(UITheme.CARD_BG);
        lonP.add(createFieldLabel("Longitude (-180 to 180):"), BorderLayout.NORTH);
        lonField = UITheme.createTextField();
        lonField.setText("78.0400");
        lonP.add(lonField, BorderLayout.CENTER);

        coordsPanel.add(latP);
        coordsPanel.add(lonP);
        formCard.add(coordsPanel);
        formCard.add(Box.createVerticalStrut(8));

        // Measured Value & Unit
        JPanel valPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        valPanel.setBackground(UITheme.CARD_BG);

        JPanel vP = new JPanel(new BorderLayout());
        vP.setBackground(UITheme.CARD_BG);
        vP.add(createFieldLabel("Value:"), BorderLayout.NORTH);
        valField = UITheme.createTextField();
        valField.setText("65.4");
        vP.add(valField, BorderLayout.CENTER);

        JPanel uP = new JPanel(new BorderLayout());
        uP.setBackground(UITheme.CARD_BG);
        uP.add(createFieldLabel("Unit (°C, hPa, idx):"), BorderLayout.NORTH);
        unitField = UITheme.createTextField();
        unitField.setText("°C");
        uP.add(unitField, BorderLayout.CENTER);

        valPanel.add(vP);
        valPanel.add(uP);
        formCard.add(valPanel);
        formCard.add(Box.createVerticalStrut(8));

        // Source Dropdown
        formCard.add(createFieldLabel("Data Source:"));
        sourceCombo = new JComboBox<>();
        UITheme.styleComboBox(sourceCombo);
        formCard.add(sourceCombo);
        formCard.add(Box.createVerticalStrut(8));

        // Region Dropdown
        formCard.add(createFieldLabel("Region:"));
        regionCombo = new JComboBox<>();
        UITheme.styleComboBox(regionCombo);
        formCard.add(regionCombo);
        formCard.add(Box.createVerticalStrut(15));

        // Submit Button
        JButton submitBtn = UITheme.createPrimaryButton("Save & Calculate Confidence");
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitBtn.addActionListener(e -> recordObservation());
        formCard.add(submitBtn);

        content.add(formCard, BorderLayout.WEST);

        // RIGHT: Table of Ingested Records
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel listTitle = new JLabel("Recorded Observations");
        listTitle.setFont(UITheme.FONT_HEADER);
        listTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadObservationsTable());

        tableHeader.add(listTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Obs ID", "Type", "Value", "Lat", "Lon", "Source", "Region", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        content.add(tableCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        // Populate dropdowns & table
        refreshDropdowns();
        loadObservationsTable();
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    public void refreshDropdowns() {
        sourceCombo.removeAllItems();
        List<DataSource> sources = DatabaseManager.getInstance().getAllDataSources();
        for (DataSource d : sources) {
            sourceCombo.addItem(d.getSourceId() + " (" + d.getSourceName() + ")");
        }

        regionCombo.removeAllItems();
        List<Region> regions = DatabaseManager.getInstance().getAllRegions();
        for (Region r : regions) {
            regionCombo.addItem(r.getRegionId() + " (" + r.getRegionName() + ")");
        }
    }

    private void recordObservation() {
        String id = idField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        String latStr = latField.getText().trim();
        String lonStr = lonField.getText().trim();
        String valStr = valField.getText().trim();
        String unit = unitField.getText().trim();

        if (id.isEmpty() || latStr.isEmpty() || lonStr.isEmpty() || valStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all required observation fields.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double lat, lon, val;
        try {
            lat = Double.parseDouble(latStr);
            lon = Double.parseDouble(lonStr);
            val = Double.parseDouble(valStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Coordinates and Measured Value must be numeric numbers.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            JOptionPane.showMessageDialog(this, "Latitude must be [-90, +90] and Longitude [-180, +180].", "Coordinates Out of Range", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rawSource = (String) sourceCombo.getSelectedItem();
        String sourceId = (rawSource != null && rawSource.contains(" ")) ? rawSource.split(" ")[0] : "SAT-CARTO3";

        String rawRegion = (String) regionCombo.getSelectedItem();
        String regionId = (rawRegion != null && rawRegion.contains(" ")) ? rawRegion.split(" ")[0] : "REG-01";

        Observation obs = new Observation(id, null, lat, lon, type, val, unit, sourceId, regionId, "RAW");
        boolean ok = DatabaseManager.getInstance().addObservation(obs);

        if (ok) {
            AuditService.log("OBSERVATION_INGESTED", "Observations", "Ingested " + id + " from " + sourceId);

            // Trigger AI Engine to assess confidence immediately!
            AIEngine ai = new AIEngine();
            ConfidenceAssessment ca = ai.evaluateObservation(obs);
            DatabaseManager.getInstance().addConfidenceAssessment(ca);

            String message = String.format(
                    "Observation %s saved successfully!\n\n" +
                    "AI Reliability Score:  %.1f%%\n" +
                    "AI Consistency Score:  %.1f%%\n" +
                    "AI Confidence Rating:  %.1f%%\n" +
                    "AI Recommended Action: %s\n\n" +
                    "Evidence: %s",
                    id, ca.getReliabilityScore(), ca.getConsistencyScore(), ca.getConfidenceScore(),
                    ca.getRecommendedDecision(), ca.getEvidenceSummary()
            );

            JOptionPane.showMessageDialog(this, message, "AI Confidence Engine Results", JOptionPane.INFORMATION_MESSAGE);

            // Reset ID field with new random ID
            idField.setText("OBS-2026-" + (int)(Math.random() * 900 + 100));
            loadObservationsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to insert observation. ID might be duplicate.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadObservationsTable() {
        tableModel.setRowCount(0);
        List<Observation> list = DatabaseManager.getInstance().getAllObservations();
        for (Observation o : list) {
            tableModel.addRow(new Object[]{
                    o.getObservationId(),
                    o.getObservationType(),
                    o.getMeasuredValue() + " " + o.getUnit(),
                    String.format("%.4f", o.getLatitude()),
                    String.format("%.4f", o.getLongitude()),
                    o.getSourceId(),
                    o.getRegionId(),
                    o.getStatus()
            });
        }
    }
}
