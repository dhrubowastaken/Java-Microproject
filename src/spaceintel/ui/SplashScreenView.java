package spaceintel.ui;

import spaceintel.db.DatabaseManager;
import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ============================================================================
 * SCREEN 1: SplashScreenView.java
 * PURPOSE: Animated mission startup screen displayed upon application launch.
 * EXPLANATION FOR BEGINNERS:
 * A splash screen provides a polished visual while background resources
 * (such as database tables and sample telemetry) are loaded into memory.
 * 
 * HOW IT WORKS (BEGINNER STEPS):
 * Step 1: Create a borderless centered window (JWindow).
 * Step 2: Render mission branding (Xovian Aerospace & Space Intelligence Centre).
 * Step 3: Run a background SwingWorker thread that updates a progress bar (0 to 100%).
 * Step 4: Once 100% is reached, close this splash and display Screen 2 (LoginView).
 * ============================================================================
 */
public class SplashScreenView extends JWindow {

    private JProgressBar progressBar;
    private JLabel statusLabel;

    public SplashScreenView() {
        initUI();
    }

    private void initUI() {
        // Step 1: Set size and center on screen
        setSize(560, 320);
        setLocationRelativeTo(null);

        // Step 2: Main container with dark aerospace styling
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(UITheme.BG_DARK);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UITheme.CYAN, 2),
                new EmptyBorder(25, 30, 25, 30)
        ));

        // Center Branding Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UITheme.BG_DARK);

        JLabel orgLabel = new JLabel("XOVIAN AEROSPACE");
        orgLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        orgLabel.setForeground(UITheme.CYAN);
        orgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("SPACE INTELLIGENCE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Geospatial Data Reliability Management System");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitleLabel.setForeground(UITheme.TEXT_MUTED);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel badge = new JLabel("Java Enterprise Micro Project 09");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(UITheme.AMBER_WARN);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(orgLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(subTitleLabel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(badge);

        // Bottom Progress & Status Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 8));
        bottomPanel.setBackground(UITheme.BG_DARK);

        statusLabel = new JLabel("Starting application...");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.TEXT_MUTED);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(UITheme.CYAN);
        progressBar.setBackground(UITheme.CARD_BG);
        progressBar.setBorder(new LineBorder(UITheme.BORDER_COLOR, 1));
        progressBar.setFont(UITheme.FONT_SMALL);
        progressBar.setPreferredSize(new Dimension(0, 18));

        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        container.add(centerPanel, BorderLayout.CENTER);
        container.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(container);
    }

    /**
     * Executes the simulated loading sequence using a SwingWorker thread.
     */
    public void startLoading(Runnable onComplete) {
        setVisible(true);

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Initialize Database in background (Module V)
                publish(20);
                statusLabel.setText("Connecting to database...");
                DatabaseManager.getInstance();
                Thread.sleep(300);

                publish(50);
                statusLabel.setText("Checking tables and loading initial data...");
                Thread.sleep(300);

                publish(80);
                statusLabel.setText("Initializing AI confidence and anomaly engines...");
                Thread.sleep(350);

                publish(100);
                statusLabel.setText("Ready! Opening login screen...");
                Thread.sleep(250);

                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int val = chunks.get(chunks.size() - 1);
                progressBar.setValue(val);
            }

            @Override
            protected void done() {
                dispose(); // Close splash
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        };

        worker.execute();
    }
}
