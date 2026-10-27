package spaceintel.ui;

import spaceintel.util.ReportExporter;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

/**
 * ============================================================================
 * SCREEN 13: ReportsView.java
 * PURPOSE: Intelligence & Reliability Report Generator (Module 9).
 * EXPLANATION FOR BEGINNERS:
 * Analysts frequently need to generate official dossiers in standard formats.
 * This screen supports generating all 6 required report types:
 * 1. Observation Report
 * 2. Source Reliability Report
 * 3. Confidence Report
 * 4. Anomaly Report
 * 5. Daily Intelligence Summary
 * 6. Monthly Performance Report
 * 
 * Supports exporting in CSV, JSON, and TXT formats with live in-app preview!
 * ============================================================================
 */
public class ReportsView extends JPanel {

    private JComboBox<String> reportTypeCombo;
    private JComboBox<String> formatCombo;
    private JTextArea previewArea;
    private JLabel statusLabel;
    private File lastGeneratedFile = null;

    public ReportsView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Generate Reports",
                "Create and export data summaries as TXT, CSV, or JSON files for offline analysis."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Configuration Panel
        JPanel configCard = UITheme.createCard();
        configCard.setLayout(new BoxLayout(configCard, BoxLayout.Y_AXIS));
        configCard.setPreferredSize(new Dimension(340, 0));

        JLabel configTitle = new JLabel("Report Options");
        configTitle.setFont(UITheme.FONT_HEADER);
        configTitle.setForeground(UITheme.CYAN);

        configCard.add(configTitle);
        configCard.add(Box.createVerticalStrut(15));

        // Report Type
        configCard.add(createMiniLabel("Select Report Type:"));
        String[] reportTypes = {
                "Daily Intelligence Summary",
                "Observation Report",
                "Source Reliability Report",
                "Confidence Report",
                "Anomaly Report",
                "Monthly Performance Report"
        };
        reportTypeCombo = new JComboBox<>(reportTypes);
        UITheme.styleComboBox(reportTypeCombo);
        configCard.add(reportTypeCombo);
        configCard.add(Box.createVerticalStrut(12));

        // File Format
        configCard.add(createMiniLabel("Select File Format:"));
        String[] formats = {"TXT (Text Document)", "CSV (Excel Spreadsheet)", "JSON (Data Object)"};
        formatCombo = new JComboBox<>(formats);
        UITheme.styleComboBox(formatCombo);
        configCard.add(formatCombo);
        configCard.add(Box.createVerticalStrut(20));

        // Generate Button
        JButton genBtn = UITheme.createPrimaryButton("Generate Report");
        genBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        genBtn.addActionListener(e -> generateReport());
        configCard.add(genBtn);
        configCard.add(Box.createVerticalStrut(10));

        // Open File Button
        JButton openBtn = UITheme.createSecondaryButton("Open Reports Folder");
        openBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        openBtn.addActionListener(e -> openReportFolder());
        configCard.add(openBtn);
        configCard.add(Box.createVerticalStrut(15));

        statusLabel = new JLabel("Status: Ready to generate");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.TEXT_MUTED);
        configCard.add(statusLabel);

        content.add(configCard, BorderLayout.WEST);

        // RIGHT: Live In-App Preview Panel
        JPanel previewCard = UITheme.createCard();
        previewCard.setLayout(new BorderLayout(10, 10));

        JLabel prevTitle = new JLabel("Report Preview");
        prevTitle.setFont(UITheme.FONT_HEADER);
        prevTitle.setForeground(UITheme.TEXT_PRIMARY);

        previewArea = new JTextArea("Click 'Generate Report' to view the content here.");
        previewArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        previewArea.setForeground(UITheme.TEXT_PRIMARY);
        previewArea.setBackground(UITheme.PANEL_BG);
        previewArea.setEditable(false);
        previewArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        previewCard.add(prevTitle, BorderLayout.NORTH);
        previewCard.add(new JScrollPane(previewArea), BorderLayout.CENTER);

        content.add(previewCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JLabel createMiniLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void generateReport() {
        String type = (String) reportTypeCombo.getSelectedItem();
        String rawFmt = (String) formatCombo.getSelectedItem();
        String fmt = rawFmt.substring(0, 3).toLowerCase(); // "txt", "csv", "jso" -> "json"
        if ("jso".equals(fmt)) fmt = "json";

        try {
            File f = ReportExporter.generateReport(type, fmt);
            lastGeneratedFile = f;
            statusLabel.setText("Saved to: " + f.getName());
            statusLabel.setForeground(UITheme.GREEN_SUCCESS);

            // Load preview text
            String content = new String(Files.readAllBytes(f.toPath()));
            previewArea.setText(content);
            previewArea.setCaretPosition(0);

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nFile: " + f.getAbsolutePath(),
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to generate report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void openReportFolder() {
        try {
            File dir = new File("reports");
            if (!dir.exists()) dir.mkdirs();
            Desktop.getDesktop().open(dir);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open reports directory.", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
