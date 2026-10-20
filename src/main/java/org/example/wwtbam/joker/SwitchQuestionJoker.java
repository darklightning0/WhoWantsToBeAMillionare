package org.example.wwtbam.joker;

import org.example.wwtbam.data.QuestionSource;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.TrueFalseQuestion;

public class SwitchQuestionJoker extends Joker {

    private final QuestionSource questionSource;

    public SwitchQuestionJoker(QuestionSource questionSource) {
        this.questionSource = questionSource;
    }

    @Override
    public String getName() {
        return "Switch Question";
    }

    @Override
    public String getDescription() {
        return "Swaps this statement for a different one of the same level.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof TrueFalseQuestion;
    }

    @Override
    protected void applyTo(Round round) {
        Question current = round.getQuestion();
        Question replacement = questionSource.draw(current.getType(), current.getDifficulty());
        round.replaceQuestion(replacement);
    }
}
