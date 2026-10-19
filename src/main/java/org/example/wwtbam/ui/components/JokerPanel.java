package org.example.wwtbam.ui.components;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.joker.Joker;

public final class JokerPanel extends JPanel {

    private final List<JButton> buttons = new ArrayList<>();
    private Round currentRound;
    private List<Joker> currentJokers = List.of();
    private Consumer<Joker> onJokerUsed;

    public JokerPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
    }

    public void setJokers(Round round, List<Joker> jokers, Consumer<Joker> onJokerUsed) {
        this.currentRound = round;
        this.currentJokers = (jokers != null) ? jokers : List.of();
        this.onJokerUsed = onJokerUsed;
        rebuild();
    }

    public void refresh() {
        rebuild();
    }

    private void rebuild() {
        removeAll();
        buttons.clear();

        for (Joker joker : currentJokers) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(62, 52));
            button.setMinimumSize(new Dimension(62, 52));
            button.setMaximumSize(new Dimension(62, 52));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(false);

            boolean isAvailable = (currentRound != null) && joker.isAvailableFor(currentRound);
            boolean isUsed = joker.isUsed();

            button.setEnabled(isAvailable);
            button.setToolTipText(isUsed ? joker.getName() + " (used)" : joker.getName() + " - " + joker.getDescription());

            String iconPath = isUsed ? joker.getUsedIconPath() : joker.getAvailableIconPath();
            URL resource = getClass().getResource(iconPath);
            if (resource != null) {
                Image image = new ImageIcon(resource).getImage();
                Image scaled = image.getScaledInstance(58, 48, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaled));
            } else {
                button.setText(joker.getName());
            }

            button.addActionListener(event -> {
                if (currentRound != null && joker.isAvailableFor(currentRound)) {
                    joker.use(currentRound);
                    rebuild();
                    if (onJokerUsed != null) {
                        onJokerUsed.accept(joker);
                    }
                }
            });

            buttons.add(button);
            add(button);
        }

        revalidate();
        repaint();
    }
}
