package org.example.wwtbam.joker;

import org.example.wwtbam.model.Question;

public class AskAFriendJoker extends RevealAnswerJoker {

    @Override
    public String getName() {
        return "Ask a Friend";
    }

    @Override
    public String getDescription() {
        return "Your friend tells you the answer.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return true;
    }

    @Override
    protected String speaker() {
        return "Your friend";
    }

    @Override
    protected String sentence(String correctAnswer) {
        return "I'm sure it's " + correctAnswer + ".";
    }
}