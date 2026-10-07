package org.example.wwtbam;

import javax.swing.*;

import org.example.wwtbam.ui.GameWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}