package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.Anomaly;
import spaceintel.model.ConfidenceAssessment;
import spaceintel.model.Observation;
import spaceintel.model.SourceCredibility;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * SCREEN 18: AnalyticsDashboardView.java
 * PURPOSE: Deep Statistical Telemetry & Distribution Analytics (Screen 18).
 * EXPLANATION FOR BEGINNERS:
 * Data Scientists need analytical aggregations:
 * 1. Observation distribution across measurement categories
 * 2. Data Provider Reliability Bar Breakdown
 * 3. AI Confidence Distribution (High, Medium, Low breakdown)
 * 4. Anomaly-to-Observation Defect Rate
 * ============================================================================
 */
public class AnalyticsDashboardView extends JPanel {

    private JPanel statsPanel;

    public AnalyticsDashboardView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Data Analytics",
                "Visual summaries of observations by type, error rates, and sensor accuracy."
        );
        add(header, BorderLayout.NORTH);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(UITheme.BG_DARK);
        statsPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        loadAnalytics();

        JScrollPane scroll = new JScrollPane(statsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void loadAnalytics() {
        statsPanel.removeAll();
        DatabaseManager db = DatabaseManager.getInstance();

        List<Observation> obsList = db.getAllObservations();
        List<SourceCredibility> credList = db.getAllSourceCredibility();
        List<ConfidenceAssessment> confList = db.getAllConfidenceAssessments();
        List<Anomaly> anomList = db.getAllAnomalies();

        // Section 1: High Level Metrics
        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 12, 12));
        kpiRow.setBackground(UITheme.BG_DARK);
        kpiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        double defectRate = obsList.isEmpty() ? 0.0 : ((double) anomList.size() / obsList.size()) * 100.0;

        double sumConf = 0;
        for (ConfidenceAssessment c : confList) sumConf += c.getConfidenceScore();
        double avgConf = confList.isEmpty() ? 0.0 : sumConf / confList.size();

        kpiRow.add(UITheme.createMetricCard("Total Observations", String.valueOf(obsList.size()), "Records", UITheme.CYAN));
        kpiRow.add(UITheme.createMetricCard("Error Rate", String.format("%.1f", defectRate), "%", UITheme.AMBER_WARN));
        kpiRow.add(UITheme.createMetricCard("Average Confidence", String.format("%.1f", avgConf), "%", UITheme.GREEN_SUCCESS));
        kpiRow.add(UITheme.createMetricCard("Active Anomalies", String.valueOf(anomList.size()), "Cases", UITheme.RED_CRITICAL));

        statsPanel.add(kpiRow);
        statsPanel.add(Box.createVerticalStrut(20));

        // Section 2: Observation Distribution by Type
        JPanel obsTypeCard = UITheme.createCard();
        obsTypeCard.setLayout(new BoxLayout(obsTypeCard, BoxLayout.Y_AXIS));

        JLabel title1 = new JLabel("Observations by Measurement Type");
        title1.setFont(UITheme.FONT_HEADER);
        title1.setForeground(UITheme.TEXT_PRIMARY);
        obsTypeCard.add(title1);
        obsTypeCard.add(Box.createVerticalStrut(12));

        Map<String, Integer> counts = new HashMap<>();
        for (Observation o : obsList) {
            counts.put(o.getObservationType(), counts.getOrDefault(o.getObservationType(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            JPanel row = new JPanel(new BorderLayout(10, 5));
            row.setBackground(UITheme.CARD_BG);

            JLabel l = new JLabel(entry.getKey() + " (" + entry.getValue() + " records)");
            l.setFont(UITheme.FONT_BODY);
            l.setForeground(UITheme.TEXT_MUTED);
            l.setPreferredSize(new Dimension(220, 22));

            int pct = obsList.isEmpty() ? 0 : (int)(((double)entry.getValue() / obsList.size()) * 100);
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(pct);
            bar.setStringPainted(true);
            bar.setString(pct + "%");
            bar.setForeground(UITheme.BLUE_PRIMARY);
            bar.setBackground(UITheme.PANEL_BG);
            bar.setFont(UITheme.FONT_SMALL);

            row.add(l, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            obsTypeCard.add(row);
            obsTypeCard.add(Box.createVerticalStrut(6));
        }
        statsPanel.add(obsTypeCard);
        statsPanel.add(Box.createVerticalStrut(20));

        // Section 3: Data Provider Trust Leaderboard
        JPanel credCard = UITheme.createCard();
        credCard.setLayout(new BoxLayout(credCard, BoxLayout.Y_AXIS));

        JLabel title2 = new JLabel("Sensor Accuracy Comparison");
        title2.setFont(UITheme.FONT_HEADER);
        title2.setForeground(UITheme.TEXT_PRIMARY);
        credCard.add(title2);
        credCard.add(Box.createVerticalStrut(12));

        for (SourceCredibility sc : credList) {
            JPanel row = new JPanel(new BorderLayout(10, 5));
            row.setBackground(UITheme.CARD_BG);

            JLabel l = new JLabel(sc.getSourceId() + " [" + sc.getValidationStatus() + "]");
            l.setFont(UITheme.FONT_BODY);
            l.setForeground(UITheme.TEXT_MUTED);
            l.setPreferredSize(new Dimension(240, 22));

            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue((int) sc.getReliabilityScore());
            bar.setStringPainted(true);
            bar.setString(String.format("%.1f%%", sc.getReliabilityScore()));

            if (sc.getReliabilityScore() >= 90) {
                bar.setForeground(UITheme.GREEN_SUCCESS);
            } else if (sc.getReliabilityScore() >= 75) {
                bar.setForeground(UITheme.AMBER_WARN);
            } else {
                bar.setForeground(UITheme.RED_CRITICAL);
            }

            bar.setBackground(UITheme.PANEL_BG);
            bar.setFont(UITheme.FONT_SMALL);

            row.add(l, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            credCard.add(row);
            credCard.add(Box.createVerticalStrut(6));
        }
        statsPanel.add(credCard);

        statsPanel.revalidate();
        statsPanel.repaint();
    }
}
