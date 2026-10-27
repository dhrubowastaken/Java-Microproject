package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.DataSource;
import spaceintel.util.AuditService;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ============================================================================
 * SCREEN 4: DataSourceRegistrationView.java
 * PURPOSE: Register and manage space and terrestrial observation assets (Module 2).
 * EXPLANATION FOR BEGINNERS:
 * Space Intelligence relies on multiple asset types:
 * - Earth Observation Satellites (Cartosat, Sentinel)
 * - Weather Radar & Satellites (INSAT-3DR, Doppler)
 * - Recon Drones (SkyScout, Heron)
 * - Ground GPS and Environmental Sensors
 * 
 * This screen allows analysts to register new assets and view active hardware.
 * ============================================================================
 */
public class DataSourceRegistrationView extends JPanel {

    private JTextField idField, nameField, orgField, coverageField;
    private JComboBox<String> typeCombo, statusCombo;
    private DefaultTableModel tableModel;

    public DataSourceRegistrationView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Data Sources",
                "Add, view, and manage satellites, weather stations, drones, and ground sensors."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Registration Form Card
        JPanel formCard = UITheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(340, 0));

        JLabel formTitle = new JLabel("Add New Data Source");
        formTitle.setFont(UITheme.FONT_HEADER);
        formTitle.setForeground(UITheme.CYAN);

        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(15));

        // Source ID
        formCard.add(createFieldLabel("Source ID:"));
        idField = UITheme.createTextField();
        idField.setText("SAT-RISAT2");
        formCard.add(idField);
        formCard.add(Box.createVerticalStrut(10));

        // Source Name
        formCard.add(createFieldLabel("Source Name:"));
        nameField = UITheme.createTextField();
        nameField.setText("RISAT-2BR1 Radar Recon");
        formCard.add(nameField);
        formCard.add(Box.createVerticalStrut(10));

        // Source Type
        formCard.add(createFieldLabel("Type:"));
        String[] types = {"Satellite", "Drone", "Weather Station", "GPS", "GIS", "Sensor"};
        typeCombo = new JComboBox<>(types);
        UITheme.styleComboBox(typeCombo);
        formCard.add(typeCombo);
        formCard.add(Box.createVerticalStrut(10));

        // Organization
        formCard.add(createFieldLabel("Organization / Agency:"));
        orgField = UITheme.createTextField();
        orgField.setText("ISRO");
        formCard.add(orgField);
        formCard.add(Box.createVerticalStrut(10));

        // Coverage Area
        formCard.add(createFieldLabel("Coverage Area:"));
        coverageField = UITheme.createTextField();
        coverageField.setText("Indo-Pacific Radar Sector");
        formCard.add(coverageField);
        formCard.add(Box.createVerticalStrut(10));

        // Status
        formCard.add(createFieldLabel("Status:"));
        String[] statuses = {"ACTIVE", "DEGRADED", "CALIBRATING", "OFFLINE"};
        statusCombo = new JComboBox<>(statuses);
        UITheme.styleComboBox(statusCombo);
        formCard.add(statusCombo);
        formCard.add(Box.createVerticalStrut(20));

        // Buttons
        JButton submitBtn = UITheme.createPrimaryButton("Add Data Source");
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        submitBtn.addActionListener(e -> registerSource());
        formCard.add(submitBtn);

        content.add(formCard, BorderLayout.WEST);

        // RIGHT: Assets List Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel listTitle = new JLabel("Registered Data Sources");
        listTitle.setFont(UITheme.FONT_HEADER);
        listTitle.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadDataSources());

        tableHeader.add(listTitle, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"Source ID", "Name", "Type", "Organization", "Coverage", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        UITheme.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        content.add(tableCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadDataSources();
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void registerSource() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String org = orgField.getText().trim();
        String cov = coverageField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || org.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in ID, Name, and Organization.", "Validation Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DataSource ds = new DataSource(id, name, type, org, cov, status);
        boolean ok = DatabaseManager.getInstance().addDataSource(ds);
        if (ok) {
            AuditService.log("SOURCE_REGISTRATION", "Data_Sources", "Added " + id + " (" + name + ")");
            JOptionPane.showMessageDialog(this, "Data Source " + id + " registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            idField.setText("");
            nameField.setText("");
            loadDataSources();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register. Source ID might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadDataSources() {
        tableModel.setRowCount(0);
        List<DataSource> list = DatabaseManager.getInstance().getAllDataSources();
        for (DataSource d : list) {
            tableModel.addRow(new Object[]{
                    d.getSourceId(),
                    d.getSourceName(),
                    d.getSourceType(),
                    d.getOrganization(),
                    d.getCoverageArea(),
                    d.getStatus()
            });
        }
    }
}
