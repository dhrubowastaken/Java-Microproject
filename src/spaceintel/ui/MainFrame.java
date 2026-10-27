package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.util.AuditService;
import spaceintel.util.SessionManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * CLASS: MainFrame.java
 * PURPOSE: Central Aerospace Control Station with Clean Professional Sidebar.
 * EXPLANATION FOR BEGINNERS:
 * 1. Role-Based Access Control (RBAC):
 *    - An Analyst sees only Analyst tools (Observation Entry, Credibility, Anomalies).
 *    - Non-admins NEVER see Admin panels (User Accounts, System Settings, Database Backup).
 * 2. Clean Professional UI:
 *    - No distracting emojis.
 *    - To switch roles, users cleanly use the Logout screen.
 * 3. Evaluator Mode:
 *    - A clean checkbox to expand all 20 screens during academic examination.
 * ============================================================================
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JPanel menuPanel;
    private JLabel userStatusLabel;
    private JLabel roleBadge;
    private final Map<String, JButton> navButtons = new HashMap<>();

    private String currentScreen = "Dashboard";

    // Child Screen Panels
    private DashboardView dashboardView;
    private DataSourceRegistrationView dataSourceView;
    private ObservationEntryView observationView;
    private SourceCredibilityView credibilityView;
    private ConfidenceAssessmentView confidenceView;
    private AnomalyDashboardView anomalyView;
    private EvidenceViewerView evidenceView;
    private RegionalMonitoringView regionalView;
    private IntelligenceDashboardView intelView;
    private AlertsView alertsView;
    private ReportsView reportsView;
    private UserManagementView userMgmtView;
    private SettingsView settingsView;
    private DatabaseBackupView backupView;
    private AboutSystemView aboutView;
    private AnalyticsDashboardView analyticsView;
    private ReportHistoryView reportHistoryView;
    private LogoutView logoutView;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Xovian Aerospace - Space Intelligence & Geospatial Data Reliability Station");
        setSize(1320, 840);
        setMinimumSize(new Dimension(1080, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root Container
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);

        // 1. Center Cards Container (Initialize all 20 screens)
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(UITheme.BG_DARK);

        dashboardView = new DashboardView(this);
        dataSourceView = new DataSourceRegistrationView();
        observationView = new ObservationEntryView();
        credibilityView = new SourceCredibilityView();
        confidenceView = new ConfidenceAssessmentView();
        anomalyView = new AnomalyDashboardView();
        evidenceView = new EvidenceViewerView();
        regionalView = new RegionalMonitoringView();
        intelView = new IntelligenceDashboardView();
        alertsView = new AlertsView();
        reportsView = new ReportsView();
        userMgmtView = new UserManagementView();
        settingsView = new SettingsView();
        backupView = new DatabaseBackupView();
        aboutView = new AboutSystemView();
        analyticsView = new AnalyticsDashboardView();
        reportHistoryView = new ReportHistoryView();
        logoutView = new LogoutView(this);

        cardContainer.add(dashboardView, "Dashboard");
        cardContainer.add(dataSourceView, "DataSources");
        cardContainer.add(observationView, "ObservationEntry");
        cardContainer.add(credibilityView, "Credibility");
        cardContainer.add(confidenceView, "Confidence");
        cardContainer.add(anomalyView, "AnomalyDashboard");
        cardContainer.add(evidenceView, "Evidence");
        cardContainer.add(regionalView, "RegionalMonitoring");
        cardContainer.add(intelView, "Intelligence");
        cardContainer.add(alertsView, "Alerts");
        cardContainer.add(reportsView, "Reports");
        cardContainer.add(userMgmtView, "UserManagement");
        cardContainer.add(settingsView, "Settings");
        cardContainer.add(backupView, "DatabaseBackup");
        cardContainer.add(aboutView, "AboutSystem");
        cardContainer.add(analyticsView, "Analytics");
        cardContainer.add(reportHistoryView, "ReportHistory");
        cardContainer.add(logoutView, "Logout");

        // 2. Left Sidebar (Clean Professional Menu)
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);
        root.add(cardContainer, BorderLayout.CENTER);

        setContentPane(root);

        // Build initial role-based menu and show Dashboard
        rebuildSidebarMenu();
        showScreen("Dashboard");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER_COLOR));

        // Top Brand Header
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(UITheme.SIDEBAR_BG);
        brandPanel.setBorder(new EmptyBorder(18, 16, 16, 16));

        JLabel brand1 = new JLabel("SPACE INTELLIGENCE");
        brand1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brand1.setForeground(UITheme.CYAN);

        JLabel brand2 = new JLabel("DATA RELIABILITY");
        brand2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        brand2.setForeground(UITheme.TEXT_PRIMARY);

        brandPanel.add(brand1);
        brandPanel.add(Box.createVerticalStrut(3));
        brandPanel.add(brand2);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Center Dynamic Menu Scroll Panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(UITheme.SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.setBackground(UITheme.SIDEBAR_BG);
        menuScroll.getViewport().setBackground(UITheme.SIDEBAR_BG);
        menuScroll.getVerticalScrollBar().setUnitIncrement(14);
        sidebar.add(menuScroll, BorderLayout.CENTER);

        // Bottom User Footer
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(new Color(11, 17, 32));
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(14, 14, 14, 14)
        ));

        userStatusLabel = new JLabel(SessionManager.getFullName());
        userStatusLabel.setFont(UITheme.FONT_BOLD);
        userStatusLabel.setForeground(UITheme.TEXT_PRIMARY);

        roleBadge = new JLabel("Role: " + SessionManager.getRole());
        roleBadge.setFont(UITheme.FONT_SMALL);
        roleBadge.setForeground(UITheme.CYAN);

        footer.add(userStatusLabel);
        footer.add(Box.createVerticalStrut(3));
        footer.add(roleBadge);

        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    /**
     * Dynamically rebuilds the navigation menu based on role permissions.
     * To switch roles or test another persona, the officer clicks "Logout" and logs in with that persona.
     */
    public void rebuildSidebarMenu() {
        menuPanel.removeAll();
        navButtons.clear();

        String role = SessionManager.getRole();
        boolean isAdmin = "Administrator".equalsIgnoreCase(role);

        // =====================================================================
        // GROUP 1: OPERATIONS
        // =====================================================================
        menuPanel.add(createCategoryLabel("OPERATIONS"));
        menuPanel.add(createNavButton("Dashboard", "Dashboard"));

        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Data Sources", "DataSources"));
        }
        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Disaster Management Officer".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Observation Entry", "ObservationEntry"));
        }
        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Intelligence Officer".equalsIgnoreCase(role) || "Disaster Management Officer".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Regional Monitor", "RegionalMonitoring"));
        }
        if (isAdmin || "Intelligence Officer".equalsIgnoreCase(role) || "Disaster Management Officer".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("System Alerts", "Alerts"));
        }

        menuPanel.add(Box.createVerticalStrut(10));

        // =====================================================================
        // GROUP 2: AI & RELIABILITY
        // =====================================================================
        menuPanel.add(createCategoryLabel("AI & RELIABILITY"));

        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Source Credibility", "Credibility"));
        }
        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Intelligence Officer".equalsIgnoreCase(role) || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("AI Confidence", "Confidence"));
        }
        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Disaster Management Officer".equalsIgnoreCase(role) || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Anomaly Scanner", "AnomalyDashboard"));
        }
        if (isAdmin || "Geospatial Analyst".equalsIgnoreCase(role) || "Intelligence Officer".equalsIgnoreCase(role) || "Disaster Management Officer".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Evidence & Explanations", "Evidence"));
        }
        if (isAdmin || "Intelligence Officer".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Intelligence Reports", "Intelligence"));
        }
        if (isAdmin || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Data Analytics", "Analytics"));
        }

        menuPanel.add(Box.createVerticalStrut(10));

        // =====================================================================
        // GROUP 3: REPORTS & ARCHIVE
        // =====================================================================
        menuPanel.add(createCategoryLabel("REPORTS"));
        menuPanel.add(createNavButton("Generate Reports", "Reports"));
        if (isAdmin || "Intelligence Officer".equalsIgnoreCase(role) || "Data Scientist".equalsIgnoreCase(role)) {
            menuPanel.add(createNavButton("Saved Reports", "ReportHistory"));
        }

        menuPanel.add(Box.createVerticalStrut(10));

        // =====================================================================
        // GROUP 4: ADMINISTRATION (STRICTLY RESTRICTED TO ADMINISTRATOR ONLY)
        // =====================================================================
        if (isAdmin) {
            menuPanel.add(createCategoryLabel("ADMINISTRATION"));
            menuPanel.add(createNavButton("User Accounts", "UserManagement"));
            menuPanel.add(createNavButton("System Settings", "Settings"));
            menuPanel.add(createNavButton("Database Backup", "DatabaseBackup"));
            menuPanel.add(Box.createVerticalStrut(10));
        }

        // =====================================================================
        // GROUP 5: SYSTEM
        // =====================================================================
        menuPanel.add(createCategoryLabel("SYSTEM"));
        menuPanel.add(createNavButton("About System", "AboutSystem"));
        menuPanel.add(createNavButton("Sign Out", "Logout"));

        menuPanel.revalidate();
        menuPanel.repaint();

        // Highlight current active button
        highlightActiveButton(currentScreen);
    }

    private JLabel createCategoryLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(UITheme.TEXT_DIM);
        l.setBorder(new EmptyBorder(6, 6, 4, 6));
        return l;
    }

    private JButton createNavButton(String text, String screenKey) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY);
        btn.setForeground(UITheme.TEXT_MUTED);
        btn.setBackground(UITheme.SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(7, 10, 7, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        btn.addActionListener(e -> showScreen(screenKey));

        navButtons.put(screenKey, btn);
        return btn;
    }

    private void highlightActiveButton(String screenKey) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton b = entry.getValue();
            if (entry.getKey().equalsIgnoreCase(screenKey)) {
                b.setForeground(Color.WHITE);
                b.setBackground(UITheme.CARD_BG);
                b.setFont(UITheme.FONT_BOLD);
            } else {
                b.setForeground(UITheme.TEXT_MUTED);
                b.setBackground(UITheme.SIDEBAR_BG);
                b.setFont(UITheme.FONT_BODY);
            }
        }
    }

    /**
     * Switches the view to the specified screen with security enforcement.
     */
    public void showScreen(String screenKey) {
        // Enforce Role-Based Access Control (RBAC)
        if (!SessionManager.isAdmin()) {
            if ("UserManagement".equalsIgnoreCase(screenKey) ||
                "Settings".equalsIgnoreCase(screenKey) ||
                "DatabaseBackup".equalsIgnoreCase(screenKey)) {

                JOptionPane.showMessageDialog(this,
                        "SECURITY CLEARANCE WARNING:\n" +
                        "Screen '" + screenKey + "' is restricted to Administrators.\n" +
                        "Current Persona: " + SessionManager.getRole() + "\n\n" +
                        "Please log in as Administrator to access this section.",
                        "Access Denied (RBAC Enforced)", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        currentScreen = screenKey;
        cardLayout.show(cardContainer, screenKey);
        highlightActiveButton(screenKey);

        // Screen refresh triggers
        if ("Dashboard".equalsIgnoreCase(screenKey)) {
            dashboardView.refreshData();
        } else if ("DataSources".equalsIgnoreCase(screenKey)) {
            dataSourceView.loadDataSources();
        } else if ("ObservationEntry".equalsIgnoreCase(screenKey)) {
            observationView.refreshDropdowns();
            observationView.loadObservationsTable();
        } else if ("Credibility".equalsIgnoreCase(screenKey)) {
            credibilityView.loadCredibilityData();
        } else if ("Confidence".equalsIgnoreCase(screenKey)) {
            confidenceView.loadAssessments();
        } else if ("AnomalyDashboard".equalsIgnoreCase(screenKey)) {
            anomalyView.loadAnomalies();
        } else if ("Evidence".equalsIgnoreCase(screenKey)) {
            evidenceView.loadEvidenceData();
        } else if ("RegionalMonitoring".equalsIgnoreCase(screenKey)) {
            regionalView.loadRegions();
        } else if ("Intelligence".equalsIgnoreCase(screenKey)) {
            intelView.loadReports();
        } else if ("Alerts".equalsIgnoreCase(screenKey)) {
            alertsView.loadAlerts();
        } else if ("UserManagement".equalsIgnoreCase(screenKey)) {
            userMgmtView.loadUsers();
        } else if ("Settings".equalsIgnoreCase(screenKey)) {
            settingsView.loadSettings();
        } else if ("DatabaseBackup".equalsIgnoreCase(screenKey)) {
            backupView.loadBackups();
        } else if ("Analytics".equalsIgnoreCase(screenKey)) {
            analyticsView.loadAnalytics();
        } else if ("ReportHistory".equalsIgnoreCase(screenKey)) {
            reportHistoryView.loadHistory();
        } else if ("Logout".equalsIgnoreCase(screenKey)) {
            logoutView.updateSessionDetails();
        }
    }
}
