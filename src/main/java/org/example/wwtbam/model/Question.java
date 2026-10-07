package org.example.wwtbam.model;

import java.util.*;

public abstract class Question {

    private final String id;
    private final String text;
    private final String category;
    private final Difficulty difficulty;

    protected Question(String id, String text, String category, Difficulty difficulty) {
        this.id = Objects.requireNonNull(id, "id");
        this.text = Objects.requireNonNull(text, "text");
        this.category = Objects.requireNonNull(category, "category");
        this.difficulty = Objects.requireNonNull(difficulty, "difficulty");
    }

    public abstract QuestionType getType();

    public abstract String getCorrectAnswerText();

    public abstract boolean isCorrect(String playerAnswer);

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public String toString() {
        return id + " [" + getType() + ", " + difficulty + "] " + text;
    }
}