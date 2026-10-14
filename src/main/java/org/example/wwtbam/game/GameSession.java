package org.example.wwtbam.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.example.wwtbam.data.QuestionBank;
import org.example.wwtbam.joker.JokerPool;
import org.example.wwtbam.model.Difficulty;
import org.example.wwtbam.model.QuestionType;

public class GameSession {

    public static final int LAST_LEVEL = 15;

    private final QuestionBank bank;
    private final Random random;
    private JokerPool jokers;
    private int level;
    private QuestionType previousType;
    private Round currentRound;
    private boolean answered;

    public GameSession(QuestionBank bank, Random random) {
        this.bank = bank;
        this.random = random;
        reset();
    }

    public final void reset() {
        bank.startNewGame();
        jokers = new JokerPool(bank, random);
        level = 1;
        previousType = null;
        currentRound = null;
        answered = false;
    }

    public Round startLevel() {
        if (hasWon()) {
            throw new IllegalStateException("The game is already won");
        }
        currentRound = new Round(bank.draw(pickType(), difficultyFor(level)));
        answered = false;
        return currentRound;
    }

    public boolean submitAnswer(String answer) {
        if (currentRound == null || answered) {
            throw new IllegalStateException("Call startLevel() before answering");
        }
        answered = true;
        boolean correct = currentRound.getQuestion().isCorrect(answer);
        if (correct) {
            level++;
        }
        return correct;
    }

    public boolean hasWon() {
        return level > LAST_LEVEL;
    }

    public int getLevel() {
        return level;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public JokerPool getJokers() {
        return jokers;
    }

    static Difficulty difficultyFor(int level) {
        if (level <= 5) {
            return Difficulty.EASY;
        }
        if (level <= 10) {
            return Difficulty.MEDIUM;
        }
        return Difficulty.HARD;
    }

    private QuestionType pickType() {
        List<QuestionType> choices = new ArrayList<>(List.of(QuestionType.values()));
        choices.remove(previousType);
        if (difficultyFor(level) == Difficulty.HARD) {
            choices.remove(QuestionType.TRUE_FALSE);
        }
        QuestionType type = choices.get(random.nextInt(choices.size()));
        previousType = type;
        return type;
    }
}