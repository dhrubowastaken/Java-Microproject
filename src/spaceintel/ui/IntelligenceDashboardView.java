package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.IntelligenceReport;
import spaceintel.util.AuditService;
import spaceintel.util.SessionManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 11: IntelligenceDashboardView.java
 * PURPOSE: High-Level Tactical Intelligence Feed & Mission Briefings (Module 8).
 * EXPLANATION FOR BEGINNERS:
 * Intelligence Officers and Commanders don't look at single sensor numbers;
 * they read synthesized Intelligence Reports that combine multi-source satellite passes
 * with threat assessments and AI confidence ratings.
 * ============================================================================
 */
public class IntelligenceDashboardView extends JPanel {

    private DefaultTableModel reportTableModel;
    private JTable reportTable;
    private JLabel reportTitleLabel, authorLabel, threatBadge, confidenceBadge;
    private JTextArea summaryArea;
    private JTextField newTitleField;
    private JTextArea newSummaryArea;
    private JComboBox<String> newThreatCombo, newRegionCombo;

    public IntelligenceDashboardView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Intelligence Reports",
                "Read analysis summaries prepared for decision-makers or write and publish a new brief."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Reports List Table
        JPanel listCard = UITheme.createCard();
        listCard.setLayout(new BorderLayout(10, 10));
        listCard.setPreferredSize(new Dimension(520, 0));

        JPanel listHeader = new JPanel(new BorderLayout());
        listHeader.setBackground(UITheme.CARD_BG);
        JLabel title = new JLabel("Published Reports");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadReports());

        listHeader.add(title, BorderLayout.WEST);
        listHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Report ID", "Report Title", "Author", "Risk Level", "AI Confidence", "Published"};
        reportTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        reportTable = new JTable(reportTableModel);
        UITheme.styleTable(reportTable);

        reportTable.getSelectionModel().addListSelectionListener(e -> {
            int row = reportTable.getSelectedRow();
            if (row >= 0) {
                displayReport(row);
            }
        });

        JScrollPane scroll = new JScrollPane(reportTable);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        listCard.add(listHeader, BorderLayout.NORTH);
        listCard.add(scroll, BorderLayout.CENTER);
        content.add(listCard, BorderLayout.WEST);

        // RIGHT: Dossier Inspector & New Report Creator (Tabs)
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(UITheme.PANEL_BG);
        tabs.setForeground(UITheme.TEXT_PRIMARY);
        tabs.setFont(UITheme.FONT_BOLD);

        // TAB 1: Dossier View
        JPanel viewTab = UITheme.createCard();
        viewTab.setLayout(new BoxLayout(viewTab, BoxLayout.Y_AXIS));

        reportTitleLabel = new JLabel("Select a report from the list");
        reportTitleLabel.setFont(UITheme.FONT_HEADER);
        reportTitleLabel.setForeground(UITheme.CYAN);

        authorLabel = new JLabel("Author: --");
        authorLabel.setFont(UITheme.FONT_SMALL);
        authorLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        badgeRow.setBackground(UITheme.CARD_BG);

        threatBadge = new JLabel("Risk Level: --");
        threatBadge.setFont(UITheme.FONT_BOLD);

        confidenceBadge = new JLabel("AI Rating: --");
        confidenceBadge.setFont(UITheme.FONT_BOLD);
        confidenceBadge.setForeground(UITheme.CYAN);

        badgeRow.add(threatBadge);
        badgeRow.add(confidenceBadge);

        summaryArea = new JTextArea();
        summaryArea.setFont(UITheme.FONT_BODY);
        summaryArea.setForeground(UITheme.TEXT_PRIMARY);
        summaryArea.setBackground(UITheme.PANEL_BG);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setEditable(false);
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        viewTab.add(reportTitleLabel);
        viewTab.add(Box.createVerticalStrut(5));
        viewTab.add(authorLabel);
        viewTab.add(Box.createVerticalStrut(10));
        viewTab.add(badgeRow);
        viewTab.add(Box.createVerticalStrut(15));
        viewTab.add(new JScrollPane(summaryArea));

        // TAB 2: Draft New Dossier
        JPanel draftTab = UITheme.createCard();
        draftTab.setLayout(new BoxLayout(draftTab, BoxLayout.Y_AXIS));

        JLabel draftTitle = new JLabel("Write New Report");
        draftTitle.setFont(UITheme.FONT_HEADER);
        draftTitle.setForeground(UITheme.CYAN);

        draftTab.add(draftTitle);
        draftTab.add(Box.createVerticalStrut(10));

        draftTab.add(createMiniLabel("Report Title / Subject:"));
        newTitleField = UITheme.createTextField();
        newTitleField.setText("Satellite Surface Reconnaissance Report");
        draftTab.add(newTitleField);
        draftTab.add(Box.createVerticalStrut(8));

        draftTab.add(createMiniLabel("Operational Region:"));
        newRegionCombo = new JComboBox<>(new String[]{"REG-01", "REG-02", "REG-03", "REG-04", "REG-05"});
        UITheme.styleComboBox(newRegionCombo);
        draftTab.add(newRegionCombo);
        draftTab.add(Box.createVerticalStrut(8));

        draftTab.add(createMiniLabel("Threat Assessment Status:"));
        newThreatCombo = new JComboBox<>(new String[]{"STABLE", "WATCH", "WARNING", "CRITICAL"});
        UITheme.styleComboBox(newThreatCombo);
        draftTab.add(newThreatCombo);
        draftTab.add(Box.createVerticalStrut(8));

        draftTab.add(createMiniLabel("Executive Summary & Intelligence Brief:"));
        newSummaryArea = new JTextArea("Cross-referenced multi-satellite observations indicate stable conditions across monitored sectors.");
        newSummaryArea.setFont(UITheme.FONT_BODY);
        newSummaryArea.setForeground(UITheme.TEXT_PRIMARY);
        newSummaryArea.setBackground(UITheme.PANEL_BG);
        newSummaryArea.setLineWrap(true);
        newSummaryArea.setWrapStyleWord(true);
        newSummaryArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        draftTab.add(new JScrollPane(newSummaryArea));
        draftTab.add(Box.createVerticalStrut(10));

        JButton publishBtn = UITheme.createPrimaryButton("Publish Flash Dossier");
        publishBtn.addActionListener(e -> publishReport());
        draftTab.add(publishBtn);

        tabs.addTab("View Dossier", viewTab);
        tabs.addTab("Draft Dossier", draftTab);
        content.add(tabs, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadReports();
    }

    private JLabel createMiniLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void displayReport(int row) {
        String repId = (String) reportTableModel.getValueAt(row, 0);
        List<IntelligenceReport> list = DatabaseManager.getInstance().getAllIntelligenceReports();
        for (IntelligenceReport r : list) {
            if (r.getReportId().equalsIgnoreCase(repId)) {
                reportTitleLabel.setText(r.getTitle() + " [" + r.getReportId() + "]");
                authorLabel.setText("Author: " + r.getAuthor() + " | Region: " + r.getRegionId() + " | Date: " + r.getCreatedAt());
                threatBadge.setText("THREAT: " + r.getThreatAssessment());
                confidenceBadge.setText("AI CONFIDENCE: " + r.getConfidenceRating() + "%");

                if ("CRITICAL".equalsIgnoreCase(r.getThreatAssessment())) {
                    threatBadge.setForeground(UITheme.RED_CRITICAL);
                } else if ("WARNING".equalsIgnoreCase(r.getThreatAssessment())) {
                    threatBadge.setForeground(UITheme.AMBER_WARN);
                } else {
                    threatBadge.setForeground(UITheme.GREEN_SUCCESS);
                }

                summaryArea.setText(r.getExecutiveSummary());
                break;
            }
        }
    }

    private void publishReport() {
        String title = newTitleField.getText().trim();
        String summary = newSummaryArea.getText().trim();
        String region = (String) newRegionCombo.getSelectedItem();
        String threat = (String) newThreatCombo.getSelectedItem();

        if (title.isEmpty() || summary.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Title and Executive Summary.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = "INTEL-2026-R" + (int)(Math.random() * 900 + 100);
        IntelligenceReport rep = new IntelligenceReport(
                id, title, SessionManager.getFullName(), region, summary, threat, 94.2, null
        );

        boolean ok = DatabaseManager.getInstance().addIntelligenceReport(rep);
        if (ok) {
            AuditService.log("REPORT_PUBLISHED", "Intelligence_Reports", "Published " + id);
            JOptionPane.showMessageDialog(this, "Dossier " + id + " published successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            newTitleField.setText("");
            loadReports();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to publish report.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadReports() {
        reportTableModel.setRowCount(0);
        List<IntelligenceReport> list = DatabaseManager.getInstance().getAllIntelligenceReports();
        for (IntelligenceReport r : list) {
            reportTableModel.addRow(new Object[]{
                    r.getReportId(),
                    r.getTitle(),
                    r.getAuthor(),
                    r.getThreatAssessment(),
                    String.format("%.1f%%", r.getConfidenceRating()),
                    r.getCreatedAt()
            });
        }
        if (reportTableModel.getRowCount() > 0) {
            reportTable.setRowSelectionInterval(0, 0);
        }
    }
}
