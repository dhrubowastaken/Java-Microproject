package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.User;
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
 * SCREEN 14: UserManagementView.java
 * PURPOSE: User Administration & Role-Based Access Control Panel (Module 1).
 * EXPLANATION FOR BEGINNERS:
 * In enterprise systems, administrators manage personnel credentials and roles.
 * Supported 5 Roles:
 * 1. Administrator
 * 2. Geospatial Analyst
 * 3. Intelligence Officer
 * 4. Disaster Management Officer
 * 5. Data Scientist
 * ============================================================================
 */
public class UserManagementView extends JPanel {

    private DefaultTableModel userTableModel;
    private JTextField usernameField, passwordField, nameField, deptField, emailField;
    private JComboBox<String> roleCombo;

    public UserManagementView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "User Management",
                "Create new user accounts, assign roles, and manage access permissions."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 20, 15, 20));

        // LEFT: Register User Form
        JPanel formCard = UITheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(340, 0));

        JLabel formTitle = new JLabel("Create New User");
        formTitle.setFont(UITheme.FONT_HEADER);
        formTitle.setForeground(UITheme.CYAN);

        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(15));

        // Username
        formCard.add(createMiniLabel("Username:"));
        usernameField = UITheme.createTextField();
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(8));

        // Password
        formCard.add(createMiniLabel("Password:"));
        passwordField = UITheme.createTextField();
        passwordField.setText("pass123");
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(8));

        // Full Name
        formCard.add(createMiniLabel("Full Name:"));
        nameField = UITheme.createTextField();
        formCard.add(nameField);
        formCard.add(Box.createVerticalStrut(8));

        // Role Dropdown
        formCard.add(createMiniLabel("Assigned Role:"));
        String[] roles = {
                "Administrator",
                "Geospatial Analyst",
                "Intelligence Officer",
                "Disaster Management Officer",
                "Data Scientist"
        };
        roleCombo = new JComboBox<>(roles);
        UITheme.styleComboBox(roleCombo);
        formCard.add(roleCombo);
        formCard.add(Box.createVerticalStrut(8));

        // Department
        formCard.add(createMiniLabel("Department:"));
        deptField = UITheme.createTextField();
        deptField.setText("Earth Observation Wing");
        formCard.add(deptField);
        formCard.add(Box.createVerticalStrut(8));

        // Email
        formCard.add(createMiniLabel("Email:"));
        emailField = UITheme.createTextField();
        emailField.setText("officer@xovian-space.gov.in");
        formCard.add(emailField);
        formCard.add(Box.createVerticalStrut(18));

        JButton addBtn = UITheme.createPrimaryButton("Create Account");
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        addBtn.addActionListener(e -> registerUser());
        formCard.add(addBtn);

        content.add(formCard, BorderLayout.WEST);

        // RIGHT: Users Table
        JPanel tableCard = UITheme.createCard();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UITheme.CARD_BG);
        JLabel title = new JLabel("User Accounts");
        title.setFont(UITheme.FONT_HEADER);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadUsers());

        tableHeader.add(title, BorderLayout.WEST);
        tableHeader.add(refreshBtn, BorderLayout.EAST);

        String[] cols = {"User ID", "Username", "Officer Name", "Role (RBAC)", "Department", "Email"};
        userTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(userTableModel);
        UITheme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.PANEL_BG);

        tableCard.add(tableHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);
        content.add(tableCard, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadUsers();
    }

    private JLabel createMiniLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);
        return l;
    }

    private void registerUser() {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();
        String n = nameField.getText().trim();
        String r = (String) roleCombo.getSelectedItem();
        String d = deptField.getText().trim();
        String em = emailField.getText().trim();

        if (u.isEmpty() || p.isEmpty() || n.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Username, Password, and Full Name.", "Validation Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User newUser = new User(0, u, p, n, r, d, em);
        boolean ok = DatabaseManager.getInstance().addUser(newUser);
        if (ok) {
            AuditService.log("USER_REGISTERED", "Users", "Created user " + u + " (" + r + ")");
            JOptionPane.showMessageDialog(this, "User " + u + " provisioned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText("");
            nameField.setText("");
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create user. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadUsers() {
        userTableModel.setRowCount(0);
        List<User> list = DatabaseManager.getInstance().getAllUsers();
        for (User u : list) {
            userTableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getRole(),
                    u.getDepartment(),
                    u.getEmail()
            });
        }
    }
}
