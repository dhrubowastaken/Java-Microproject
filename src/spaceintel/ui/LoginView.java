package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.model.User;
import spaceintel.util.AuditService;
import spaceintel.util.SessionManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ============================================================================
 * SCREEN 2: LoginView.java
 * PURPOSE: Secure Authentication & Role-Based Access Control Gate (Module 1).
 * EXPLANATION FOR BEGINNERS:
 * This window validates credentials against Table 1 (Users).
 * For ease during student presentations, we provide a "Quick Demo Fill" selector
 * so you can test all 5 required roles with a single click:
 * 1. Administrator
 * 2. Geospatial Analyst
 * 3. Intelligence Officer
 * 4. Disaster Management Officer
 * 5. Data Scientist
 * ============================================================================
 */
public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> quickRoleSelect;
    private JLabel errorLabel;

    public LoginView() {
        initUI();
    }

    private void initUI() {
        setTitle("Space Intelligence & Data Reliability System");
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Root container
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PANEL_BG);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Choose a demo role or enter your credentials to log in");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.CYAN);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // Main Card Form
        JPanel formCard = UITheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        // Quick Role Preset (Awesome for quick live demo)
        JLabel presetLbl = new JLabel("Quick Role Selector:");
        presetLbl.setFont(UITheme.FONT_SMALL);
        presetLbl.setForeground(UITheme.AMBER_WARN);

        String[] demoRoles = {
                "-- Select a Demo Role --",
                "Administrator (admin)",
                "Geospatial Analyst (analyst)",
                "Intelligence Officer (intel)",
                "Disaster Management Officer (disaster)",
                "Data Scientist (scientist)"
        };
        quickRoleSelect = new JComboBox<>(demoRoles);
        UITheme.styleComboBox(quickRoleSelect);
        quickRoleSelect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        quickRoleSelect.addActionListener(e -> {
            int idx = quickRoleSelect.getSelectedIndex();
            switch (idx) {
                case 1: usernameField.setText("admin"); passwordField.setText("admin123"); break;
                case 2: usernameField.setText("analyst"); passwordField.setText("analyst123"); break;
                case 3: usernameField.setText("intel"); passwordField.setText("intel123"); break;
                case 4: usernameField.setText("disaster"); passwordField.setText("disaster123"); break;
                case 5: usernameField.setText("scientist"); passwordField.setText("scientist123"); break;
            }
        });

        // Username
        JLabel uLbl = new JLabel("Username:");
        uLbl.setFont(UITheme.FONT_BODY);
        uLbl.setForeground(UITheme.TEXT_MUTED);

        usernameField = UITheme.createTextField();
        usernameField.setText("analyst"); // default
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Password
        JLabel pLbl = new JLabel("Password:");
        pLbl.setFont(UITheme.FONT_BODY);
        pLbl.setForeground(UITheme.TEXT_MUTED);

        passwordField = UITheme.createPasswordField();
        passwordField.setText("analyst123");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Error message banner
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL);
        errorLabel.setForeground(UITheme.RED_CRITICAL);

        // Login Button
        JButton loginBtn = UITheme.createPrimaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.addActionListener(e -> handleLogin());

        // Assemble Form
        formCard.add(presetLbl);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(quickRoleSelect);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(uLbl);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(usernameField);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(pLbl);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(passwordField);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(errorLabel);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(loginBtn);

        // Padding around form
        JPanel centerPad = new JPanel(new BorderLayout());
        centerPad.setBackground(UITheme.BG_DARK);
        centerPad.setBorder(new EmptyBorder(20, 30, 20, 30));
        centerPad.add(formCard, BorderLayout.CENTER);

        root.add(centerPad, BorderLayout.CENTER);
        setContentPane(root);
    }

    /**
     * Authenticates user against Table 1 (Users) and Table 13 (Login_Log).
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        DatabaseManager db = DatabaseManager.getInstance();
        User user = db.authenticate(username, password);

        if (user != null) {
            // Success: log to Table 13 (Login_Log) and Table 14 (Audit_Log)
            db.logLogin(username, user.getRole(), "SUCCESS");
            SessionManager.setCurrentUser(user);
            AuditService.log("USER_LOGIN", "Session", "Authenticated as " + user.getRole());

            // Open Main Aerospace Control Station
            SwingUtilities.invokeLater(() -> {
                new MainFrame().setVisible(true);
                dispose(); // Close login window
            });
        } else {
            // Failure: log to Table 13
            db.logLogin(username, "UNKNOWN", "FAILED_CREDENTIALS");
            errorLabel.setText("Invalid credentials. Try 'admin' / 'admin123'.");
        }
    }
}
