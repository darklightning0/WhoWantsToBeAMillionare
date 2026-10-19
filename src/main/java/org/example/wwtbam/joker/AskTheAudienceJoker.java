package org.example.wwtbam.joker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.ChoiceQuestion;
import org.example.wwtbam.model.Question;

public class AskTheAudienceJoker extends Joker {

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
        return "Polls the audience for the most likely answer.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof ChoiceQuestion;
    }

    @Override
    protected void applyTo(Round round) {
        if (round.getQuestion() instanceof ChoiceQuestion choiceQuestion) {
            List<String> options = choiceQuestion.getOptions();
            String correct = choiceQuestion.getCorrectOption();

            Map<String, Integer> votes = new LinkedHashMap<>();
            int correctPct = 55 + random.nextInt(26);
            int remaining = 100 - correctPct;

            List<String> otherOptions = options.stream()
                .filter(opt -> !opt.equals(correct))
                .toList();

            for (int i = 0; i < otherOptions.size(); i++) {
                if (i == otherOptions.size() - 1) {
                    votes.put(otherOptions.get(i), remaining);
                } else {
                    int share = remaining > 0 ? random.nextInt(remaining) : 0;
                    votes.put(otherOptions.get(i), share);
                    remaining -= share;
                }
            }
            votes.put(correct, correctPct);

            round.setAudienceVotes(votes);

            String formattedVotes = votes.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue() + "%")
                .collect(Collectors.joining(", "));
            round.addJokerMessage("Audience Poll Results: " + formattedVotes);
        }
    }

    @Override
    public String getAvailableIconPath() {
        return "/joker_audience.png";
    }

    @Override
    public String getUsedIconPath() {
        return "/joker_audience_used.png";
    }
}
