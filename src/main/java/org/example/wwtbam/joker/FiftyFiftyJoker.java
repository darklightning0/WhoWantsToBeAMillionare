package org.example.wwtbam.joker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.MultipleChoiceQuestion;
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
        return "Removes two incorrect options.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof MultipleChoiceQuestion;
    }

    @Override
    protected void applyTo(Round round) {
        if (round.getQuestion() instanceof MultipleChoiceQuestion mcq) {
            String correct = mcq.getCorrectOption();
            List<String> incorrect = new ArrayList<>();
            for (String opt : mcq.getOptions()) {
                if (!opt.equals(correct)) {
                    incorrect.add(opt);
                }
            }
            Collections.shuffle(incorrect, random);
            List<String> toHide = incorrect.subList(0, Math.min(2, incorrect.size()));
            round.HideOptions(toHide);
            round.addJokerMessage("50:50 removed two incorrect answers.");
        }
    }

    @Override
    public String getAvailableIconPath() {
        return "/joker_5050.png";
    }

    @Override
    public String getUsedIconPath() {
        return "/joker_5050_used.png";
    }
}
