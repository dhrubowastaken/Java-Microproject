package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.SourceCredibility;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 6: SourceCredibilityView.java
 * PURPOSE: Source Credibility & Trust Evaluation Dashboard (Module 4).
 * EXPLANATION FOR BEGINNERS:
 * Space agencies track the track record of every data provider.
 * A satellite might drift over time, or a ground sensor battery might fail.
 * This screen displays:
 * 1. Reliability Score (%)
 * 2. Historical Accuracy (%)
 * 3. Validation Status (CERTIFIED, PROVISIONAL, UNDER_REVIEW)
 * 4. Allows analysts to adjust trust ratings when a sensor is recalibrated.
 * ============================================================================
 */
public class SourceCredibilityView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField reliabilityField, accuracyField;
    private JComboBox<String> statusCombo, levelCombo, freqCombo;
    private JLabel selectedSourceLabel;
    private String selectedSourceId = null;

    public SourceCredibilityView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Source Credibility",
                "View and update how trustworthy each data source is based on its past accuracy and update frequency."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // CENTER: Credibility Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel listTitle = new JLabel("Source Trust Scores");
        listTitle.setFont(UITheme.FONT_HEADER);
        listTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadCredibilityData());

        tableHeader.add(listTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Source ID", "Reliability Score", "Historical Accuracy", "Update Freq", "Validation Status", "Confidence Level", "Last Evaluated"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        // Selection Listener to populate edit form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedSourceId = (String) table.getValueAt(row, 0);
                selectedSourceLabel.setText("Editing: " + selectedSourceId);
                reliabilityField.setText(table.getValueAt(row, 1).toString().replace("%", "").trim());
                accuracyField.setText(table.getValueAt(row, 2).toString().replace("%", "").trim());
                freqCombo.setSelectedItem(table.getValueAt(row, 3).toString());
                statusCombo.setSelectedItem(table.getValueAt(row, 4).toString());
                levelCombo.setSelectedItem(table.getValueAt(row, 5).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        // RIGHT: Quick Recalibration Form
        JPanel editCard = UITheme.createCard();
        editCard.setLayout(new BoxLayout(editCard, BoxLayout.Y_AXIS));
        editCard.setPreferredSize(new Dimension(320, 0));

        JLabel editTitle = new JLabel("Edit Trust Score");
        editTitle.setFont(UITheme.FONT_HEADER);
        editTitle.setForeground(UITheme.CYAN);

        selectedSourceLabel = new JLabel("Select a source from table");
        selectedSourceLabel.setFont(UITheme.FONT_SMALL);
        selectedSourceLabel.setForeground(UITheme.AMBER_WARN);

        editCard.add(editTitle);
        editCard.add(Box.createVerticalStrut(5));
        editCard.add(selectedSourceLabel);
        editCard.add(Box.createVerticalStrut(15));

        // Reliability
        editCard.add(createFieldLabel("Reliability Score (0 - 100%):"));
        reliabilityField = UITheme.createTextField();
        editCard.add(reliabilityField);
        editCard.add(Box.createVerticalStrut(10));

        // Accuracy
        editCard.add(createFieldLabel("Historical Accuracy (0 - 100%):"));
        accuracyField = UITheme.createTextField();
        editCard.add(accuracyField);
        editCard.add(Box.createVerticalStrut(10));

        // Frequency
        editCard.add(createFieldLabel("Update Frequency:"));
        String[] freqs = {"Real-time", "5 min", "10 min", "15 min", "1 Hour", "Daily"};
        freqCombo = new JComboBox<>(freqs);
        UITheme.styleComboBox(freqCombo);
        editCard.add(freqCombo);
        editCard.add(Box.createVerticalStrut(10));

        // Validation Status
        editCard.add(createFieldLabel("Validation Status:"));
        String[] statuses = {"CERTIFIED", "PROVISIONAL", "UNDER_REVIEW"};
        statusCombo = new JComboBox<>(statuses);
        UITheme.styleComboBox(statusCombo);
        editCard.add(statusCombo);
        editCard.add(Box.createVerticalStrut(10));

        // Confidence Level
        editCard.add(createFieldLabel("Confidence Tier:"));
        String[] levels = {"HIGH", "MEDIUM", "LOW"};
        levelCombo = new JComboBox<>(levels);
        UITheme.styleComboBox(levelCombo);
        editCard.add(levelCombo);
        editCard.add(Box.createVerticalStrut(20));

        // Save Button
        JButton saveBtn = UITheme.createPrimaryButton("Save Changes");
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        saveBtn.addActionListener(e -> saveRecalibration());
        editCard.add(saveBtn);

        content.add(editCard, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);

        loadCredibilityData();
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void saveRecalibration() {
        if (selectedSourceId == null) {
            JOptionPane.showMessageDialog(this, "Please click a row in the table to select a source.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            double rel = Double.parseDouble(reliabilityField.getText().trim());
            double acc = Double.parseDouble(accuracyField.getText().trim());
            String freq = (String) freqCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();
            String level = (String) levelCombo.getSelectedItem();

            SourceCredibility sc = new SourceCredibility(0, selectedSourceId, rel, acc, freq, status, level, null);
            boolean ok = DatabaseManager.getInstance().updateSourceCredibility(sc);
            if (ok) {
                AuditService.log("SOURCE_RECALIBRATED", "Source_Credibility", "Updated " + selectedSourceId + " to " + rel + "% reliability");
                JOptionPane.showMessageDialog(this, "Credibility scores for " + selectedSourceId + " successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCredibilityData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update database record.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Reliability and Accuracy must be decimal numbers (e.g. 95.5).", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadCredibilityData() {
        tableModel.setRowCount(0);
        List<SourceCredibility> list = DatabaseManager.getInstance().getAllSourceCredibility();
        for (SourceCredibility sc : list) {
            tableModel.addRow(new Object[]{
                    sc.getSourceId(),
                    String.format("%.1f%%", sc.getReliabilityScore()),
                    String.format("%.1f%%", sc.getHistoricalAccuracy()),
                    sc.getUpdateFrequency(),
                    sc.getValidationStatus(),
                    sc.getConfidenceLevel(),
                    sc.getLastEvaluated()
            });
        }
    }
}
