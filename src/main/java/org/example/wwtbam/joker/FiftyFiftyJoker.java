package org.example.wwtbam.joker;

import java.util.*;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.ChoiceQuestion;
import org.example.wwtbam.model.Question;

public class FiftyFiftyJoker extends Joker {

    private final Random random;

    public FiftyFiftyJoker(Random random) {
        this.random = random;
    }

    @Override
    public String getName() {
        return "50:50";
    }

    @Override
    public String getDescription() {
        return "Removes two wrong answers.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof ChoiceQuestion choice && choice.getOptions().size() > 2;
    }

    @Override
    protected void applyTo(Round round) {
        ChoiceQuestion question = (ChoiceQuestion) round.getQuestion();
        List<String> wrongOptions = new ArrayList<>(round.getVisibleOptions());
        wrongOptions.remove(question.getCorrectOption());
        Collections.shuffle(wrongOptions, random);
        round.hideOptions(wrongOptions.subList(1, wrongOptions.size()));
    }
}
