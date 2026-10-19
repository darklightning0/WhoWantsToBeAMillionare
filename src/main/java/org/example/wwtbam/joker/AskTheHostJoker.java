package org.example.wwtbam.joker;

import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.TypedAnswerQuestion;

public class AskTheHostJoker extends RevealAnswerJoker {

    @Override
    public String getName() {
        return "Ask the Host";
    }

    @Override
    public String getDescription() {
        return "The host tells you the answer.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof TypedAnswerQuestion;
    }

    @Override
    protected String speaker() {
        return "The host";
    }

    @Override
    protected String sentence(String correctAnswer) {
        return "Between you and me, the answer is " + correctAnswer + ".";
    }

    @Override
    public String getAvailableIconPath() {
        return "/joker_ask_host.jpeg";
    }

    @Override
    public String getUsedIconPath() {
        return "/joker_ask_host_used.jpeg";
    }
}
