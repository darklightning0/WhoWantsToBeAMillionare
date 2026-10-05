package com.example;

import javax.swing.*;

import com.example.ui.ScreenManager;
import com.example.ui.screens.EndScreen;
import com.example.ui.screens.InBetweenScreen;
import com.example.ui.screens.QuestionScreen;
import com.example.ui.screens.StartScreen;

public class GameWindow extends JFrame {
   
    public GameWindow() {
        setTitle("Who Wants to Be a Millionaire");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ScreenManager screens = new ScreenManager();

        screens.addScreen(
            ScreenManager.START,
            new StartScreen(() -> screens.showScreen(ScreenManager.QUESTION))
        );

        screens.addScreen(
            ScreenManager.QUESTION,
            new QuestionScreen(() -> screens.showScreen(ScreenManager.BETWEEN))
        );

        screens.addScreen(
            ScreenManager.BETWEEN,
            new InBetweenScreen(() -> screens.showScreen(ScreenManager.QUESTION))
        );

        screens.addScreen(
            ScreenManager.END,
            new EndScreen(() -> screens.showScreen(ScreenManager.START))
        );

        setContentPane(screens);
        screens.showScreen(ScreenManager.START);
    }
}