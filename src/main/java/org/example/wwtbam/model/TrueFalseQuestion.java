package org.example.wwtbam.model;

import java.util.*;

public final class TrueFalseQuestion extends ChoiceQuestion {

    public static final String TRUE = "True";
    public static final String FALSE = "False";
    private static final List<String> OPTIONS = List.of(TRUE, FALSE);

    private final boolean answer;

    public TrueFalseQuestion(String id, String text, String category, Difficulty difficulty,
                             boolean answer) {
        super(id, text, category, difficulty);
        this.answer = answer;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.TRUE_FALSE;
    }

    @Override
    public List<String> getOptions() {
        return OPTIONS;
    }

    @Override
    public String getCorrectOption() {
        return answer ? TRUE : FALSE;
    }
}