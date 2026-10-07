package org.example.wwtbam.ui.components;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public final class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel() {
        URL resource = getClass().getResource("/background.jpg");
        if (resource == null) {
            throw new IllegalStateException("Missing UI resource: /background.jpg");
        }
        backgroundImage = new ImageIcon(resource).getImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D copy = (Graphics2D) graphics.create();
        copy.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        copy.setColor(new Color(3, 7, 30, 105));
        copy.fillRect(0, 0, getWidth(), getHeight());
        copy.dispose();
    }
}
