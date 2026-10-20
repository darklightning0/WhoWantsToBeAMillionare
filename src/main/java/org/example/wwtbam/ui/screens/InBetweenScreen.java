package org.example.wwtbam.ui.screens;

import org.example.wwtbam.joker.Joker;
import org.example.wwtbam.ui.UITheme;
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
    public static final String[] PRIZES = loadPrizesFromJson();

    private final JLabel label;
    private final JLabel score;
    private final JokerPanel jokersPanel;
    private final JPanel ladderRowsContainer;
    private int highlightedQuestion = 1;

    public InBetweenScreen(Runnable onContinue, Runnable onWithdraw) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout());

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel layout = new JPanel(new BorderLayout(36, 0));
        layout.setOpaque(false);
        layout.setBorder(BorderFactory.createEmptyBorder(20, 48, 16, 48));
        layout.setPreferredSize(new Dimension(900, 580));

        RoundedPanel content = new RoundedPanel(UITheme.PANEL, 28);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));

        label = UITheme.subtitle("CURRENT BANKED PRIZE");
        score = UITheme.title("€0");
        score.setForeground(UITheme.GOLD);
        JLabel question = UITheme.subtitle("What would you like to do?");
        StyledButton continueButton = UITheme.primaryButton("Continue");
        continueButton.addActionListener(event -> onContinue.run());
        StyledButton withdrawButton = UITheme.primaryButton("Withdraw");
        withdrawButton.addActionListener(event -> onWithdraw.run());
        Dimension buttonSize = new Dimension(210, 54);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setPreferredSize(buttonSize);
        continueButton.setMaximumSize(buttonSize);
        withdrawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        withdrawButton.setPreferredSize(buttonSize);
        withdrawButton.setMaximumSize(buttonSize);

        content.add(label);
        content.add(Box.createVerticalStrut(14));
        content.add(score);
        content.add(Box.createVerticalStrut(6));
        content.add(question);
        content.add(Box.createVerticalStrut(26));
        content.add(continueButton);
        content.add(Box.createVerticalStrut(14));
        content.add(withdrawButton);
        content.add(Box.createVerticalStrut(20));
        JLabel jokersLabel = UITheme.subtitle("JOKERS");
        jokersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(jokersLabel);
        content.add(Box.createVerticalStrut(6));

        jokersPanel = new JokerPanel();
        jokersPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(jokersPanel);

        RoundedPanel prizePanel = new RoundedPanel(new Color(7, 15, 52, 220), 24);
        prizePanel.setLayout(new BoxLayout(prizePanel, BoxLayout.Y_AXIS));
        prizePanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        prizePanel.setPreferredSize(new Dimension(300, 0));

        JLabel title = new JLabel("PRIZE LADDER");
        title.setForeground(UITheme.GOLD);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        prizePanel.add(title);
        prizePanel.add(Box.createVerticalStrut(12));

        ladderRowsContainer = new JPanel();
        ladderRowsContainer.setOpaque(false);
        ladderRowsContainer.setLayout(new BoxLayout(ladderRowsContainer, BoxLayout.Y_AXIS));
        prizePanel.add(ladderRowsContainer);

        rebuildPrizeLadder();

        layout.add(content, BorderLayout.CENTER);
        layout.add(prizePanel, BorderLayout.EAST);

        centerWrapper.add(layout);
        background.add(centerWrapper, BorderLayout.CENTER);

        JLabel attribution = UITheme.attributionLabel();
        attribution.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        background.add(attribution, BorderLayout.SOUTH);

        add(background, BorderLayout.CENTER);
    }

    public void setRoundInfo(int nextQuestionNumber, String bankedMoney, boolean isSafePoint, List<Joker> allJokers) {
        this.highlightedQuestion = nextQuestionNumber;
        if (isSafePoint) {
            label.setText("YOU HAVE REACHED A SAFE POINT");
        } else {
            label.setText("CURRENT BANKED PRIZE");
        }
        score.setText(bankedMoney != null ? bankedMoney : "€0");
        rebuildPrizeLadder();
        jokersPanel.setJokers(null, allJokers, null);
    }

    private void rebuildPrizeLadder() {
        ladderRowsContainer.removeAll();
        for (int index = PRIZES.length - 1; index >= 0; index--) {
            int questionNumber = index + 1;
            JPanel row = createPrizeRow(questionNumber, PRIZES[index]);
            ladderRowsContainer.add(row);
        }
        ladderRowsContainer.revalidate();
        ladderRowsContainer.repaint();
    }

    private JPanel createPrizeRow(int questionNumber, String amount) {
        JPanel row = new JPanel(new GridBagLayout());
        boolean isCurrent = (questionNumber == highlightedQuestion);
        row.setOpaque(isCurrent);
        if (isCurrent) {
            row.setBackground(new Color(213, 139, 22));
        }
        row.setBorder(BorderFactory.createEmptyBorder(1, 8, 1, 8));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setPreferredSize(new Dimension(240, 28));

        Color textColor;
        if (isCurrent) {
            textColor = Color.WHITE;
        } else if (questionNumber % 5 == 0) {
            textColor = UITheme.GOLD;
        } else if (questionNumber < highlightedQuestion) {
            textColor = new Color(130, 210, 150); // Completed question in soft green
        } else {
            textColor = UITheme.TEXT;
        }

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
