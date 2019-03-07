package org.wadhome.redjack;

import java.util.Set;

public class SplitHand extends Hand {
    private PlayerHand parentHand;
    private int splitHandIndex;

    public SplitHand(
            PlayerHand parentHand,
            int splitHandIndex) {
        this.parentHand = parentHand;
        this.splitHandIndex = splitHandIndex;
    }

    @Override
    protected boolean hasAnyCardsHelper() {
        return hasAnyCards(false);
    }

    @Override
    protected Set<Card> removeCardsHelper() {
        return removeCards(false);
    }
}
