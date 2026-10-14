package org.example.wwtbam.joker;

import java.util.List;
import java.util.Random;
import org.example.wwtbam.data.QuestionSource;
import org.example.wwtbam.model.Question;

public final class JokerPool {

    private final List<Joker> jokers;

    public JokerPool(QuestionSource questionSource, Random random) {
        this.jokers = List.of(
            new AskAFriendJoker(),
            new AskTheHostJoker());
        // need to add other jokers here.
    }

    public List<Joker> jokersFor(Question question) {
        return jokers.stream()
            .filter(joker -> joker.canBeUsedOn(question))
            .toList();
    }

    public List<Joker> all() {
        return jokers;
    }
}
