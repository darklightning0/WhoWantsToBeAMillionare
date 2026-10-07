package org.example.wwtbam.ui.components;

import org.example.wwtbam.ui.JokerState;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public final class JokerPanel extends JPanel {
    private static final String[] AVAILABLE_IMAGES = {
        "/joker_5050.png", "/joker_audience.png", "/joker_phone.png"
    };
    private static final String[] USED_IMAGES = {
        "/joker_5050_used.png", "/joker_audience_used.png", "/joker_phone_used.png"
    };
    private static final String[] TOOLTIPS = {
        "50:50", "Ask the Audience", "Phone a Friend"
    };

    private final JokerState state;
    private final JButton[] buttons = new JButton[3];

    public JokerPanel(JokerState state) {
        this.state = state;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));

        for (int index = 0; index < buttons.length; index++) {
            int jokerIndex = index;
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(62, 52));
            button.setMinimumSize(new Dimension(62, 52));
            button.setMaximumSize(new Dimension(62, 52));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setToolTipText(TOOLTIPS[index]);
            button.addActionListener(event -> useJoker(jokerIndex));
            buttons[index] = button;
            add(button);
            updateButton(index);
        }
    }

    private void useJoker(int index) {
        if (state.isUsed(index)) {
            return;
        }
        state.use(index);
        updateButton(index);
    }

    private void updateButton(int index) {
        String path = state.isUsed(index) ? USED_IMAGES[index] : AVAILABLE_IMAGES[index];
        URL resource = getClass().getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Missing joker resource: " + path);
        }

        Image image = new ImageIcon(resource).getImage();
        Image scaled = image.getScaledInstance(58, 48, Image.SCALE_SMOOTH);
        buttons[index].setIcon(new ImageIcon(scaled));
        buttons[index].setEnabled(!state.isUsed(index));
        buttons[index].setToolTipText(
            state.isUsed(index) ? TOOLTIPS[index] + " (used)" : TOOLTIPS[index]
        );
    }
}
