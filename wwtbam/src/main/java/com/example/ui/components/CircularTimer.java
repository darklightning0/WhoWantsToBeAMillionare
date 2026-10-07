package com.example.ui.components;

import javax.swing.*;
import java.awt.*;

public final class CircularTimer extends JComponent {
    private static final Color TRACK_COLOR = new Color(9, 19, 58, 210);
    private static final Color BORDER_COLOR = new Color(190, 202, 225);
    private final int maximumSeconds;
    private int secondsRemaining;

    public CircularTimer(int maximumSeconds) {
        this.maximumSeconds = maximumSeconds;
        this.secondsRemaining = maximumSeconds;
        setPreferredSize(new Dimension(76, 76));
        setMinimumSize(new Dimension(76, 76));
        setMaximumSize(new Dimension(76, 76));
        setToolTipText("Time remaining");
    }

    public void setSecondsRemaining(int secondsRemaining) {
        this.secondsRemaining = Math.max(0, Math.min(secondsRemaining, maximumSeconds));
        repaint();
    }

    private Color progressColor() {
        if (secondsRemaining <= 10) {
            return new Color(220, 10, 10);
        }
        if (secondsRemaining <= 20) {
            return new Color(253, 208, 23);
        }
        return new Color(10, 220, 50);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int strokeWidth = 7;
        int diameter = Math.min(getWidth(), getHeight()) - strokeWidth - 2;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        copy.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        copy.setColor(TRACK_COLOR);
        copy.drawArc(x, y, diameter, diameter, 90, -360);

        int remainingAngle = (int) Math.round(
            360.0 * secondsRemaining / maximumSeconds
        );
        copy.setColor(progressColor());
        copy.drawArc(x, y, diameter, diameter, 90, remainingAngle);

        String text = String.valueOf(secondsRemaining);
        copy.setFont(new Font("SansSerif", Font.BOLD, 18));
        FontMetrics metrics = copy.getFontMetrics();
        int textX = (getWidth() - metrics.stringWidth(text)) / 2;
        int textY = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        copy.setColor(Color.WHITE);
        copy.drawString(text, textX, textY);

        copy.setStroke(new BasicStroke(1));
        copy.setColor(BORDER_COLOR);
        copy.drawOval(x + 2, y + 2, diameter - 4, diameter - 4);
        copy.dispose();
    }
}
