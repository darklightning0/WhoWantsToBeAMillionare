package org.example.wwtbam.model;

import java.util.*;

public final class MultipleChoiceQuestion extends ChoiceQuestion {

    private final String correctAnswer;
    private final List<String> options;

    public MultipleChoiceQuestion(String id, String text, String category, Difficulty difficulty,
                             String correctAnswer, List<String> incorrectAnswers, Random random) {
        super(id, text, category, difficulty);
        this.correctAnswer = Objects.requireNonNull(correctAnswer, "correctAnswer");

        List<String> all = new ArrayList<>(incorrectAnswers);
        all.add(correctAnswer);
        if (all.size() < 3) {
            throw new IllegalArgumentException(id + ": needs at least two incorrect answers");
        }
        if (new HashSet<>(all).size() != all.size()) {
            throw new IllegalArgumentException(id + ": options must all be different");
        }
        // Sanity check to verify the options that will be displayed in multiple choice questions.
        Collections.shuffle(all, random);
        this.options = List.copyOf(all);
    }

    @Override
    public QuestionType getType() {
        return QuestionType.MULTIPLE_CHOICE;
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public String getCorrectOption() {
        return correctAnswer;
    }
}