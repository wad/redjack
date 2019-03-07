package org.wadhome.redjack;

import java.util.Set;

public class SplitHand extends Hand {
    private PlayerHand parentHand;

    public SplitHand(PlayerHand parentHand) {
        this.parentHand = parentHand;
    }

    @Override
    protected boolean hasAnyCardsHelper() {
        return hasAnyCards();
    }

    @Override
    protected Set<Card> removeCardsHelper() {
        return removeCards(false);
    }
}
