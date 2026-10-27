package spaceintel.ui;

import spaceintel.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ============================================================================
 * SCREEN 17: AboutSystemView.java
 * PURPOSE: Project Overview, Academic Module Mapping & System Specs (Screen 17).
 * EXPLANATION FOR BEGINNERS:
 * When presenting to evaluators, this screen proves complete alignment with
 * all Java Enterprise curriculum modules:
 * - Module I: Java Fundamentals, Arrays, Strings, Input Validation, CSV/JSON
 * - Module II: OOP (Classes, Interfaces, Encapsulation, Polymorphism)
 * - Module III: Multithreading, Exception Handling, File I/O, Audit Logging
 * - Module IV: Collections (ArrayList, HashMap, Queue, Stack), Swing GUI
 * - Module V: JDBC, Database CRUD (16 Tables), REST/HTTP Telemetry Handlers
 * ============================================================================
 */
public class AboutSystemView extends JPanel {

    public AboutSystemView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = UITheme.createHeader(
                "About the System",
                "Project background, purpose, and Java curriculum syllabus mapping."
        );
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Mission Card
        JPanel missionCard = UITheme.createCard();
        missionCard.setLayout(new BoxLayout(missionCard, BoxLayout.Y_AXIS));

        JLabel mTitle = new JLabel("Project Background & Purpose");
        mTitle.setFont(UITheme.FONT_HEADER);
        mTitle.setForeground(UITheme.CYAN);

        JTextArea mText = new JTextArea(
                "Modern satellites and weather stations generate massive amounts of Earth Observation data every day. However, " +
                "conflicting satellite feeds, delayed updates, and sensor errors can lead " +
                "to poor operational decisions. This system collects feeds from multiple sensors, " +
                "checks each source's credibility, detects 5 common types of data errors, and calculates explainable confidence scores."
        );
        mText.setFont(UITheme.FONT_BODY);
        mText.setForeground(UITheme.TEXT_PRIMARY);
        mText.setBackground(UITheme.CARD_BG);
        mText.setLineWrap(true);
        mText.setWrapStyleWord(true);
        mText.setEditable(false);

        missionCard.add(mTitle);
        missionCard.add(Box.createVerticalStrut(8));
        missionCard.add(mText);
        content.add(missionCard);

        content.add(Box.createVerticalStrut(15));

        // Module Mapping Card
        JPanel mapCard = UITheme.createCard();
        mapCard.setLayout(new BoxLayout(mapCard, BoxLayout.Y_AXIS));

        JLabel mapTitle = new JLabel("Java Syllabus Coverage (Modules 1 - 5)");
        mapTitle.setFont(UITheme.FONT_HEADER);
        mapTitle.setForeground(UITheme.GREEN_SUCCESS);

        String mappingText =
                "• Module I – Java Concepts:\n" +
                "   Data validation, regex parsing, coordinate range enforcement, CSV/JSON/TXT report formatters.\n\n" +
                "• Module II – Object-Oriented Programming:\n" +
                "   Modular domain hierarchy (User, DataSource, Observation, Anomaly, Evidence, Region, Alert), encapsulation.\n\n" +
                "• Module III – Multithreading, File I/O & Exceptions:\n" +
                "   SwingWorker background threads in Splash & Anomaly Scan, Report file I/O, immutable Audit_Log trail.\n\n" +
                "• Module IV – Collections Framework & GUI Programming:\n" +
                "   ArrayList, Map<String, String>, 20 Java Swing GUI Screens, CardLayout, custom dark aerospace theme.\n\n" +
                "• Module V – Database Connectivity & Enterprise Systems:\n" +
                "   JDBC connectivity to 16 normalized tables (SQLite embedded out-of-the-box + MySQL schema.sql included).";

        JTextArea mapArea = new JTextArea(mappingText);
        mapArea.setFont(UITheme.FONT_BODY);
        mapArea.setForeground(UITheme.TEXT_PRIMARY);
        mapArea.setBackground(UITheme.CARD_BG);
        mapArea.setLineWrap(true);
        mapArea.setWrapStyleWord(true);
        mapArea.setEditable(false);

        mapCard.add(mapTitle);
        mapCard.add(Box.createVerticalStrut(8));
        mapCard.add(mapArea);
        content.add(mapCard);

        content.add(Box.createVerticalStrut(15));

        // Specs & Industry Footer
        JPanel specCard = UITheme.createCard();
        specCard.setLayout(new GridLayout(1, 3, 10, 10));

        specCard.add(createMiniSpec("INDUSTRY PARTNER", "Xovian Aerospace", UITheme.CYAN));
        specCard.add(createMiniSpec("OPERATIONAL COMMAND", "ABC Geospatial Centre", UITheme.BLUE_PRIMARY));
        specCard.add(createMiniSpec("JAVA PLATFORM", "Java 21 LTS SE / Swing", UITheme.AMBER_WARN));

        content.add(specCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createMiniSpec(String label, String value, Color c) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(UITheme.CARD_BG);
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_MUTED);

        JLabel v = new JLabel(value);
        v.setFont(UITheme.FONT_BOLD);
        v.setForeground(c);

        p.add(l, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }
}
