package org.example.wwtbam.joker;

import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.Question;
import org.example.wwtbam.model.TypedAnswerQuestion;

public class RevealLetterJoker extends Joker {

    @Override
    public String getName() {
        return "Reveal Letter";
    }

    @Override
    public String getDescription() {
        return "Reveals the first letter of the answer, or the last digit if it is a number.";
    }

    @Override
    public boolean canBeUsedOn(Question question) {
        return question instanceof TypedAnswerQuestion;
    }

    @Override
    protected boolean isReadyFor(Round round) {
        long letters = round.getQuestion().getCorrectAnswerText().chars()
            .filter(Character::isLetterOrDigit)
            .count();
        return letters >= 2;
    }

    @Override
    protected void applyTo(Round round) {
        round.setHint(mask(round.getQuestion().getCorrectAnswerText()));
    }

    static String mask(String answer) {
        int revealIndex = isNumber(answer) ? lastDigitIndex(answer)
            : firstLetterOrDigitIndex(answer);
        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            char c = answer.charAt(i);
            if (!Character.isLetterOrDigit(c) || i == revealIndex) {
                hint.append(c);
            } else {
                hint.append('_');
            }
            hint.append(' ');
        }
        return hint.toString().trim();
    }

    static boolean isNumber(String answer) {
        return answer.matches("-?[0-9][0-9,.]*");
    }

    private static int lastDigitIndex(String answer) {
        for (int i = answer.length() - 1; i >= 0; i--) {
            if (Character.isDigit(answer.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int firstLetterOrDigitIndex(String answer) {
        for (int i = 0; i < answer.length(); i++) {
            if (Character.isLetterOrDigit(answer.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
