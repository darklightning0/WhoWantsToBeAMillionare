package com.example.ui.screens;

import com.example.ui.UITheme;
import com.example.ui.components.BackgroundPanel;
import com.example.ui.components.GameShowButton;
import com.example.ui.components.GameShowPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;

public final class QuestionScreen extends JPanel {
    private static final int QUESTION_TIME_SECONDS = 30;

    private final JLabel timerLabel = new JLabel("30", SwingConstants.CENTER);
    private final TimerProgressBar timerBar = new TimerProgressBar(QUESTION_TIME_SECONDS);
    private final Timer timer;
    private int secondsRemaining = QUESTION_TIME_SECONDS;
//JOKERS to be added
//More question formats to be added
//rn it directly tells you if your answer is correct or not but i want it to wait a little to create tension
//also i ll add some sfx for ambiance
    public QuestionScreen(Runnable onAnswerSelected) {
        setLayout(new BorderLayout());
        setOpaque(false);

        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout(0, 14));
        background.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));
        add(background, BorderLayout.CENTER);

        JPanel topBar = new JPanel(new BorderLayout(0, 5));
        topBar.setOpaque(false);
        int currentQuestionNumber = 1; // We gotta convert this to a dynamic one, maybe read from json???, this for placeholder kinda
        JLabel questionNumber = new JLabel("QUESTION " + currentQuestionNumber + " OF 15", SwingConstants.CENTER);
        questionNumber.setForeground(UITheme.MUTED_TEXT);
        questionNumber.setFont(new Font("SansSerif", Font.BOLD, 14));
        topBar.add(questionNumber, BorderLayout.WEST);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setOpaque(false);
        timerLabel.setBackground(new Color(8, 20, 56));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(3, 12, 3, 12));
        topBar.add(timerLabel, BorderLayout.EAST);

        timerBar.setValue(QUESTION_TIME_SECONDS);
        timerBar.setStringPainted(false);
        topBar.add(timerBar, BorderLayout.SOUTH);

        GameShowPanel questionCard = new GameShowPanel(UITheme.PANEL);
        questionCard.setLayout(new GridBagLayout());
        questionCard.setBorder(BorderFactory.createEmptyBorder(18, 55, 18, 55));
        questionCard.setPreferredSize(new Dimension(0, 106));
        questionCard.setMinimumSize(new Dimension(0, 106));
        questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));
        //This is just a placeholder as well for now just for testing yk
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
        for (String answer : new String[]{"◆  A: Science", "◆  B: History", "◆  C: Geography", "◆  D: Literature"}) {
            GameShowButton answerButton = new GameShowButton(answer, UITheme.PANEL, UITheme.PANEL_LIGHT);
            answerButton.setPreferredSize(new Dimension(0, 62));
            answerButton.setMinimumSize(new Dimension(0, 62));
            answerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            answerButton.addActionListener(event -> answerSelected(onAnswerSelected));
            answers.add(answerButton);
        }

        JPanel questionAndAnswers = new JPanel(new BorderLayout(0, 10));
        questionAndAnswers.setOpaque(false);
        JPanel timerAndQuestion = new JPanel(new BorderLayout(0, 10));
        timerAndQuestion.setOpaque(false);
        timerAndQuestion.add(topBar, BorderLayout.NORTH);
        timerAndQuestion.add(questionCard, BorderLayout.SOUTH);
        questionAndAnswers.add(timerAndQuestion, BorderLayout.NORTH);
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
        timerLabel.setText(String.valueOf(secondsRemaining));
        timerBar.setValue(secondsRemaining);

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
        timerLabel.setText(String.valueOf(secondsRemaining));
        timerBar.setValue(secondsRemaining);
        timer.start();
    }

    private static final class TimerProgressBar extends JProgressBar {
        private static final Color TRACK_COLOR = new Color(9, 19, 58);
        private static final Color BORDER_COLOR = new Color(190, 202, 225);

        private static Color progressColor(int value) {
            if (value <= 10) {
                return new Color(220, 10, 10);
            }
            if (value <= 20) {
                return new Color(253, 208, 23);
            }
            return new Color(10, 220, 50);
        }

        private TimerProgressBar(int maximum) {
            super(0, maximum);
            setValue(maximum);
            setOpaque(false);
            setBorder(null);
            setPreferredSize(new Dimension(0, 16));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D copy = (Graphics2D) graphics.create();
            copy.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int border = 1;
            int width = getWidth() - (border * 2);
            int height = getHeight() - (border * 2);
            int progressWidth = (int) Math.round(
                width * (getValue() / (double) getMaximum())
            );

            copy.setColor(TRACK_COLOR);
            copy.fillRect(border, border, width, height);

            copy.setColor(progressColor(getValue()));
            copy.fillRoundRect(
                border + 2,
                border + 2,
                Math.max(0, progressWidth - 4),
                Math.max(0, height - 4),
                5,
                5
            );

            copy.setColor(BORDER_COLOR);
            copy.setStroke(new BasicStroke(border));
            copy.drawRect(border, border, width, height);
            copy.dispose();
        }
    }
}
