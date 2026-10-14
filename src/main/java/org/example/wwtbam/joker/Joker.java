package org.example.wwtbam.joker;

import org.example.wwtbam.game.Round;
import org.example.wwtbam.model.Question;

public abstract class Joker {
    private boolean used;

    public abstract String getName();

    public abstract String getDescription();

    public abstract boolean canBeUsedOn(Question question);

    protected abstract void applyTo(Round round);

    public boolean isReadyFor(Round round) {
        return true;
    }

    public final boolean isAvailableFor(Round round) {
        return !used && canBeUsedOn(round.getQuestion()) && isReadyFor(round);
    }

    public final void use(Round round) {
        if (!isAvailableFor(round)) {
            throw new IllegalStateException(getName() + " cannot be used with this question.");
        }
        applyTo(round);
        used = true;
    }

    public final boolean isUsed() {
        return true;
    }
}