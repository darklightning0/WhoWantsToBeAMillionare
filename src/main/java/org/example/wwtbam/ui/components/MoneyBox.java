package org.example.wwtbam.ui.components;

import org.example.wwtbam.ui.UITheme;

import javax.swing.*;
import java.awt.*;

public final class MoneyBox extends JPanel {
    private final JLabel label;

    public MoneyBox(String moneyText) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(160, 44));
        setMinimumSize(new Dimension(130, 44));
        setMaximumSize(new Dimension(190, 44));

        label = new JLabel();
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        label.setForeground(UITheme.GOLD);
        setMoney(moneyText);
        add(label);
    }

    public void setMoney(String moneyText) {
        String goldHex = String.format("#%02x%02x%02x", UITheme.GOLD.getRed(), UITheme.GOLD.getGreen(), UITheme.GOLD.getBlue());
        label.setText("<html><font color='" + goldHex + "'>◆</font>&nbsp;&nbsp;<font color='" + goldHex + "'><b>" + moneyText + "</b></font></html>");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        int point = Math.min(18, h / 2);
        Polygon shape = new Polygon(
            new int[]{0, point, w - point, w, w - point, point},
            new int[]{h / 2, 0, 0, h / 2, h, h},
            6
        );

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setColor(UITheme.PANEL);
        copy.fillPolygon(shape);
        copy.setColor(UITheme.GOLD);
        copy.setStroke(new BasicStroke(2f));
        copy.drawPolygon(shape);
        copy.dispose();
        super.paintComponent(graphics);
    }
}
