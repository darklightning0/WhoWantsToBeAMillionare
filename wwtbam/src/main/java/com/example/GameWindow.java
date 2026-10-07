package com.example;

import javax.swing.*;

import com.example.ui.ScreenManager;
import com.example.ui.JokerState;
import com.example.ui.screens.EndScreen;
import com.example.ui.screens.InBetweenScreen;
import com.example.ui.screens.OpenInputQuestionScreen;
import com.example.ui.screens.QuestionScreen;
import com.example.ui.screens.StartScreen;
import com.example.ui.screens.TrueFalseQuestionScreen;

public class GameWindow extends JFrame {
   
    public GameWindow() {
        setTitle("Who Wants to Be a Millionaire");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ScreenManager screens = new ScreenManager();
        JokerState jokerState = new JokerState();

        String[] questionSequence = {
            ScreenManager.QUESTION,
            ScreenManager.TRUE_FALSE,
            ScreenManager.OPEN_INPUT
        };
        int[] questionIndex = {0};

        screens.addScreen(
            ScreenManager.START,
            new StartScreen(() -> {
                questionIndex[0] = 0;
                screens.showScreen(questionSequence[0]);
            })
        );

        screens.addScreen(
            ScreenManager.QUESTION,
            new QuestionScreen(
                () -> screens.showScreen(ScreenManager.BETWEEN),
                jokerState
            )
        );

        screens.addScreen(
            ScreenManager.TRUE_FALSE,
            new TrueFalseQuestionScreen(
                () -> screens.showScreen(ScreenManager.BETWEEN),
                jokerState
            )
        );

        screens.addScreen(
            ScreenManager.OPEN_INPUT,
            new OpenInputQuestionScreen(
                () -> screens.showScreen(ScreenManager.BETWEEN),
                jokerState
            )
        );

        screens.addScreen(
            ScreenManager.BETWEEN,
            new InBetweenScreen(
                () -> {
                    questionIndex[0] = (questionIndex[0] + 1) % questionSequence.length;
                    screens.showScreen(questionSequence[questionIndex[0]]);
                },
                () -> screens.showScreen(ScreenManager.END),
                jokerState
            )
        );

        screens.addScreen(
            ScreenManager.END,
            new EndScreen(() -> screens.showScreen(ScreenManager.START))
        );

        setContentPane(screens);
        screens.showScreen(ScreenManager.END);
    }
}