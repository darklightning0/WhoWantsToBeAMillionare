package org.example.wwtbam.joker;

import org.example.wwtbam.game.Round;

public abstract class RevealAnswerJoker extends Joker {

    protected abstract String speaker();

    protected abstract String sentence(String correctAnswer);

    @Override
    protected final void applyTo(Round round) {
        String answer = round.getQuestion().getCorrectAnswerText();
        round.addJokerMessage(speaker() + ": \"" + sentence(answer) + "\"");
    }
}