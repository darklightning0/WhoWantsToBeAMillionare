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
}
