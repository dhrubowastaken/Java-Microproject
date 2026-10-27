package spaceintel.ui;

import spaceintel.util.AuditService;
import spaceintel.util.SessionManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ============================================================================
 * SCREEN 20: LogoutView.java
 * PURPOSE: Secure Session Termination & Access Sign-Off (Screen 20).
 * EXPLANATION FOR BEGINNERS:
 * Military and space intelligence consoles require explicit session termination.
 * When an officer clicks "Confirm Logout", this screen:
 * 1. Writes an audit entry to Table 14 (Audit_Log)
 * 2. Clears SessionManager state
 * 3. Closes MainFrame and re-opens Screen 2 (LoginView).
 * ============================================================================
 */
public class LogoutView extends JPanel {

    private final MainFrame parentFrame;
    private JLabel userLabel, roleLabel, deptLabel;

    public LogoutView(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "Sign Out",
                "End your current session safely and return to the login screen."
        );
        add(header, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(UITheme.BG_DARK);

        JPanel card = UITheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(440, 320));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(25, 30, 25, 30)
        ));

        JLabel title = new JLabel("Sign Out of Your Account");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("You will be returned to the login screen");
        sub.setFont(UITheme.FONT_SUBTITLE);
        sub.setForeground(UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        userLabel = new JLabel("User: " + SessionManager.getFullName());
        userLabel.setFont(UITheme.FONT_BOLD);
        userLabel.setForeground(UITheme.CYAN);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        roleLabel = new JLabel("Role: " + SessionManager.getRole());
        roleLabel.setFont(UITheme.FONT_BODY);
        roleLabel.setForeground(UITheme.TEXT_PRIMARY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        deptLabel = new JLabel("Status: Active Session");
        deptLabel.setFont(UITheme.FONT_SMALL);
        deptLabel.setForeground(UITheme.GREEN_SUCCESS);
        deptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(userLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(roleLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(deptLabel);
        card.add(Box.createVerticalStrut(25));

        // Action Buttons
        JButton logoutBtn = UITheme.createPrimaryButton("Log Out & Return to Login Screen");
        logoutBtn.setBackground(UITheme.AMBER_WARN);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.addActionListener(e -> executeLogout());

        JButton exitAppBtn = UITheme.createSecondaryButton("Exit Application");
        exitAppBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitAppBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        exitAppBtn.addActionListener(e -> System.exit(0));

        card.add(logoutBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(exitAppBtn);

        centerContainer.add(card);
        add(centerContainer, BorderLayout.CENTER);
    }

    public void updateSessionDetails() {
        userLabel.setText("User: " + SessionManager.getFullName());
        roleLabel.setText("Role: " + SessionManager.getRole());
    }

    private void executeLogout() {
        AuditService.log("USER_LOGOUT", "Session", "Session cleanly terminated by user");
        SessionManager.logout();

        // Close MainFrame and open LoginView
        parentFrame.dispose();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
