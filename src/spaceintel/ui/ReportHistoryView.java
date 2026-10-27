package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * SCREEN 19: ReportHistoryView.java
 * PURPOSE: Archive of Generated Intelligence Dossiers & Downloads (Screen 19).
 * EXPLANATION FOR BEGINNERS:
 * Table 16 (Report_History) maintains an immutable paper-trail of every report
 * downloaded by officers, including export format, user call-sign, and file path.
 * ============================================================================
 */
public class ReportHistoryView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    public ReportHistoryView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Saved Reports",
                "Browse previously generated reports and open them directly in your default text editor."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel title = new JLabel("Report Export History");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBox.setBackground(UITheme.CARD_BG);

        JButton openBtn = UITheme.createPrimaryButton("Open Selected File");
        openBtn.addActionListener(e -> openSelectedFile());

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadHistory());

        btnBox.add(openBtn);
        btnBox.add(refreshBtn);

        tableHeader.add(title, BorderLayout.WEST);
        tableHeader.add(btnBox, BorderLayout.EAST);

        String[] cols = {"Log ID", "Report Title", "Exported By", "Format", "File System Location", "Export Timestamp"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(tableCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadHistory();
    }

    private void openSelectedFile() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a report row to open.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String path = (String) tableModel.getValueAt(row, 4);
        try {
            File f = new File(path);
            if (f.exists()) {
                Desktop.getDesktop().open(f);
            } else {
                JOptionPane.showMessageDialog(this, "File not found at: " + path, "Missing File", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadHistory() {
        tableModel.setRowCount(0);
        List<Map<String, String>> list = DatabaseManager.getInstance().getReportHistory();
        for (Map<String, String> row : list) {
            tableModel.addRow(new Object[]{
                    row.get("id"),
                    row.get("type"),
                    row.get("author"),
                    row.get("format"),
                    row.get("path"),
                    row.get("time")
            });
        }
    }
}
