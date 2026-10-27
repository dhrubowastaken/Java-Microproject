package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.engine.AIEngine;
import spaceintel.model.ConfidenceAssessment;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 7: ConfidenceAssessmentView.java
 * PURPOSE: Explainable AI Confidence Assessment Viewer & Decision Support (Module 5).
 * EXPLANATION FOR BEGINNERS:
 * Modern defense and space software requires Explainable AI (XAI).
 * When this system tells commanders to take an action, it displays:
 * 1. Final Confidence Rating (0 - 100%)
 * 2. Hardware Reliability Breakdown
 * 3. Spatial & Environmental Consistency Score
 * 4. Human-Readable Evidence Summary
 * 5. Recommended Decision: PROCEED, CORROBORATE, REJECT, or ESCALATE
 * ============================================================================
 */
public class ConfidenceAssessmentView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel obsIdLabel, confScoreLabel, decisionBadge;
    private JProgressBar relBar, constBar, confBar;
    private JTextArea evidenceSummaryArea;

    public ConfidenceAssessmentView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "AI Confidence Scores",
                "See how the AI calculates confidence for each observation using reliability and consistency."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // CENTER: Assessments Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel listTitle = new JLabel("Confidence Scores Log");
        listTitle.setFont(UITheme.FONT_HEADER);
        listTitle.setForeground(UITheme.TEXT_PRIMARY);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(UITheme.CARD_BG);

        JButton evalAllBtn = UITheme.createPrimaryButton("Re-evaluate All");
        evalAllBtn.setBackground(UITheme.PURPLE_AI);
        evalAllBtn.addActionListener(e -> runFullReassessment());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadAssessments());

        btnPanel.add(evalAllBtn);
        btnPanel.add(refreshBtn);

        tableHeader.add(listTitle, BorderLayout.WEST);
        tableHeader.add(btnPanel, BorderLayout.EAST);

        String[] cols = {"Assessment ID", "Obs ID", "Confidence", "Reliability", "Consistency", "Decision", "Assessed At"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Row Selection Listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                displayDetail(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        // RIGHT: Explainable AI Card
        JPanel explainCard = UITheme.createCard();
        explainCard.setLayout(new BoxLayout(explainCard, BoxLayout.Y_AXIS));
        explainCard.setPreferredSize(new Dimension(360, 0));

        JLabel cardTitle = new JLabel("Score Breakdown");
        cardTitle.setFont(UITheme.FONT_HEADER);
        cardTitle.setForeground(UITheme.CYAN);

        obsIdLabel = new JLabel("Click any row in the table");
        obsIdLabel.setFont(UITheme.FONT_SMALL);
        obsIdLabel.setForeground(UITheme.TEXT_MUTED);

        explainCard.add(cardTitle);
        explainCard.add(Box.createVerticalStrut(3));
        explainCard.add(obsIdLabel);
        explainCard.add(Box.createVerticalStrut(15));

        // Big Confidence Score & Decision
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBackground(UITheme.CARD_BG);

        confScoreLabel = new JLabel("-- %");
        confScoreLabel.setFont(UITheme.FONT_METRIC_VAL);
        confScoreLabel.setForeground(UITheme.CYAN);

        decisionBadge = new JLabel("SELECT ROW");
        decisionBadge.setFont(UITheme.FONT_BOLD);
        decisionBadge.setForeground(UITheme.AMBER_WARN);

        scorePanel.add(confScoreLabel, BorderLayout.WEST);
        scorePanel.add(decisionBadge, BorderLayout.EAST);
        explainCard.add(scorePanel);
        explainCard.add(Box.createVerticalStrut(15));

        // Progress Bar 1: Hardware Reliability
        explainCard.add(createMiniLabel("1. Source Reliability (50% weight):"));
        relBar = createProgressBar(UITheme.BLUE_PRIMARY);
        explainCard.add(relBar);
        explainCard.add(Box.createVerticalStrut(10));

        // Progress Bar 2: Environmental Consistency
        explainCard.add(createMiniLabel("2. Cross-Sensor Consistency (50% weight):"));
        constBar = createProgressBar(UITheme.PURPLE_AI);
        explainCard.add(constBar);
        explainCard.add(Box.createVerticalStrut(10));

        // Progress Bar 3: Net Confidence
        explainCard.add(createMiniLabel("3. Total Confidence Score:"));
        confBar = createProgressBar(UITheme.GREEN_SUCCESS);
        explainCard.add(confBar);
        explainCard.add(Box.createVerticalStrut(15));

        // Explainability Text Area
        explainCard.add(createMiniLabel("Reasoning & Explanation:"));
        evidenceSummaryArea = new JTextArea("Click an observation above to see how this score was calculated.");
        evidenceSummaryArea.setFont(UITheme.FONT_BODY);
        evidenceSummaryArea.setForeground(UITheme.TEXT_PRIMARY);
        evidenceSummaryArea.setBackground(UITheme.PANEL_BG);
        evidenceSummaryArea.setLineWrap(true);
        evidenceSummaryArea.setWrapStyleWord(true);
        evidenceSummaryArea.setEditable(false);
        evidenceSummaryArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JScrollPane textScroll = new JScrollPane(evidenceSummaryArea);
        textScroll.setPreferredSize(new Dimension(0, 130));
        textScroll.setBorder(null);
        explainCard.add(textScroll);

        content.add(explainCard, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);

        loadAssessments();
    }

    private JLabel createMiniLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private JProgressBar createProgressBar(Color c) {
        JProgressBar b = new JProgressBar(0, 100);
        b.setValue(0);
        b.setStringPainted(true);
        b.setForeground(c);
        b.setBackground(UITheme.PANEL_BG);
        b.setFont(UITheme.FONT_SMALL);
        b.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        b.setPreferredSize(new Dimension(0, 20));
        return b;
    }

    private void displayDetail(int row) {
        String obsId = (String) tableModel.getValueAt(row, 1);
        String confStr = tableModel.getValueAt(row, 2).toString().replace("%", "").trim();
        String relStr = tableModel.getValueAt(row, 3).toString().replace("%", "").trim();
        String constStr = tableModel.getValueAt(row, 4).toString().replace("%", "").trim();
        String decision = (String) tableModel.getValueAt(row, 5);

        double conf = Double.parseDouble(confStr);
        double rel = Double.parseDouble(relStr);
        double con = Double.parseDouble(constStr);

        obsIdLabel.setText("Evaluating: " + obsId);
        confScoreLabel.setText(String.format("%.1f%%", conf));
        decisionBadge.setText(decision);

        if ("PROCEED".equalsIgnoreCase(decision)) {
            decisionBadge.setForeground(UITheme.GREEN_SUCCESS);
            confScoreLabel.setForeground(UITheme.GREEN_SUCCESS);
        } else if ("CORROBORATE".equalsIgnoreCase(decision)) {
            decisionBadge.setForeground(UITheme.AMBER_WARN);
            confScoreLabel.setForeground(UITheme.AMBER_WARN);
        } else {
            decisionBadge.setForeground(UITheme.RED_CRITICAL);
            confScoreLabel.setForeground(UITheme.RED_CRITICAL);
        }

        relBar.setValue((int) rel);
        relBar.setString(String.format("%.1f%%", rel));

        constBar.setValue((int) con);
        constBar.setString(String.format("%.1f%%", con));

        confBar.setValue((int) conf);
        confBar.setString(String.format("%.1f%%", conf));

        // Look up evidence summary from list
        List<ConfidenceAssessment> list = DatabaseManager.getInstance().getAllConfidenceAssessments();
        for (ConfidenceAssessment ca : list) {
            if (ca.getObservationId().equalsIgnoreCase(obsId)) {
                evidenceSummaryArea.setText(
                        ca.getEvidenceSummary() + "\n\n" +
                        "Formula: (0.5 * " + rel + "%) + (0.5 * " + con + "%) = " + conf + "%\n" +
                        "Recommended Operational Directive: " + ca.getRecommendedDecision()
                );
                break;
            }
        }
    }

    private void runFullReassessment() {
        AIEngine engine = new AIEngine();
        int count = engine.assessAllRawObservations();
        AuditService.log("AI_REASSESSMENT", "Confidence_Assessment", "Evaluated " + count + " observations");
        JOptionPane.showMessageDialog(this, "AI Engine successfully evaluated " + count + " observations!", "AI Engine Run Complete", JOptionPane.INFORMATION_MESSAGE);
        loadAssessments();
    }

    public void loadAssessments() {
        tableModel.setRowCount(0);
        List<ConfidenceAssessment> list = DatabaseManager.getInstance().getAllConfidenceAssessments();
        for (ConfidenceAssessment ca : list) {
            tableModel.addRow(new Object[]{
                    ca.getAssessmentId(),
                    ca.getObservationId(),
                    String.format("%.1f%%", ca.getConfidenceScore()),
                    String.format("%.1f%%", ca.getReliabilityScore()),
                    String.format("%.1f%%", ca.getConsistencyScore()),
                    ca.getRecommendedDecision(),
                    ca.getAssessedAt()
            });
        }
        if (tableModel.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }
}
