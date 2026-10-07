package org.example.wwtbam.ui.screens;

import org.example.wwtbam.ui.UITheme;
import org.example.wwtbam.ui.components.BackgroundPanel;
import org.example.wwtbam.ui.components.RoundedPanel;
import org.example.wwtbam.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;

public final class StartScreen extends JPanel {
    public StartScreen(Runnable onStart) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());

        RoundedPanel content = new RoundedPanel(new Color(255, 255, 255, 0), 28);
        content.setBorder(BorderFactory.createEmptyBorder(34, 55, 38, 55));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = UITheme.title("Who Wants to Be");
        JLabel titleSecondLine = UITheme.title("a Millionaire?");
        titleSecondLine.setForeground(UITheme.GOLD);

        StyledButton startButton = UITheme.primaryButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(event -> onStart.run());

        content.add(Box.createVerticalStrut(12));
        content.add(title);
        content.add(titleSecondLine);
        content.add(Box.createVerticalStrut(12));
        content.add(UITheme.subtitle("By Alexander Ivanov and Efe Kapukulak"));
        content.add(Box.createVerticalStrut(30));
        content.add(startButton);

        background.add(content);
        add(background, BorderLayout.CENTER);
    }
}
