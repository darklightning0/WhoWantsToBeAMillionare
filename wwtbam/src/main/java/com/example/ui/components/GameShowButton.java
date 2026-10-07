package com.example.ui.components;

import com.example.ui.UITheme;

import javax.swing.*;
import java.awt.*;

public final class GameShowButton extends JButton {
    private final Color fill;
    private final Color hoverFill;
    private boolean hovered;

    public GameShowButton(String text, Color fill, Color hoverFill) {
        super(text);
        this.fill = fill;
        this.hoverFill = hoverFill;
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 18));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(10, 36, 10, 36));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                hovered = false;
                repaint();
            }
        });
    }

    public static String formatWithShape(String text) {
        String goldHex = String.format("#%02x%02x%02x", UITheme.GOLD.getRed(), UITheme.GOLD.getGreen(), UITheme.GOLD.getBlue());
        return "<html><font color='" + goldHex + "'>◆</font>&nbsp;&nbsp;" + text + "</html>";
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        int point = Math.min(24, h / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, w - point, w, w - point, point},
            new int[]{h / 2, 0, 0, h / 2, h, h},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(hovered ? hoverFill : fill);
        copy.fillPolygon(shape);
        copy.setColor(UITheme.GOLD);
        copy.setStroke(new BasicStroke(2f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
