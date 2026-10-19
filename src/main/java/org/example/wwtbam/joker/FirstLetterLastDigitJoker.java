package org.example.wwtbam.joker;

import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.TypedAnswerQuestion;

public class FirstLetterLastDigitJoker extends Joker {

    @Override
    public String getName() {
        return "First Letter / Last Digit";
    }

    @Override
    public String getDescription() {
        return "Reveals the first letter or last digit of the answer.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof TypedAnswerQuestion;
    }

    @Override
    protected void applyTo(Round round) {
        String answer = round.getQuestion().getCorrectAnswerText().trim();
        String hint;
        char lastChar = answer.charAt(answer.length() - 1);
        char firstChar = answer.charAt(0);
        if (Character.isDigit(lastChar)) {
            hint = "The answer ends with the digit: '" + lastChar + "'";
        } else {
            hint = "The answer starts with the letter: '" + Character.toUpperCase(firstChar) + "'";
        }
        round.setHint(hint);
        round.addJokerMessage("Hint: " + hint);
    }

    @Override
    public String getAvailableIconPath() {
        return "/joker_get_digit.jpeg";
    }

    @Override
    public String getUsedIconPath() {
        return "/joker_get_digit_used.jpeg";
    }
}
