package org.example.wwtbam.model;

import java.util.*;

public abstract class ChoiceQuestion extends Question {
    protected ChoiceQuestion(String id, String text, String category, Difficulty difficulty) {
        super(id, text, category, difficulty);
    }

    public abstract List<String> getOptions();

    public abstract String getCorrectOption();

    @Override
    public final boolean isCorrect(String playerAnswer) {
        return getCorrectOption().equals(playerAnswer);
    }

    @Override
    public final String getCorrectAnswerText() {
        return getCorrectOption();
    }
}