package com.example.ui.components;

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
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
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

    @Override
    protected void paintComponent(Graphics graphics) {
        int point = Math.min(24, getHeight() / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, getWidth() - point, getWidth(), getWidth() - point, point},
            new int[]{getHeight() / 2, 0, 0, getHeight() / 2, getHeight(), getHeight()},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(hovered ? hoverFill : fill);
        copy.fillPolygon(shape);
        copy.setColor(new Color(190, 199, 216));
        copy.setStroke(new BasicStroke(2f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
