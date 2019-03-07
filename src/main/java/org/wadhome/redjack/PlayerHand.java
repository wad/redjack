package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerHand extends Hand {
    private Player player = null;
    private MoneyPile betAmount = MoneyPile.zero();
    private int handNumber;
    private List<SplitHand> splitHands = new ArrayList<>();

    PlayerHand(int handNumber) {
        this.handNumber = handNumber;
    }

    boolean isInUse() {
        return this.player != null;
    }

    Player getPlayer() {
        return player;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    void removePlayer() {
        this.player = null;
    }

    MoneyPile getBetAmount() {
        return betAmount;
    }

    void setBetAmount(MoneyPile betAmount) {
        this.betAmount = betAmount;
    }

    String getSeatNumber() {
        return String.valueOf(handNumber + 1);
    }

    boolean isPair() {
        return getNumCards() == 2
                && firstCard.getValue() == secondCard.getValue();
    }

    private boolean canHandBeSplit(TableRules tableRules) {
        if (!isPair()) {
            return false;
        }
        int numSplitsSoFar = splitHands.size() - 1;
        return numSplitsSoFar < tableRules.getMaxNumSplits();
    }

    void splitTheHand(TableRules tableRules) {
        if (!canHandBeSplit(tableRules)) {
            throw new RuntimeException("Cannot split hand");
        }

        // todo

    }

    @Override
    protected boolean hasAnyCardsHelper() {
        for (SplitHand splitHand : splitHands) {
            if (splitHand.hasAnyCardsHelper()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Set<Card> removeCardsHelper() {
        Set<Card> removedCards = new HashSet<>();
        for (SplitHand splitHand : splitHands) {
            removedCards.addAll(splitHand.removeCardsHelper());
        }
        splitHands.clear();
        return removedCards;
    }
}
