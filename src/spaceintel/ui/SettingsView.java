package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * ============================================================================
 * SCREEN 15: SettingsView.java
 * PURPOSE: Operational System Thresholds & Parameters Configuration (Screen 15).
 * EXPLANATION FOR BEGINNERS:
 * Real intelligence engines use configurable thresholds stored in Table 15 (System_Settings):
 * - Minimum AI confidence score to automatically approve decisions (e.g. 85%)
 * - Outlier sigma tolerance multiplier
 * - Sensor heartbeat timeout in seconds
 * Analysts can inspect and update parameters live from this screen.
 * ============================================================================
 */
public class SettingsView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField keyField, valueField;

    public SettingsView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "System Settings",
                "Adjust thresholds for AI confidence scoring, anomaly detection sensitivity, and alert triggers."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Edit Setting Form
        JPanel editCard = UITheme.createCard();
        editCard.setLayout(new BoxLayout(editCard, BoxLayout.Y_AXIS));
        editCard.setPreferredSize(new Dimension(340, 0));

        JLabel title = new JLabel("Edit Setting");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.CYAN);

        editCard.add(title);
        editCard.add(Box.createVerticalStrut(15));

        editCard.add(createMiniLabel("Setting Name:"));
        keyField = UITheme.createTextField();
        keyField.setEditable(false);
        editCard.add(keyField);
        editCard.add(Box.createVerticalStrut(10));

        editCard.add(createMiniLabel("Setting Value:"));
        valueField = UITheme.createTextField();
        editCard.add(valueField);
        editCard.add(Box.createVerticalStrut(20));

        JButton saveBtn = UITheme.createPrimaryButton("Save Setting");
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        saveBtn.addActionListener(e -> updateSetting());
        editCard.add(saveBtn);

        content.add(editCard, BorderLayout.WEST);

        // RIGHT: Settings Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel tblTitle = new JLabel("System Configuration Values");
        tblTitle.setFont(UITheme.FONT_HEADER);
        tblTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadSettings());

        tableHeader.add(tblTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Parameter Key", "Current Value"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                keyField.setText((String) tableModel.getValueAt(row, 0));
                valueField.setText((String) tableModel.getValueAt(row, 1));
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadSettings();
    }

    private JLabel createMiniLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void updateSetting() {
        String key = keyField.getText().trim();
        String val = valueField.getText().trim();

        if (key.isEmpty() || val.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a setting row to modify.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean ok = DatabaseManager.getInstance().updateSystemSetting(key, val);
        if (ok) {
            AuditService.log("SETTING_UPDATED", "System_Settings", key + " -> " + val);
            JOptionPane.showMessageDialog(this, "Setting '" + key + "' updated to '" + val + "'.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSettings();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update setting.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadSettings() {
        tableModel.setRowCount(0);
        Map<String, String> map = DatabaseManager.getInstance().getSystemSettings();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
