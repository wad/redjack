package org.wadhome.redjack;

import java.util.Set;

public class SplitHand extends Hand {
    private PlayerHand parentHand;
    private MoneyPile betAmount = MoneyPile.zero();

    public SplitHand(PlayerHand parentHand) {
        this.parentHand = parentHand;
    }

    public MoneyPile getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(MoneyPile betAmount) {
        this.betAmount = betAmount;
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
