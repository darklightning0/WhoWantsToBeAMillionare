package org.example.wwtbam.ui.screens;

import org.example.wwtbam.game.Round;
import org.example.wwtbam.joker.Joker;
import org.example.wwtbam.ui.UITheme;
import org.example.wwtbam.ui.components.BackgroundPanel;
import org.example.wwtbam.ui.components.CircularTimer;
import org.example.wwtbam.ui.components.GameShowButton;
import org.example.wwtbam.ui.components.GameShowPanel;
import org.example.wwtbam.ui.components.JokerPanel;
import org.example.wwtbam.ui.components.MoneyBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.util.List;
import java.util.function.Consumer;

public final class OpenInputQuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;

    private final CircularTimer timerCircle = new CircularTimer(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private final JTextField inputField = new JTextField();
    private final GameShowButton submitButton;
    private final JLabel feedbackLabel;
    private final JLabel questionLabel;
    private final MoneyBox moneyBox;
    private final JokerPanel jokerPanel;

    private Round currentRound;
    private Consumer<String> onAnswerSubmitted;
    private Timer blinkTimer;
    private Timer revealTimer;
    private boolean answerLocked;
    private int secondsRemaining = QUESTION_TIME_SECONDS;

    public OpenInputQuestionScreen() {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout(0, 14));
        background.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));
        add(background, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);
        moneyBox = new MoneyBox(InBetweenScreen.PRIZES[0]);
        topBar.add(moneyBox, BorderLayout.WEST);

        JPanel timerSide = new JPanel(new BorderLayout(8, 0));
        timerSide.setOpaque(false);
        jokerPanel = new JokerPanel();
        timerSide.add(jokerPanel, BorderLayout.WEST);
        timerSide.add(timerCircle, BorderLayout.EAST);
        topBar.add(timerSide, BorderLayout.EAST);
        background.add(topBar, BorderLayout.NORTH);

        GameShowPanel questionCard = new GameShowPanel(UITheme.PANEL);
        questionCard.setLayout(new GridBagLayout());
        questionCard.setBorder(BorderFactory.createEmptyBorder(18, 55, 18, 55));
        questionCard.setPreferredSize(new Dimension(0, 106));
        questionCard.setMinimumSize(new Dimension(0, 106));
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setForeground(UITheme.TEXT);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        questionCard.add(questionLabel);

        JPanel inputPanel = new JPanel(new BorderLayout(12, 0));
        inputPanel.setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(0, 62));
        inputPanel.setMinimumSize(new Dimension(0, 62));
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        inputField.setBackground(UITheme.PANEL);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(UITheme.GOLD);
        inputField.setFont(new Font("SansSerif", Font.BOLD, 18));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.GOLD, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        inputField.addActionListener(event -> handleAnswerSubmission());

        submitButton = new GameShowButton(
            GameShowButton.formatWithShape("Submit"),
            UITheme.PANEL,
            UITheme.PANEL_LIGHT
        );
        submitButton.setPreferredSize(new Dimension(200, 62));
        submitButton.setMinimumSize(new Dimension(200, 62));
        submitButton.setMaximumSize(new Dimension(200, 62));
        submitButton.addActionListener(event -> handleAnswerSubmission());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        feedbackLabel.setForeground(UITheme.TEXT);
        feedbackLabel.setPreferredSize(new Dimension(0, 28));

        JPanel inputSection = new JPanel(new BorderLayout(0, 8));
        inputSection.setOpaque(false);
        inputSection.add(inputPanel, BorderLayout.NORTH);
        inputSection.add(feedbackLabel, BorderLayout.SOUTH);

        JPanel questionAndAnswers = new JPanel(new BorderLayout(0, 10));
        questionAndAnswers.setOpaque(false);
        questionAndAnswers.add(questionCard, BorderLayout.NORTH);
        questionAndAnswers.add(inputSection, BorderLayout.SOUTH);
        background.add(questionAndAnswers, BorderLayout.SOUTH);

        timer = new Timer(1000, event -> tick());
        timer.setInitialDelay(1000);

        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (isShowing()) {
                    resetAndStart();
                } else {
                    stopAllTimers();
                }
            }
        });
    }

    public void setRound(Round round, int level, List<Joker> jokers, Consumer<String> onAnswerSubmitted) {
        this.currentRound = round;
        this.onAnswerSubmitted = onAnswerSubmitted;

        String prize = (level >= 1 && level <= InBetweenScreen.PRIZES.length)
            ? InBetweenScreen.PRIZES[level - 1]
            : "€100";
        moneyBox.setMoney(prize);

        jokerPanel.setJokers(round, jokers, this::handleJokerUsed);

        questionLabel.setText("<html><div style='text-align:center'>" + round.getQuestion().getText() + "</div></html>");

        feedbackLabel.setText(" ");
        feedbackLabel.setVisible(false);

        resetAndStart();
    }

    private void handleJokerUsed(Joker joker) {
        if (currentRound == null) {
            return;
        }

        if (joker.getName().contains("Letter") || joker.getName().contains("Digit")) {
            String hint = currentRound.getHint().orElse("");
            feedbackLabel.setText("<html><font color='#f5be3f'><b>" + hint + "</b></font></html>");
            feedbackLabel.setVisible(true);
            pauseTimerWhile(() -> JOptionPane.showMessageDialog(this, hint, "First Letter / Last Digit", JOptionPane.INFORMATION_MESSAGE));
        } else if (joker.getName().contains("Host")) {
            List<String> msgs = currentRound.getJokerMessages();
            String msg = msgs.isEmpty() ? "The host is silent." : msgs.get(msgs.size() - 1);
            pauseTimerWhile(() -> JOptionPane.showMessageDialog(this, msg, "Ask the Host", JOptionPane.INFORMATION_MESSAGE));
        } else if (joker.getName().contains("Friend")) {
            List<String> msgs = currentRound.getJokerMessages();
            String msg = msgs.isEmpty() ? "Your friend couldn't be reached." : msgs.get(msgs.size() - 1);
            pauseTimerWhile(() -> JOptionPane.showMessageDialog(this, msg, "Phone a Friend", JOptionPane.INFORMATION_MESSAGE));
        }
    }

    private void pauseTimerWhile(Runnable action) {
        boolean wasRunning = (timer != null && timer.isRunning());
        if (wasRunning) {
            timer.stop();
        }
        try {
            action.run();
        } finally {
            if (wasRunning && !answerLocked) {
                timer.start();
            }
        }
    }

    private void handleAnswerSubmission() {
        if (answerLocked || currentRound == null) {
            return;
        }
        answerLocked = true;
        timer.stop();

        inputField.setEditable(false);
        submitButton.setCursor(Cursor.getDefaultCursor());
        submitButton.setLabelText("Locked");

        String userInput = inputField.getText().trim();
        boolean isCorrect = currentRound.getQuestion().isCorrect(userInput);
        String correctAnswer = currentRound.getQuestion().getCorrectAnswerText();

        startBlinking(() -> {
            if (isCorrect) {
                submitButton.setLabelText("Correct!");
                submitButton.setButtonState(GameShowButton.State.CORRECT);
                inputField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.GREEN, 3),
                    BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
                inputField.setBackground(new Color(15, 45, 25));
                inputField.setForeground(new Color(160, 255, 180));
                feedbackLabel.setText("<html><font color='#22c55e'><b>CORRECT!</b></font></html>");
            } else {
                inputField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.RED, 3),
                    BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
                inputField.setBackground(new Color(55, 15, 20));
                inputField.setForeground(new Color(255, 170, 170));

                submitButton.setLabelText(correctAnswer);
                submitButton.setButtonState(GameShowButton.State.CORRECT);
                feedbackLabel.setText("<html><font color='#ef4444'>Incorrect! </font><font color='#22c55e'>Correct answer: </font><font color='#ffffff'><b>" + correctAnswer + "</b></font></html>");
            }
            feedbackLabel.setVisible(true);

            revealTimer = new Timer(2000, event -> {
                if (onAnswerSubmitted != null) {
                    onAnswerSubmitted.accept(userInput);
                }
            });
            revealTimer.setRepeats(false);
            revealTimer.start();
        });
    }

    private void startBlinking(Runnable onComplete) {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }

        final int interval = 500;
        final int totalTicks = 6;
        final int[] tickCount = {0};

        submitButton.setButtonState(GameShowButton.State.BLINK_ON);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.YELLOW, 3),
            BorderFactory.createEmptyBorder(9, 19, 9, 19)
        ));
        inputField.setBackground(new Color(42, 36, 18));

        blinkTimer = new Timer(interval, event -> {
            tickCount[0]++;
            if (tickCount[0] >= totalTicks) {
                blinkTimer.stop();
                onComplete.run();
            } else {
                boolean isOn = (tickCount[0] % 2 == 0);
                submitButton.setButtonState(isOn ? GameShowButton.State.BLINK_ON : GameShowButton.State.BLINK_OFF);
                if (isOn) {
                    inputField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.YELLOW, 3),
                        BorderFactory.createEmptyBorder(9, 19, 9, 19)
                    ));
                    inputField.setBackground(new Color(42, 36, 18));
                } else {
                    inputField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.GOLD, 2),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                    ));
                    inputField.setBackground(UITheme.PANEL);
                }
            }
        });
        blinkTimer.start();
    }

    private void tick() {
        secondsRemaining--;
        timerCircle.setSecondsRemaining(secondsRemaining);

        if (secondsRemaining <= 0) {
            handleTimeExpired();
        }
    }

    private void handleTimeExpired() {
        if (answerLocked || currentRound == null) {
            return;
        }
        answerLocked = true;
        timer.stop();

        inputField.setEditable(false);
        submitButton.setCursor(Cursor.getDefaultCursor());

        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.RED, 3),
            BorderFactory.createEmptyBorder(9, 19, 9, 19)
        ));
        inputField.setBackground(new Color(55, 15, 20));
        inputField.setForeground(new Color(255, 170, 170));

        String correctAnswer = currentRound.getQuestion().getCorrectAnswerText();
        submitButton.setLabelText(correctAnswer);
        submitButton.setButtonState(GameShowButton.State.CORRECT);

        feedbackLabel.setText("<html><font color='#ef4444'>Time's up! </font><font color='#22c55e'>Correct answer: </font><font color='#ffffff'><b>" + correctAnswer + "</b></font></html>");
        feedbackLabel.setVisible(true);

        revealTimer = new Timer(2000, event -> {
            if (onAnswerSubmitted != null) {
                onAnswerSubmitted.accept("");
            }
        });
        revealTimer.setRepeats(false);
        revealTimer.start();
    }

    private void resetAndStart() {
        stopAllTimers();
        answerLocked = false;
        inputField.setText("");
        inputField.setEditable(true);
        inputField.setBackground(UITheme.PANEL);
        inputField.setForeground(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.GOLD, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        submitButton.setLabelText("Submit");
        submitButton.setButtonState(GameShowButton.State.NORMAL);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        secondsRemaining = QUESTION_TIME_SECONDS;
        timerCircle.setSecondsRemaining(secondsRemaining);
        timer.start();

        SwingUtilities.invokeLater(inputField::requestFocusInWindow);
    }

    private void stopAllTimers() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }
        if (revealTimer != null && revealTimer.isRunning()) {
            revealTimer.stop();
        }
    }
}
