package org.example.wwtbam.ui;

public final class JokerState {
    private final boolean[] used = new boolean[3];

    public boolean isUsed(int index) {
        return used[index];
    }

    public void use(int index) {
        used[index] = true;
    }
}
