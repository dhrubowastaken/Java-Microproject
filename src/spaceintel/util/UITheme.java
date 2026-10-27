package spaceintel.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * ============================================================================
 * CLASS: UITheme.java
 * PURPOSE: Central Styling & Design System for the Aerospace Control Station.
 * EXPLANATION FOR BEGINNERS:
 * Instead of hardcoding colors on every button and label across 20 screens,
 * we store all color constants, fonts, and styling helpers in this single class.
 * This guarantees a cohesive, sleek, modern dark aerospace command look with
 * maximum visual contrast and zero white bleed-through!
 * ============================================================================
 */
public class UITheme {

    // --- High-Contrast Dark Color Palette ---
    public static final Color BG_DARK        = new Color(11, 15, 25);    // #0B0F19 Deep space void
    public static final Color PANEL_BG       = new Color(19, 27, 46);    // #131B2E Elevated navy panel
    public static final Color CARD_BG        = new Color(30, 41, 59);    // #1E293B Elevated card
    public static final Color SIDEBAR_BG     = new Color(15, 23, 42);    // #0F172A Deep sidebar
    public static final Color BORDER_COLOR   = new Color(51, 65, 85);    // #334155 Subtle border
    public static final Color BORDER_ACTIVE  = new Color(56, 189, 248);  // #38BDF8 Sky Cyan border

    // --- Vibrant Accents & Telemetry Colors ---
    public static final Color CYAN           = new Color(56, 189, 248);  // #38BDF8 Vibrant Space Cyan
    public static final Color BLUE_PRIMARY   = new Color(37, 99, 235);   // #2563EB Command Royal Blue
    public static final Color GREEN_SUCCESS  = new Color(52, 211, 153);  // #34D399 Mint Green
    public static final Color AMBER_WARN     = new Color(251, 191, 36);  // #FBBF24 Bright Warning Amber
    public static final Color RED_CRITICAL   = new Color(248, 113, 113); // #F87171 Bright Coral Red
    public static final Color PURPLE_AI      = new Color(192, 132, 252); // #C084FC AI Violet

    // --- High-Contrast Typography Colors ---
    public static final Color TEXT_PRIMARY   = new Color(255, 255, 255); // Pure Crisp White
    public static final Color TEXT_MUTED     = new Color(203, 213, 225); // Slate Light Muted (High readability)
    public static final Color TEXT_DIM       = new Color(148, 163, 184); // Dim Subtext

    // --- Fonts ---
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER     = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD       = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_METRIC_VAL = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Initializes FlatDarkLaf and global dark properties for all standard Swing widgets.
     */
    public static void setupGlobalTheme() {
        try {
            // Attempt to load modern FlatDarkLaf
            Class<?> flatDarkClass = Class.forName("com.formdev.flatlaf.FlatDarkLaf");
            flatDarkClass.getMethod("setup").invoke(null);
            System.out.println("[UITheme] FlatDarkLaf modern dark look & feel activated!");
        } catch (Throwable t) {
            System.out.println("[UITheme] Using custom dark palette overrides.");
        }

        // Global UIManager defaults to guarantee deep dark components everywhere
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.background", PANEL_BG);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", CYAN);
        UIManager.put("PasswordField.background", PANEL_BG);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", CYAN);
        UIManager.put("TextArea.background", PANEL_BG);
        UIManager.put("TextArea.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", PANEL_BG);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("Table.background", PANEL_BG);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER_COLOR);
        UIManager.put("TableHeader.background", CARD_BG);
        UIManager.put("TableHeader.foreground", CYAN);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Viewport.background", BG_DARK);
        UIManager.put("TabbedPane.background", PANEL_BG);
        UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
        UIManager.put("TabbedPane.selectedForeground", CYAN);
        UIManager.put("OptionPane.background", PANEL_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
    }

    /**
     * Creates a standard screen header panel with title and mission description.
     */
    public static JPanel createHeader(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL_BG);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_TITLE);
        titleLbl.setForeground(TEXT_PRIMARY);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_SUBTITLE);
        subLbl.setForeground(CYAN);

        p.add(titleLbl, BorderLayout.NORTH);
        p.add(subLbl, BorderLayout.SOUTH);
        return p;
    }

    /**
     * Creates an elevated card panel with rounded border appearance.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return card;
    }

    /**
     * Creates a high-impact metric KPI card (used in dashboards).
     */
    public static JPanel createMetricCard(String title, String value, String unit, Color accent) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(5, 5));

        JLabel titleLbl = new JLabel(title.toUpperCase());
        titleLbl.setFont(FONT_SMALL);
        titleLbl.setForeground(TEXT_MUTED);

        JLabel valLbl = new JLabel(value + " " + (unit != null ? unit : ""));
        valLbl.setFont(FONT_METRIC_VAL);
        valLbl.setForeground(accent != null ? accent : CYAN);

        JPanel topBar = new JPanel();
        topBar.setBackground(accent != null ? accent : CYAN);
        topBar.setPreferredSize(new Dimension(0, 3));

        card.add(topBar, BorderLayout.NORTH);
        card.add(titleLbl, BorderLayout.CENTER);
        card.add(valLbl, BorderLayout.SOUTH);
        return card;
    }

    /**
     * Creates a primary aerospace action button.
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(BLUE_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Creates a secondary/cancel button.
     */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(CARD_BG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Styles a standard text input field for dark mode.
     */
    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(PANEL_BG);
        tf.setCaretColor(CYAN);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(7, 10, 7, 10)
        ));
        return tf;
    }

    /**
     * Styles a password input field for dark mode.
     */
    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setForeground(TEXT_PRIMARY);
        pf.setBackground(PANEL_BG);
        pf.setCaretColor(CYAN);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(7, 10, 7, 10)
        ));
        return pf;
    }

    /**
     * Styles a JComboBox with custom dark renderer and high-contrast item list.
     */
    public static void styleComboBox(JComboBox<?> box) {
        box.setBackground(PANEL_BG);
        box.setForeground(TEXT_PRIMARY);
        box.setFont(FONT_BODY);
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(3, 5, 3, 5)
        ));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                lbl.setFont(FONT_BODY);
                lbl.setBorder(new EmptyBorder(6, 10, 6, 10));
                if (isSelected) {
                    lbl.setBackground(new Color(30, 58, 138));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(PANEL_BG);
                    lbl.setForeground(TEXT_PRIMARY);
                }
                return lbl;
            }
        });
    }

    /**
     * Styles a JTextArea with dark backgrounds and clean line borders.
     */
    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_BODY);
        area.setForeground(TEXT_PRIMARY);
        area.setBackground(PANEL_BG);
        area.setCaretColor(CYAN);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    /**
     * Styles a JTable with sleek alternating dark rows and high-contrast text.
     */
    public static void styleTable(JTable table) {
        table.setBackground(PANEL_BG);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(30);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(30, 58, 138)); // Deep indigo selection
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(CARD_BG);
        header.setForeground(CYAN);
        header.setFont(FONT_BOLD);
        header.setBorder(new LineBorder(BORDER_COLOR, 1));
        header.setPreferredSize(new Dimension(header.getWidth(), 34));

        // Alternating dark rows renderer with padding
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? PANEL_BG : new Color(25, 34, 54));
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(new Color(30, 58, 138));
                    c.setForeground(Color.WHITE);
                }
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(new EmptyBorder(2, 10, 2, 10));
                }
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);
    }
}
