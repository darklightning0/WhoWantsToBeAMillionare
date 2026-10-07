package org.example.wwtbam.ui;

import javax.swing.*;
import java.awt.*;

public final class ScreenManager extends JPanel {
    public static final String START = "start";
    public static final String QUESTION = "question";
    public static final String BETWEEN = "between";
    public static final String END = "end";
    public static final String TRUE_FALSE = "true_false";
    public static final String OPEN_INPUT = "open_input";

    private final CardLayout layout = new CardLayout();

    public ScreenManager() {
        setLayout(layout);
    }

    public void addScreen(String name, JPanel screen) {
        add(screen, name);
    }

    public void showScreen(String name) {
        layout.show(this, name);
    }
}
