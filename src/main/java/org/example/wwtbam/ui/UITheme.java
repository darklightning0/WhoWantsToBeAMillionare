package org.example.wwtbam.ui;

import org.example.wwtbam.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public final class UITheme {
    public static final Color NAVY = new Color(7, 12, 42);
    public static final Color PANEL = new Color(13, 27, 78, 232);
    public static final Color PANEL_LIGHT = new Color(30, 56, 125, 238);
    public static final Color BLUE = new Color(32, 119, 190);
    public static final Color BLUE_HOVER = new Color(50, 200, 200);
    public static final Color GOLD = new Color(245, 190, 63);
    public static final Color RED = new Color(220, 10, 10);
    public static final Color GREEN = new Color(30, 175, 65);
    public static final Color YELLOW = new Color(245, 175, 25);
    public static final Color TEXT = new Color(245, 247, 255);
    public static final Color MUTED_TEXT = new Color(185, 198, 226);

    private UITheme() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 34));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED_TEXT);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static StyledButton primaryButton(String text) {
        StyledButton button = new StyledButton(text, BLUE, BLUE_HOVER);
        button.setFont(new Font("SansSerif", Font.BOLD, 17));
        return button;
    }

    public static StyledButton secondaryButton(String text) {
        StyledButton button = new StyledButton(text, PANEL_LIGHT, BLUE);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        return button;
    }

    public static JLabel attributionLabel() {
        JLabel label = new JLabel(
            "<html>The questions come from the <font color='#8da8d6'><u>Open Trivia Database</u></font> (https://opentdb.com), licensed under <font color='#8da8d6'><u>CC BY-SA 4.0</u></font></html>",
            SwingConstants.CENTER
        );
        label.setForeground(new Color(145, 155, 180));
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new java.net.URI("https://opentdb.com"));
                    }
                } catch (Exception ignored) {
                }
            }
        });
        return label;
    }

    public static void formatQuestionText(JLabel label, String questionText) {
        int fontSize = (questionText.length() > 90) ? 17 : (questionText.length() > 60 ? 19 : 21);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setText("<html><table width='760' cellpadding='0' cellspacing='0'><tr><td align='center'>"
            + questionText
            + "</td></tr></table></html>");
    }
}

