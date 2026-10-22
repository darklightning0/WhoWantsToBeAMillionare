package org.example.wwtbam.ui.screens;

import org.example.wwtbam.game.Round;
import org.example.wwtbam.joker.Joker;
import org.example.wwtbam.model.MultipleChoiceQuestion;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class QuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;

    private final CircularTimer timerCircle = new CircularTimer(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private final List<GameShowButton> answerButtons = new ArrayList<>();
    private final JLabel questionLabel;
    private final MoneyBox moneyBox;
    private final JokerPanel jokerPanel;
    private final org.example.wwtbam.ui.components.JokerFeedbackCard jokerFeedbackCard = new org.example.wwtbam.ui.components.JokerFeedbackCard();

    private Round currentRound;
    private Consumer<String> onAnswerSubmitted;
    private Timer blinkTimer;
    private Timer revealTimer;
    private boolean answerLocked;
    private int secondsRemaining = QUESTION_TIME_SECONDS;

    public QuestionScreen() {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout(0, 14));
        background.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 28));
        add(background, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);

        JPanel topLeft = new JPanel();
        topLeft.setOpaque(false);
        topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
        moneyBox = new MoneyBox(InBetweenScreen.PRIZES[0]);
        moneyBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        jokerFeedbackCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        topLeft.add(moneyBox);
        topLeft.add(Box.createVerticalStrut(8));
        topLeft.add(jokerFeedbackCard);
        topBar.add(topLeft, BorderLayout.WEST);

        JPanel timerWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        timerWrapper.setOpaque(false);
        JPanel timerSide = new JPanel(new BorderLayout(8, 0));
        timerSide.setOpaque(false);
        jokerPanel = new JokerPanel();
        timerSide.add(jokerPanel, BorderLayout.WEST);
        timerSide.add(timerCircle, BorderLayout.EAST);
        timerWrapper.add(timerSide);
        topBar.add(timerWrapper, BorderLayout.EAST);
        background.add(topBar, BorderLayout.NORTH);

        GameShowPanel questionCard = new GameShowPanel(UITheme.PANEL);
        questionCard.setLayout(new BorderLayout());
        questionCard.setBorder(BorderFactory.createEmptyBorder(12, 45, 12, 45));
        questionCard.setPreferredSize(new Dimension(0, 106));
        questionCard.setMinimumSize(new Dimension(0, 106));
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setForeground(UITheme.TEXT);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        questionCard.add(questionLabel, BorderLayout.CENTER);

        JPanel answers = new JPanel(new GridLayout(2, 2, 12, 8));
        answers.setOpaque(false);
        answers.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        answers.setPreferredSize(new Dimension(0, 132));
        answers.setMinimumSize(new Dimension(0, 132));
        answers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 132));

        for (int i = 0; i < 4; i++) {
            GameShowButton answerButton = new GameShowButton(
                "",
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

        JPanel bottomContainer = new JPanel(new BorderLayout(0, 8));
        bottomContainer.setOpaque(false);
        bottomContainer.add(questionAndAnswers, BorderLayout.CENTER);
        bottomContainer.add(UITheme.attributionLabel(), BorderLayout.SOUTH);
        background.add(bottomContainer, BorderLayout.SOUTH);

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

        jokerFeedbackCard.clear();
        jokerPanel.setJokers(round, jokers, this::handleJokerUsed);

        UITheme.formatQuestionText(questionLabel, round.getQuestion().getText());

        if (round.getQuestion() instanceof MultipleChoiceQuestion mcq) {
            List<String> options = mcq.getOptions();
            char[] letters = {'A', 'B', 'C', 'D'};
            for (int i = 0; i < answerButtons.size(); i++) {
                GameShowButton btn = answerButtons.get(i);
                if (i < options.size()) {
                    btn.setPrefix(letters[i] + ":");
                    btn.setLabelText(options.get(i));
                    btn.setButtonState(GameShowButton.State.NORMAL);
                    btn.setEnabled(true);
                    btn.setVisible(true);
                } else {
                    btn.setPrefix("");
                    btn.setVisible(false);
                }
            }
        }

        resetAndStart();
    }

    private void handleJokerUsed(Joker joker) {
        if (currentRound == null) {
            return;
        }

        if (joker.getName().contains("50:50")) {
            List<String> visible = currentRound.getVisibleOptions();
            for (GameShowButton btn : answerButtons) {
                if (!visible.contains(btn.getLabelText())) {
                    btn.setEnabled(false);
                    btn.setPrefix("");
                    btn.setLabelText("");
                }
            }
            jokerFeedbackCard.setFeedback("50:50 Joker", "Two incorrect options have been eliminated.");
        } else if (joker.getName().contains("Audience")) {
            Map<String, Integer> votes = currentRound.getAudienceVotes();
            Map<String, Integer> letterVotes = new LinkedHashMap<>();
            if (currentRound.getQuestion() instanceof MultipleChoiceQuestion mcq) {
                List<String> allOptions = mcq.getOptions();
                char[] letters = {'A', 'B', 'C', 'D'};
                for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                    int idx = allOptions.indexOf(entry.getKey());
                    String key = (idx >= 0 && idx < letters.length) ? String.valueOf(letters[idx]) : entry.getKey();
                    letterVotes.put(key, entry.getValue());
                }
            } else {
                letterVotes.putAll(votes);
            }
            jokerFeedbackCard.setAudienceFeedback("Ask the Audience", letterVotes);
        } else if (joker.getName().contains("Friend")) {
            List<String> msgs = currentRound.getJokerMessages();
            String msg = msgs.isEmpty() ? "Your friend couldn't be reached." : msgs.get(msgs.size() - 1);
            jokerFeedbackCard.setFeedback("Phone a Friend", msg);
        }
        revalidate();
        repaint();
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
