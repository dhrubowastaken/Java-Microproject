package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.Evidence;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 9: EvidenceViewerView.java
 * PURPOSE: Evidence Management & Corroboration Matrix Viewer (Module 7).
 * EXPLANATION FOR BEGINNERS:
 * When analyzing defense or space anomalies, analysts need to see opposing facts:
 * - What supporting sensor observations confirm the event?
 * - What contradicting observations challenge the hypothesis?
 * - What is the plain-English justification and final operational directive?
 * ============================================================================
 */
public class EvidenceViewerView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextArea supportingArea, contradictingArea, explanationArea, recArea;

    public EvidenceViewerView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Evidence & Explanations",
                "Compare supporting and contradicting sensor readings to understand why data was accepted or flagged."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // CENTER: Evidence Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel listTitle = new JLabel("Evidence Records");
        listTitle.setFont(UITheme.FONT_HEADER);
        listTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadEvidenceData());

        tableHeader.add(listTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Evidence ID", "Assessment Ref", "Supporting Observation Summary", "Recommendation Directive", "Recorded At"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Selection Listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                displayEvidenceDetail(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        // RIGHT: Evidence Comparison Matrix Panel
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(UITheme.BG_DARK);
        detailPanel.setPreferredSize(new Dimension(380, 0));

        // Card 1: Supporting Evidence (Green Accent)
        JPanel supportCard = UITheme.createCard();
        supportCard.setLayout(new BorderLayout(5, 5));
        JLabel suppTitle = new JLabel("SUPPORTING SENSOR DATA (CORROBORATING)");
        suppTitle.setFont(UITheme.FONT_SMALL);
        suppTitle.setForeground(UITheme.GREEN_SUCCESS);
        supportingArea = createReadOnlyTextArea();
        supportCard.add(suppTitle, BorderLayout.NORTH);
        supportCard.add(new JScrollPane(supportingArea), BorderLayout.CENTER);
        supportCard.setPreferredSize(new Dimension(380, 100));

        // Card 2: Contradicting Evidence (Red Accent)
        JPanel contCard = UITheme.createCard();
        contCard.setLayout(new BorderLayout(5, 5));
        JLabel contTitle = new JLabel("CONTRADICTING SENSOR DATA (DISPUTED)");
        contTitle.setFont(UITheme.FONT_SMALL);
        contTitle.setForeground(UITheme.RED_CRITICAL);
        contradictingArea = createReadOnlyTextArea();
        contCard.add(contTitle, BorderLayout.NORTH);
        contCard.add(new JScrollPane(contradictingArea), BorderLayout.CENTER);
        contCard.setPreferredSize(new Dimension(380, 100));

        // Card 3: Confidence Explanation
        JPanel expCard = UITheme.createCard();
        expCard.setLayout(new BorderLayout(5, 5));
        JLabel expTitle = new JLabel("WHY WE TRUST OR DOUBT THIS DATA");
        expTitle.setFont(UITheme.FONT_SMALL);
        expTitle.setForeground(UITheme.CYAN);
        explanationArea = createReadOnlyTextArea();
        expCard.add(expTitle, BorderLayout.NORTH);
        expCard.add(new JScrollPane(explanationArea), BorderLayout.CENTER);
        expCard.setPreferredSize(new Dimension(380, 100));

        // Card 4: Action Recommendation
        JPanel recCard = UITheme.createCard();
        recCard.setLayout(new BorderLayout(5, 5));
        JLabel recTitle = new JLabel("RECOMMENDED ACTION");
        recTitle.setFont(UITheme.FONT_SMALL);
        recTitle.setForeground(UITheme.AMBER_WARN);
        recArea = createReadOnlyTextArea();
        recCard.add(recTitle, BorderLayout.NORTH);
        recCard.add(new JScrollPane(recArea), BorderLayout.CENTER);
        recCard.setPreferredSize(new Dimension(380, 90));

        detailPanel.add(supportCard);
        detailPanel.add(Box.createVerticalStrut(8));
        detailPanel.add(contCard);
        detailPanel.add(Box.createVerticalStrut(8));
        detailPanel.add(expCard);
        detailPanel.add(Box.createVerticalStrut(8));
        detailPanel.add(recCard);

        content.add(detailPanel, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);

        loadEvidenceData();
    }

    private JTextArea createReadOnlyTextArea() {
        JTextArea a = new JTextArea();
        a.setFont(UITheme.FONT_BODY);
        a.setForeground(UITheme.TEXT_PRIMARY);
        a.setBackground(UITheme.PANEL_BG);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setEditable(false);
        a.setBorder(new EmptyBorder(6, 6, 6, 6));
        return a;
    }

    private void displayEvidenceDetail(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        List<Evidence> list = DatabaseManager.getInstance().getAllEvidence();
        for (Evidence ev : list) {
            if (ev.getEvidenceId() == id) {
                supportingArea.setText(ev.getSupportingObservation());
                contradictingArea.setText(ev.getContradictingObservation());
                explanationArea.setText(ev.getConfidenceExplanation());
                recArea.setText(ev.getRecommendation());
                break;
            }
        }
    }

    public void loadEvidenceData() {
        tableModel.setRowCount(0);
        List<Evidence> list = DatabaseManager.getInstance().getAllEvidence();
        for (Evidence ev : list) {
            tableModel.addRow(new Object[]{
                    ev.getEvidenceId(),
                    "Assessment #" + ev.getAssessmentId(),
                    ev.getSupportingObservation(),
                    ev.getRecommendation(),
                    ev.getRecordedAt()
            });
        }
        if (tableModel.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }
}
