package org.example.wwtbam.data;

import java.util.*;
import org.example.wwtbam.model.Difficulty;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.QuestionType;

public final class QuestionBank implements QuestionSource {

    private final Map<QuestionType, Map<Difficulty, List<Question>>> allQuestions =
        new EnumMap<>(QuestionType.class);
    private final Map<QuestionType, Map<Difficulty, Deque<Question>>> unused =
        new EnumMap<>(QuestionType.class);
    private final Random random;

    public QuestionBank(List<Question> questions, Random random) {
        this.random = random;

        for (QuestionType type : QuestionType.values()) {
            Map<Difficulty, List<Question>> byDifficulty = new EnumMap<>(Difficulty.class);
            for (Difficulty difficulty : Difficulty.values()) {
                byDifficulty.put(difficulty, new ArrayList<>());
            }
            allQuestions.put(type, byDifficulty);
        }

        for (Question question : questions) {
            allQuestions.get(question.getType()).get(question.getDifficulty()).add(question);
        }

        startNewGame();
    }

    public void startNewGame() {
        for (QuestionType type : QuestionType.values()) {
            Map<Difficulty, Deque<Question>> piles = new EnumMap<>(Difficulty.class);
            for (Difficulty difficulty : Difficulty.values()) {
                List<Question> pile = new ArrayList<>(allQuestions.get(type).get(difficulty));
                Collections.shuffle(pile, random);
                piles.put(difficulty, new ArrayDeque<>(pile));
            }
            unused.put(type, piles);
        }
    }

    @Override
    public Question draw(QuestionType type, Difficulty difficulty) {
        Question next = unused.get(type).get(difficulty).poll();
        if (next == null) {
            throw new NoSuchElementException("No unused " + type + " " + difficulty
                + " questions left");
        }
        return next;
    }

    public int remaining(QuestionType type, Difficulty difficulty) {
        return unused.get(type).get(difficulty).size();
    }
}
