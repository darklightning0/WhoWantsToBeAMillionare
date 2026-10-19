package org.example.wwtbam.ui.screens;

import org.example.wwtbam.ui.UITheme;
import org.example.wwtbam.ui.JokerState;
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

public final class QuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;
    private static final String CORRECT_ANSWER = "Science";

    private final CircularTimer timerCircle = new CircularTimer(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private final List<GameShowButton> answerButtons = new ArrayList<>();
    private final Runnable onAnswerSelected;
    private final MoneyBox moneyBox;

    private Timer blinkTimer;
    private Timer revealTimer;
    private boolean answerLocked;
    private int secondsRemaining = QUESTION_TIME_SECONDS;

    public QuestionScreen(Runnable onAnswerSelected, JokerState jokerState) {
        this.onAnswerSelected = onAnswerSelected;
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout(0, 14));
        background.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));
        add(background, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);
        int currentQuestionNumber = 1;
        moneyBox = new MoneyBox(InBetweenScreen.PRIZES[currentQuestionNumber - 1]);
        topBar.add(moneyBox, BorderLayout.WEST);

        JPanel timerSide = new JPanel(new BorderLayout(8, 0));
        timerSide.setOpaque(false);
        timerSide.add(new JokerPanel(jokerState), BorderLayout.WEST);
        timerSide.add(timerCircle, BorderLayout.EAST);
        topBar.add(timerSide, BorderLayout.EAST);
        background.add(topBar, BorderLayout.NORTH);

        GameShowPanel questionCard = new GameShowPanel(UITheme.PANEL);
        questionCard.setLayout(new GridBagLayout());
        questionCard.setBorder(BorderFactory.createEmptyBorder(18, 55, 18, 55));
        questionCard.setPreferredSize(new Dimension(0, 106));
        questionCard.setMinimumSize(new Dimension(0, 106));
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        JLabel question = new JLabel(
            "<html><div style='text-align:center'>Which subject are you ready<br>to master?</div></html>",
            SwingConstants.CENTER
        );
        question.setForeground(UITheme.TEXT);
        question.setFont(new Font("SansSerif", Font.BOLD, 22));
        questionCard.add(question);

        JPanel answers = new JPanel(new GridLayout(2, 2, 12, 8));
        answers.setOpaque(false);
        answers.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        answers.setPreferredSize(new Dimension(0, 132));
        answers.setMinimumSize(new Dimension(0, 132));
        answers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 132));

        for (String answer : new String[]{"Science", "History", "Geography", "Literature"}) {
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

    private void handleAnswerSelection(GameShowButton clickedButton) {
        if (answerLocked) {
            return;
        }
        answerLocked = true;
        timer.stop();

        for (GameShowButton button : answerButtons) {
            button.setCursor(Cursor.getDefaultCursor());
        }

        // Blink yellow slower (500ms interval, 6 ticks = 3000ms = 3 seconds)
        startBlinking(clickedButton, () -> {
            GameShowButton correctButton = null;
            for (GameShowButton button : answerButtons) {
                if (CORRECT_ANSWER.equalsIgnoreCase(button.getLabelText())) {
                    correctButton = button;
                    break;
                }
            }

            if (correctButton != null) {
                correctButton.setButtonState(GameShowButton.State.CORRECT);
            }

            if (clickedButton != correctButton) {
                clickedButton.setButtonState(GameShowButton.State.WRONG);
            }

            revealTimer = new Timer(2000, event -> onAnswerSelected.run());
            revealTimer.setRepeats(false);
            revealTimer.start();
        });
    }

    private void startBlinking(GameShowButton button, Runnable onComplete) {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }

        final int interval = 500;
        final int totalTicks = 6; // 6 * 500ms = 3000ms (3 seconds)
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
        if (answerLocked) {
            return;
        }
        answerLocked = true;
        timer.stop();

        for (GameShowButton button : answerButtons) {
            button.setCursor(Cursor.getDefaultCursor());
            if (CORRECT_ANSWER.equalsIgnoreCase(button.getLabelText())) {
                button.setButtonState(GameShowButton.State.CORRECT);
            }
        }

        revealTimer = new Timer(2000, event -> onAnswerSelected.run());
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
