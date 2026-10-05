package com.example.ui.components;

import javax.swing.*;
import java.awt.*;

public final class GameShowPanel extends JPanel {
    public GameShowPanel(Color fill) {
        setOpaque(false);
        setBackground(fill);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int point = Math.min(34, getHeight() / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, getWidth() - point, getWidth(), getWidth() - point, point},
            new int[]{getHeight() / 2, 0, 0, getHeight() / 2, getHeight(), getHeight()},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(getBackground());
        copy.fillPolygon(shape);
        copy.setColor(new Color(205, 194, 137));
        copy.setStroke(new BasicStroke(2.5f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
