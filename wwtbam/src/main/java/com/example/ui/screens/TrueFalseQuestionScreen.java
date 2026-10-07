package com.example.ui.screens;

import com.example.ui.UITheme;
import com.example.ui.JokerState;
import com.example.ui.components.BackgroundPanel;
import com.example.ui.components.CircularTimer;
import com.example.ui.components.GameShowButton;
import com.example.ui.components.GameShowPanel;
import com.example.ui.components.JokerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;

public final class TrueFalseQuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;

    private final CircularTimer timerCircle = new CircularTimer(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private int secondsRemaining = QUESTION_TIME_SECONDS;

    public TrueFalseQuestionScreen(Runnable onAnswerSelected, JokerState jokerState) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout(0, 14));
        background.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));
        add(background, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);
        int currentQuestionNumber = 2;
        JLabel questionMoney = new JLabel(InBetweenScreen.PRIZES[currentQuestionNumber - 1]);
        questionMoney.setForeground(UITheme.GOLD);
        questionMoney.setFont(new Font("SansSerif", Font.BOLD, 18));
        topBar.add(questionMoney, BorderLayout.WEST);

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
            "<html><div style='text-align:center'>The Great Wall of China is visible<br>from low Earth orbit with the naked eye.</div></html>",
            SwingConstants.CENTER
        );
        question.setForeground(UITheme.TEXT);
        question.setFont(new Font("SansSerif", Font.BOLD, 22));
        questionCard.add(question);

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
            answerButton.addActionListener(event -> answerSelected(onAnswerSelected));
            answers.add(answerButton);
        }

        JPanel questionAndAnswers = new JPanel(new BorderLayout(0, 10));
        questionAndAnswers.setOpaque(false);
        questionAndAnswers.add(questionCard, BorderLayout.NORTH);
        questionAndAnswers.add(answers, BorderLayout.SOUTH);
        background.add(questionAndAnswers, BorderLayout.SOUTH);

        timer = new Timer(1000, event -> tick(onAnswerSelected));
        timer.setInitialDelay(1000);
        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                startTimer();
            }
        });
    }

    private void tick(Runnable onAnswerSelected) {
        secondsRemaining--;
        timerCircle.setSecondsRemaining(secondsRemaining);

        if (secondsRemaining <= 0) {
            answerSelected(onAnswerSelected);
        }
    }

    private void answerSelected(Runnable onAnswerSelected) {
        timer.stop();
        onAnswerSelected.run();
    }

    private void startTimer() {
        secondsRemaining = QUESTION_TIME_SECONDS;
        timerCircle.setSecondsRemaining(secondsRemaining);
        timer.start();
    }
}
