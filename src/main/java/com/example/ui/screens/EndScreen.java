package com.example.ui.screens;

import com.example.ui.UITheme;
import com.example.ui.components.BackgroundPanel;
import com.example.ui.components.RoundedPanel;
import com.example.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public final class EndScreen extends JPanel {
    public EndScreen(Runnable onPlayAgain) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        RoundedPanel content = new RoundedPanel(UITheme.PANEL, 28);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(35, 65, 40, 65));

        JLabel heading = UITheme.title("Game Over");
        JLabel result = UITheme.subtitle("Your final score");
        int currentScore = 6767;
        JLabel score = UITheme.title((String) Integer.toString(currentScore));
        score.setForeground(UITheme.GOLD);
        StyledButton playAgainButton = UITheme.primaryButton("Play Again");
        playAgainButton.addActionListener(event -> onPlayAgain.run());

        content.add(heading);
        content.add(Box.createVerticalStrut(12));
        content.add(result);
        content.add(Box.createVerticalStrut(6));
        content.add(score);
        content.add(Box.createVerticalStrut(28));
        content.add(playAgainButton);
        background.add(content);
        add(background, BorderLayout.CENTER);
    }
}
