package com.shop.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Widgets — factory methods for consistently-styled Swing components.
 * Java concept: static factory methods, method reuse
 */
public class Widgets {

    // ── Styled button ─────────────────────────────────────────────────
    public static JButton button(String label, Color bg, Color fg) {
        JButton b = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()   ? bg.darker()
                           : getModel().isRollover() ? bg.darker()
                           : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(fg);
        b.setFont(Theme.FONT_BTN);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        return b;
    }

    public static JButton primaryBtn(String label) {
        return button(label, Theme.ACCENT, Color.WHITE);
    }
    public static JButton dangerBtn(String label) {
        return button(label, Theme.DANGER, Color.WHITE);
    }
    public static JButton ghostBtn(String label) {
        JButton b = button(label, Theme.BORDER, Theme.TEXT_DARK);
        b.setForeground(Theme.TEXT_MUTED);
        return b;
    }
    public static JButton outlineBtn(String label) {
        JButton b = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xEFF6FF) : Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Theme.ACCENT);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Theme.ACCENT);
        b.setFont(Theme.FONT_BTN);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return b;
    }

    // ── Styled text field ─────────────────────────────────────────────
    public static JTextField textField(String placeholder, int cols) {
        JTextField f = new JTextField(cols);
        styleTextField(f, placeholder);
        return f;
    }

    public static JPasswordField passwordField(String placeholder, int cols) {
        JPasswordField f = new JPasswordField(cols);
        styleTextField(f, placeholder);
        return f;
    }

    private static void styleTextField(JTextField f, String placeholder) {
        f.setBackground(Theme.BG_INPUT);
        f.setForeground(Theme.TEXT_DARK);
        f.setCaretColor(Theme.ACCENT);
        f.setFont(Theme.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Theme.TEXT_DARK); }
                f.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Theme.ACCENT, 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(Theme.TEXT_MUTED); }
                f.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Theme.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });
        f.setText(placeholder);
        f.setForeground(Theme.TEXT_MUTED);
    }

    // ── Card panel ────────────────────────────────────────────────────
    public static JPanel card(int radius) {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
    }

    // ── Label helpers ─────────────────────────────────────────────────
    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    // ── Divider ───────────────────────────────────────────────────────
    public static JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setBackground(Theme.BORDER);
        return sep;
    }
}
