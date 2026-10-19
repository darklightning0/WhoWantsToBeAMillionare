package org.example.wwtbam.ui.screens;

import org.example.wwtbam.ui.UITheme;
import org.example.wwtbam.ui.components.BackgroundPanel;
import org.example.wwtbam.ui.components.RoundedPanel;
import org.example.wwtbam.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public final class EndScreen extends JPanel {
    private final JLabel heading;
    private final JLabel result;
    private final JLabel score;

    public EndScreen(Runnable onPlayAgain) {
        this(onPlayAgain, InBetweenScreen.PRIZES.length >= 3 ? InBetweenScreen.PRIZES[2] : "€300");
    }

    public EndScreen(Runnable onPlayAgain, String finalPrize) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        RoundedPanel content = new RoundedPanel(UITheme.PANEL, 28);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(35, 65, 40, 65));

        heading = UITheme.title("Game Over");
        result = UITheme.subtitle("Your final score");
        score = UITheme.title(finalPrize);
        score.setForeground(UITheme.GOLD);
        StyledButton playAgainButton = UITheme.primaryButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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

    public void setResults(String titleText, String subtitleText, String prizeAmount) {
        heading.setText(titleText);
        result.setText(subtitleText);
        score.setText(prizeAmount != null ? prizeAmount : "€0");
    }
}
