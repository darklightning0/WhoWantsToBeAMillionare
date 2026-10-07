package org.example.wwtbam.model;

import java.util.*;
import java.util.stream.Collectors;

public final class TypedAnswerQuestion extends Question {

    private final List<String> acceptedAnswers;
    private final Set<String> normalizedAnswers;

    public TypedAnswerQuestion(String id, String text, String category, Difficulty difficulty,
                               List<String> acceptedAnswers) {
        super(id, text, category, difficulty);
        if (acceptedAnswers == null || acceptedAnswers.isEmpty()) {
            throw new IllegalArgumentException(id + ": needs at least one accepted answer");
        }
        //Checks if answers are empty or not, if they are, throws an exception

        this.acceptedAnswers = List.copyOf(acceptedAnswers);
        this.normalizedAnswers = this.acceptedAnswers.stream()
            .map(NormalizeAnswer::normalize)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public QuestionType getType() {
        return QuestionType.TYPED_ANSWER;
    }

    @Override
    public boolean isCorrect(String playerAnswer) {
        return playerAnswer != null
            && normalizedAnswers.contains(NormalizeAnswer.normalize(playerAnswer));
    }

    @Override
    public String getCorrectAnswerText() {
        return acceptedAnswers.getFirst();
    }

    public List<String> getAcceptedAnswers() {
        return acceptedAnswers;
    }

}