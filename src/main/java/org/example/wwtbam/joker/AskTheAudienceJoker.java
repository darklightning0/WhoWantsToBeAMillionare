package org.example.wwtbam.joker;

import java.util.*;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.ChoiceQuestion;
import org.example.wwtbam.model.Question;

public class AskTheAudienceJoker extends Joker {

    private static final int AUDIENCE_SIZE = 100;
    private static final int CORRECT_VOTES = 70;

    private final Random random;

    public AskTheAudienceJoker(Random random) {
        this.random = random;
    }

    @Override
    public String getName() {
        return "Ask the Audience";
    }

    @Override
    public String getDescription() {
        return "See how 100 people in the audience would answer.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof ChoiceQuestion;
    }

    @Override
    protected void applyTo(Round round) {
        ChoiceQuestion question = (ChoiceQuestion) round.getQuestion();
        String correct = question.getCorrectOption();

        Map<String, Integer> votes = new LinkedHashMap<>();
        for (String option : round.getVisibleOptions()) {
            votes.put(option, 0);
        }
        votes.put(correct, CORRECT_VOTES);

        List<String> wrongOptions = new ArrayList<>(votes.keySet());
        wrongOptions.remove(correct);
        for (int i = 0; i < AUDIENCE_SIZE - CORRECT_VOTES; i++) {
            String vote = wrongOptions.get(random.nextInt(wrongOptions.size()));
            votes.merge(vote, 1, Integer::sum);
        }
        round.setAudienceVotes(votes);
    }
}
