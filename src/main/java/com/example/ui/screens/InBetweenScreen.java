package com.example.ui.screens;

import com.example.ui.UITheme;
import com.example.ui.components.BackgroundPanel;
import com.example.ui.components.RoundedPanel;
import com.example.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public final class InBetweenScreen extends JPanel {
    public InBetweenScreen(Runnable onContinue) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        RoundedPanel content = new RoundedPanel(UITheme.PANEL, 28);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(35, 60, 40, 60));

        JLabel label = UITheme.subtitle("IS THAT YOU ALBERT EINSTEIN");//ill change this dont worry
        int currentScore = 6767;
        JLabel score = UITheme.title((String) Integer.toString(currentScore));
        score.setForeground(UITheme.GOLD);
        JLabel question = UITheme.subtitle("Withdraw or contine?");
        StyledButton continueButton = UITheme.primaryButton("Continue");
        continueButton.addActionListener(event -> onContinue.run());
        StyledButton withdrawButton = UITheme.primaryButton("Withdraw");
        withdrawButton.addActionListener(event -> onContinue.run());
        Dimension buttonSize = new Dimension(165, 58);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setPreferredSize(buttonSize);
        continueButton.setMaximumSize(buttonSize);
        withdrawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawButton.setPreferredSize(buttonSize);
        withdrawButton.setMaximumSize(buttonSize);

        content.add(label);
        content.add(Box.createVerticalStrut(10));
        content.add(score);
        content.add(Box.createVerticalStrut(10));
        content.add(question);
        content.add(Box.createVerticalStrut(28));
        content.add(continueButton);
        content.add(Box.createVerticalStrut(38));
        content.add(withdrawButton);
        background.add(content);
        add(background, BorderLayout.CENTER);
    }
}
