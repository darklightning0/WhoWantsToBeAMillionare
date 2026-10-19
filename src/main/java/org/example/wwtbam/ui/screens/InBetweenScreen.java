package org.example.wwtbam.ui.screens;

import org.example.wwtbam.ui.UITheme;
import org.example.wwtbam.ui.JokerState;
import org.example.wwtbam.ui.components.BackgroundPanel;
import org.example.wwtbam.ui.components.JokerPanel;
import org.example.wwtbam.ui.components.RoundedPanel;
import org.example.wwtbam.ui.components.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InBetweenScreen extends JPanel {
    private static final int CURRENT_QUESTION = 3;
    public static final String[] PRIZES = loadPrizesFromJson();

    public InBetweenScreen(Runnable onContinue, Runnable onWithdraw, JokerState jokerState) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        JPanel layout = new JPanel(new BorderLayout(36, 0));
        layout.setOpaque(false);
        layout.setBorder(BorderFactory.createEmptyBorder(30, 48, 30, 48));
        layout.setPreferredSize(new Dimension(900, 585));

        RoundedPanel content = new RoundedPanel(UITheme.PANEL, 28);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(42, 52, 42, 52));

        JLabel label = UITheme.subtitle("YOU HAVE REACHED A SAFE POINT");
        JLabel score = UITheme.title(PRIZES.length >= CURRENT_QUESTION ? PRIZES[CURRENT_QUESTION - 1] : "€6,767");
        score.setForeground(UITheme.GOLD);
        JLabel question = UITheme.subtitle("What would you like to do?");
        StyledButton continueButton = UITheme.primaryButton("Continue");
        continueButton.addActionListener(event -> onContinue.run());
        StyledButton withdrawButton = UITheme.primaryButton("Withdraw");
        withdrawButton.addActionListener(event -> onWithdraw.run());
        Dimension buttonSize = new Dimension(210, 58);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setPreferredSize(buttonSize);
        continueButton.setMaximumSize(buttonSize);
        withdrawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawButton.setPreferredSize(buttonSize);
        withdrawButton.setMaximumSize(buttonSize);

        content.add(label);
        content.add(Box.createVerticalStrut(18));
        content.add(score);
        content.add(Box.createVerticalStrut(8));
        content.add(question);
        content.add(Box.createVerticalStrut(34));
        content.add(continueButton);
        content.add(Box.createVerticalStrut(18));
        content.add(withdrawButton);
        content.add(Box.createVerticalStrut(24));
        JLabel jokersLabel = UITheme.subtitle("JOKERS");
        jokersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(jokersLabel);
        content.add(Box.createVerticalStrut(8));
        JokerPanel jokers = new JokerPanel(jokerState);
        jokers.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(jokers);

        RoundedPanel prizePanel = createPrizePanel();
        layout.add(content, BorderLayout.CENTER);
        layout.add(prizePanel, BorderLayout.EAST);
        background.add(layout);
        add(background, BorderLayout.CENTER);
    }

    private RoundedPanel createPrizePanel() {
        RoundedPanel prizePanel = new RoundedPanel(new Color(7, 15, 52, 220), 24);
        prizePanel.setLayout(new BoxLayout(prizePanel, BoxLayout.Y_AXIS));
        prizePanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        prizePanel.setPreferredSize(new Dimension(300, 0));

        JLabel title = new JLabel("PRIZE LADDER");
        title.setForeground(UITheme.GOLD);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        prizePanel.add(title);
        prizePanel.add(Box.createVerticalStrut(14));

        for (int index = PRIZES.length - 1; index >= 0; index--) {
            int questionNumber = index + 1;
            JPanel row = createPrizeRow(questionNumber, PRIZES[index]);
            prizePanel.add(row);
        }

        return prizePanel;
    }

    private JPanel createPrizeRow(int questionNumber, String amount) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(questionNumber == CURRENT_QUESTION);
        row.setBackground(new Color(213, 139, 22));
        row.setBorder(BorderFactory.createEmptyBorder(1, 8, 1, 8));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setPreferredSize(new Dimension(240, 28));

        Color textColor = questionNumber == CURRENT_QUESTION
            ? Color.WHITE
            : (questionNumber % 5 == 0 ? UITheme.GOLD : UITheme.TEXT);

        JLabel number = new JLabel(String.valueOf(questionNumber));
        number.setForeground(textColor);
        number.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel prize = new JLabel(amount);
        prize.setForeground(textColor);
        prize.setFont(new Font("SansSerif", Font.BOLD, 14));

        GridBagConstraints numberConstraints = new GridBagConstraints();
        numberConstraints.gridx = 0;
        numberConstraints.weightx = 0.35;
        numberConstraints.anchor = GridBagConstraints.CENTER;
        row.add(number, numberConstraints);

        GridBagConstraints prizeConstraints = new GridBagConstraints();
        prizeConstraints.gridx = 1;
        prizeConstraints.weightx = 0.65;
        prizeConstraints.anchor = GridBagConstraints.WEST;
        row.add(prize, prizeConstraints);
        return row;
    }

    private static String[] loadPrizesFromJson() {
        try (InputStream in = InBetweenScreen.class.getResourceAsStream("/constants.json")) {
            if (in != null) {
                String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                Pattern pattern = Pattern.compile("\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(text);
                List<String> list = new ArrayList<>();
                while (matcher.find()) {
                    String val = matcher.group(1);
                    if (!val.equals("prizes")) {
                        list.add(val);
                    }
                }
                if (!list.isEmpty()) {
                    return list.toArray(new String[0]);
                }
            }
        } catch (Exception ignored) {
        }
        return new String[]{
            "€100", "€200", "€300", "€500", "€1,000",
            "€2,000", "€4,000", "€8,000", "€16,000", "€32,000",
            "€64,000", "€125,000", "€250,000", "€500,000", "€1,000,000"
        };
    }
}
