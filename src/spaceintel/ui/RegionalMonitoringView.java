package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.Observation;
import spaceintel.model.Region;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 10: RegionalMonitoringView.java
 * PURPOSE: Geospatial Territorial Surveillance & Sector Assessment (Screen 10).
 * EXPLANATION FOR BEGINNERS:
 * Space Intelligence groups sensor data by geographical regions.
 * Analysts can click any monitored sector to see its coordinates, terrain type,
 * threat level, and all observations currently captured in that zone.
 * ============================================================================
 */
public class RegionalMonitoringView extends JPanel {

    private DefaultTableModel regionTableModel;
    private DefaultTableModel obsInRegionModel;
    private JTable regionTable;
    private JLabel selectedRegionTitle;
    private JLabel threatLevelBadge;
    private JLabel coordsLabel;

    public RegionalMonitoringView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Regional Monitoring",
                "Select a geographic area to view its weather conditions, active satellites, and recent sensor readings."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(2, 1, 15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // TOP: Regions Table Card
        JPanel topCard = UITheme.createCard();
        topCard.setLayout(new BorderLayout(10, 10));

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(UITheme.CARD_BG);
        JLabel title1 = new JLabel("Monitored Geographic Regions");
        title1.setFont(UITheme.FONT_HEADER);
        title1.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadRegions());

        topHeader.add(title1, BorderLayout.WEST);
        topHeader.add(refreshBtn, BorderLayout.EAST);

        String[] regCols = {"Region ID", "Region Name", "Latitude", "Longitude", "Terrain", "Risk Level", "Description"};
        regionTableModel = new DefaultTableModel(regCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        regionTable = new JTable(regionTableModel);
        UITheme.styleTable(regionTable);

        // Row Selection
        regionTable.getSelectionModel().addListSelectionListener(e -> {
            int row = regionTable.getSelectedRow();
            if (row >= 0) {
                String regId = (String) regionTableModel.getValueAt(row, 0);
                String regName = (String) regionTableModel.getValueAt(row, 1);
                String threat = (String) regionTableModel.getValueAt(row, 5);
                String lat = regionTableModel.getValueAt(row, 2).toString();
                String lon = regionTableModel.getValueAt(row, 3).toString();

                selectedRegionTitle.setText("Selected Region: " + regName + " (" + regId + ")");
                threatLevelBadge.setText("Risk Level: " + threat);
                coordsLabel.setText("Coordinates: " + lat + "° N, " + lon + "° E");

                if ("CRITICAL".equalsIgnoreCase(threat)) {
                    threatLevelBadge.setForeground(UITheme.RED_CRITICAL);
                } else if ("ELEVATED".equalsIgnoreCase(threat)) {
                    threatLevelBadge.setForeground(UITheme.AMBER_WARN);
                } else {
                    threatLevelBadge.setForeground(UITheme.GREEN_SUCCESS);
                }

                loadObservationsForRegion(regId);
            }
        });

        JScrollPane scroll1 = new JScrollPane(regionTable);
        scroll1.getViewport().setBackground(UITheme.PANEL_BG);

        topCard.add(topHeader, BorderLayout.NORTH);
        topCard.add(scroll1, BorderLayout.CENTER);
        content.add(topCard);

        // BOTTOM: Filtered Observations in Selected Region
        JPanel bottomCard = UITheme.createCard();
        bottomCard.setLayout(new BorderLayout(10, 10));

        JPanel botHeader = new JPanel(new BorderLayout());
        botHeader.setBackground(UITheme.CARD_BG);

        selectedRegionTitle = new JLabel("Selected Region: Click a region above");
        selectedRegionTitle.setFont(UITheme.FONT_HEADER);
        selectedRegionTitle.setForeground(UITheme.CYAN);

        JPanel badgeBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        badgeBox.setBackground(UITheme.CARD_BG);

        coordsLabel = new JLabel("Coordinates: --");
        coordsLabel.setFont(UITheme.FONT_SMALL);
        coordsLabel.setForeground(UITheme.TEXT_MUTED);

        threatLevelBadge = new JLabel("Risk Level: --");
        threatLevelBadge.setFont(UITheme.FONT_BOLD);
        threatLevelBadge.setForeground(UITheme.TEXT_PRIMARY);

        badgeBox.add(coordsLabel);
        badgeBox.add(threatLevelBadge);

        botHeader.add(selectedRegionTitle, BorderLayout.WEST);
        botHeader.add(badgeBox, BorderLayout.EAST);

        String[] obsCols = {"Observation ID", "Timestamp", "Type", "Measured Value", "Source Platform", "Status"};
        obsInRegionModel = new DefaultTableModel(obsCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable obsTable = new JTable(obsInRegionModel);
        UITheme.styleTable(obsTable);

        JScrollPane scroll2 = new JScrollPane(obsTable);
        scroll2.getViewport().setBackground(UITheme.PANEL_BG);

        bottomCard.add(botHeader, BorderLayout.NORTH);
        bottomCard.add(scroll2, BorderLayout.CENTER);
        content.add(bottomCard);

        add(content, BorderLayout.CENTER);

        loadRegions();
    }

    private void loadObservationsForRegion(String regionId) {
        obsInRegionModel.setRowCount(0);
        List<Observation> list = DatabaseManager.getInstance().getAllObservations();
        for (Observation o : list) {
            if (o.getRegionId().equalsIgnoreCase(regionId)) {
                obsInRegionModel.addRow(new Object[]{
                        o.getObservationId(),
                        o.getTimestamp(),
                        o.getObservationType(),
                        o.getMeasuredValue() + " " + o.getUnit(),
                        o.getSourceId(),
                        o.getStatus()
                });
            }
        }
    }

    public void loadRegions() {
        regionTableModel.setRowCount(0);
        List<Region> list = DatabaseManager.getInstance().getAllRegions();
        for (Region r : list) {
            regionTableModel.addRow(new Object[]{
                    r.getRegionId(),
                    r.getRegionName(),
                    r.getCenterLat(),
                    r.getCenterLon(),
                    r.getTerrainType(),
                    r.getThreatLevel(),
                    r.getDescription()
            });
        }
        if (regionTableModel.getRowCount() > 0) {
            regionTable.setRowSelectionInterval(0, 0);
        }
    }
}
