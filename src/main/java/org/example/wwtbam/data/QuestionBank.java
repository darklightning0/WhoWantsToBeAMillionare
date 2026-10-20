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
        return draw(type, difficulty, null);
    }

    @Override
    public Question draw(QuestionType type, Difficulty difficulty, String excludeCategory) {
        Deque<Question> pile = unused.get(type).get(difficulty);
        if (pile == null || pile.isEmpty()) {
            throw new NoSuchElementException("No unused " + type + " " + difficulty + " questions left");
        }

        if (excludeCategory != null && !excludeCategory.isBlank()) {
            for (Iterator<Question> it = pile.iterator(); it.hasNext(); ) {
                Question candidate = it.next();
                if (!candidate.getCategory().trim().equalsIgnoreCase(excludeCategory.trim())) {
                    it.remove();
                    return candidate;
                }
            }
        }

        return pile.poll();
    }

    public boolean hasQuestionWithDifferentCategory(QuestionType type, Difficulty difficulty, String excludeCategory) {
        if (excludeCategory == null || excludeCategory.isBlank()) {
            return remaining(type, difficulty) > 0;
        }
        Deque<Question> pile = unused.get(type).get(difficulty);
        if (pile == null || pile.isEmpty()) {
            return false;
        }
        for (Question candidate : pile) {
            if (!candidate.getCategory().trim().equalsIgnoreCase(excludeCategory.trim())) {
                return true;
            }
        }
        return false;
    }

    public int remaining(QuestionType type, Difficulty difficulty) {
        return unused.get(type).get(difficulty).size();
    }
}
