package org.example.wwtbam.ui.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private final Color fill;
    private final int radius;

    public RoundedPanel(Color fill, int radius) {
        this.fill = fill;
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(fill);
        copy.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
