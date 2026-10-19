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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TrueFalseQuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;

    private final CircularTimer timerCircle = new CircularTimer(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private final List<GameShowButton> answerButtons = new ArrayList<>();
    private final JLabel questionLabel;
    private final MoneyBox moneyBox;
    private final JokerPanel jokerPanel;

    private Round currentRound;
    private Consumer<String> onAnswerSubmitted;
    private Timer blinkTimer;
    private Timer revealTimer;
    private boolean answerLocked;
    private int secondsRemaining = QUESTION_TIME_SECONDS;

    public TrueFalseQuestionScreen() {
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

        JPanel answers = new JPanel(new GridLayout(1, 2, 12, 0));
        answers.setOpaque(false);
        answers.setPreferredSize(new Dimension(0, 62));
        answers.setMinimumSize(new Dimension(0, 62));
        answers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        for (String answer : new String[]{"True", "False"}) {
            GameShowButton answerButton = new GameShowButton(
                GameShowButton.formatWithShape(answer),
                UITheme.PANEL,
                UITheme.PANEL_LIGHT
            );
            answerButton.setPreferredSize(new Dimension(0, 62));
            answerButton.setMinimumSize(new Dimension(0, 62));
            answerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            answerButton.addActionListener(event -> handleAnswerSelection(answerButton));
            answerButtons.add(answerButton);
            answers.add(answerButton);
        }

        JPanel questionAndAnswers = new JPanel(new BorderLayout(0, 10));
        questionAndAnswers.setOpaque(false);
        questionAndAnswers.add(questionCard, BorderLayout.NORTH);
        questionAndAnswers.add(answers, BorderLayout.SOUTH);
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

        for (GameShowButton btn : answerButtons) {
            btn.setButtonState(GameShowButton.State.NORMAL);
            btn.setEnabled(true);
        }

        resetAndStart();
    }

    private void handleJokerUsed(Joker joker) {
        if (currentRound == null) {
            return;
        }

        if (joker.getName().contains("Switch")) {
            questionLabel.setText("<html><div style='text-align:center'>" + currentRound.getQuestion().getText() + "</div></html>");
            pauseTimerWhile(() -> JOptionPane.showMessageDialog(this, "The question has been switched to a new one!", "Switch Question", JOptionPane.INFORMATION_MESSAGE));
        } else if (joker.getName().contains("Audience")) {
            Map<String, Integer> votes = currentRound.getAudienceVotes();
            String formatted = votes.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue() + "%")
                .collect(Collectors.joining("\n"));
            pauseTimerWhile(() -> JOptionPane.showMessageDialog(this, "Audience Votes:\n" + formatted, "Ask the Audience", JOptionPane.INFORMATION_MESSAGE));
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

    private void handleAnswerSelection(GameShowButton clickedButton) {
        if (answerLocked || currentRound == null) {
            return;
        }
        answerLocked = true;
        timer.stop();

        for (GameShowButton button : answerButtons) {
            button.setCursor(Cursor.getDefaultCursor());
        }

        String chosenAnswer = clickedButton.getLabelText();

        startBlinking(clickedButton, () -> {
            boolean isCorrect = currentRound.getQuestion().isCorrect(chosenAnswer);

            GameShowButton correctButton = null;
            for (GameShowButton button : answerButtons) {
                if (currentRound.getQuestion().isCorrect(button.getLabelText())) {
                    correctButton = button;
                    break;
                }
            }

            if (correctButton != null) {
                correctButton.setButtonState(GameShowButton.State.CORRECT);
            }

            if (!isCorrect) {
                clickedButton.setButtonState(GameShowButton.State.WRONG);
            }

            revealTimer = new Timer(2000, event -> {
                if (onAnswerSubmitted != null) {
                    onAnswerSubmitted.accept(chosenAnswer);
                }
            });
            revealTimer.setRepeats(false);
            revealTimer.start();
        });
    }

    private void startBlinking(GameShowButton button, Runnable onComplete) {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }

        final int interval = 500;
        final int totalTicks = 6;
        final int[] tickCount = {0};

        button.setButtonState(GameShowButton.State.BLINK_ON);

        blinkTimer = new Timer(interval, event -> {
            tickCount[0]++;
            if (tickCount[0] >= totalTicks) {
                blinkTimer.stop();
                onComplete.run();
            } else {
                boolean isOn = (tickCount[0] % 2 == 0);
                button.setButtonState(isOn ? GameShowButton.State.BLINK_ON : GameShowButton.State.BLINK_OFF);
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

        for (GameShowButton button : answerButtons) {
            button.setCursor(Cursor.getDefaultCursor());
            if (currentRound.getQuestion().isCorrect(button.getLabelText())) {
                button.setButtonState(GameShowButton.State.CORRECT);
            }
        }

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
        for (GameShowButton button : answerButtons) {
            button.setButtonState(GameShowButton.State.NORMAL);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        secondsRemaining = QUESTION_TIME_SECONDS;
        timerCircle.setSecondsRemaining(secondsRemaining);
        timer.start();
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
