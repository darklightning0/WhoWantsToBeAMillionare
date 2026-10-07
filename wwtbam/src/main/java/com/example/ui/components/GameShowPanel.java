package com.example.ui.components;

import com.example.ui.UITheme;

import javax.swing.*;
import java.awt.*;

public final class GameShowPanel extends JPanel {
    public GameShowPanel(Color fill) {
        setOpaque(false);
        setBackground(fill);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        int point = Math.min(34, h / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, w - point, w, w - point, point},
            new int[]{h / 2, 0, 0, h / 2, h, h},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(getBackground());
        copy.fillPolygon(shape);
        copy.setColor(UITheme.GOLD);
        copy.setStroke(new BasicStroke(2.5f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
