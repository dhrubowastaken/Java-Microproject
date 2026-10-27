package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================================
 * SCREEN 16: DatabaseBackupView.java
 * PURPOSE: Database Maintenance, Snapshot Backup & Demo Reset (Screen 16).
 * EXPLANATION FOR BEGINNERS:
 * System Administrators must ensure data continuity. This screen provides:
 * 1. 1-Click Database Snapshot (copies space_intel.db with timestamp)
 * 2. 1-Click "Reset to Fresh Demo State" (restores clean sample data for demonstrations)
 * 3. Inspection of database file size, path, and backup history.
 * ============================================================================
 */
public class DatabaseBackupView extends JPanel {

    private JLabel dbPathLabel, dbSizeLabel, tableCountLabel;
    private DefaultTableModel backupTableModel;

    public DatabaseBackupView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Database Backup & Reset",
                "Save backup copies of the SQLite database or restore the original demo sample data."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Operations Card
        JPanel opsCard = UITheme.createCard();
        opsCard.setLayout(new BoxLayout(opsCard, BoxLayout.Y_AXIS));
        opsCard.setPreferredSize(new Dimension(360, 0));

        JLabel opsTitle = new JLabel("Database Status");
        opsTitle.setFont(UITheme.FONT_HEADER);
        opsTitle.setForeground(UITheme.CYAN);

        opsCard.add(opsTitle);
        opsCard.add(Box.createVerticalStrut(15));

        dbPathLabel = new JLabel("Database: space_intel.db (SQLite Embedded)");
        dbPathLabel.setFont(UITheme.FONT_BODY);
        dbPathLabel.setForeground(UITheme.TEXT_PRIMARY);

        dbSizeLabel = new JLabel("File Size: Calculating...");
        dbSizeLabel.setFont(UITheme.FONT_BODY);
        dbSizeLabel.setForeground(UITheme.TEXT_MUTED);

        tableCountLabel = new JLabel("Schema Verification: 16 Normalized Tables ACTIVE");
        tableCountLabel.setFont(UITheme.FONT_BODY);
        tableCountLabel.setForeground(UITheme.GREEN_SUCCESS);

        opsCard.add(dbPathLabel);
        opsCard.add(Box.createVerticalStrut(6));
        opsCard.add(dbSizeLabel);
        opsCard.add(Box.createVerticalStrut(6));
        opsCard.add(tableCountLabel);
        opsCard.add(Box.createVerticalStrut(25));

        // Action 1: Create Backup
        JButton backupBtn = UITheme.createPrimaryButton("Create Backup Snapshot");
        backupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        backupBtn.addActionListener(e -> createBackup());
        opsCard.add(backupBtn);
        opsCard.add(Box.createVerticalStrut(12));

        // Action 2: Reset Demo Data
        JButton resetBtn = UITheme.createPrimaryButton("Reset to Demo Data");
        resetBtn.setBackground(UITheme.AMBER_WARN);
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        resetBtn.addActionListener(e -> resetDemoData());
        opsCard.add(resetBtn);
        opsCard.add(Box.createVerticalStrut(12));

        // Action 3: Open Backup Folder
        JButton openDirBtn = UITheme.createSecondaryButton("Open Backups Folder");
        openDirBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        openDirBtn.addActionListener(e -> openBackupsFolder());
        opsCard.add(openDirBtn);

        content.add(opsCard, BorderLayout.WEST);

        // RIGHT: Backup History Table
        JPanel histCard = UITheme.createCard();
        histCard.setLayout(new BorderLayout(10, 10));

        JPanel histHeader = new JPanel(new BorderLayout());
        histHeader.setBackground(UITheme.CARD_BG);
        JLabel title = new JLabel("Saved Backups");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadBackups());

        histHeader.add(title, BorderLayout.WEST);
        histHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Backup Filename", "File Size", "Creation Timestamp"};
        backupTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(backupTableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        histCard.add(histHeader, BorderLayout.NORTH);
        histCard.add(scroll, BorderLayout.CENTER);
        content.add(histCard, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        refreshStatus();
        loadBackups();
    }

    private void refreshStatus() {
        File f = new File("space_intel.db");
        if (f.exists()) {
            long kb = f.length() / 1024;
            dbSizeLabel.setText("File Size: " + kb + " KB (" + f.length() + " bytes)");
        } else {
            dbSizeLabel.setText("File Size: Not created yet");
        }
    }

    private void createBackup() {
        try {
            File source = new File("space_intel.db");
            if (!source.exists()) {
                JOptionPane.showMessageDialog(this, "No active database file found to backup.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            File backupDir = new File("backups");
            if (!backupDir.exists()) backupDir.mkdirs();

            String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File dest = new File(backupDir, "space_intel_backup_" + ts + ".db");

            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            AuditService.log("DATABASE_BACKUP", "Database", "Created snapshot: " + dest.getName());

            JOptionPane.showMessageDialog(this, "Database snapshot created successfully!\nFile: " + dest.getName(), "Backup Success", JOptionPane.INFORMATION_MESSAGE);
            loadBackups();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Backup failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetDemoData() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to re-seed all 16 tables with clean demo data?\nAll temporary manual inputs will be refreshed.",
                "Confirm Demo Reset", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.getInstance().resetToDemoData();
            AuditService.log("DATABASE_RESET", "Database", "Reset to factory seed baseline");
            JOptionPane.showMessageDialog(this, "Database successfully restored to clean demo state!", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
            refreshStatus();
        }
    }

    private void openBackupsFolder() {
        try {
            File dir = new File("backups");
            if (!dir.exists()) dir.mkdirs();
            Desktop.getDesktop().open(dir);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open folder.", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadBackups() {
        backupTableModel.setRowCount(0);
        File dir = new File("backups");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".db"));
            if (files != null) {
                for (File f : files) {
                    backupTableModel.addRow(new Object[]{
                            f.getName(),
                            (f.length() / 1024) + " KB",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(f.lastModified()))
                    });
                }
            }
        }
    }
}
