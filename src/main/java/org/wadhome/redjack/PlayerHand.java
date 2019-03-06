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

    public PlayerHand(int handNumber) {
        this.handNumber = handNumber;
    }

    public boolean isInUse() {
        return this.player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void removePlayer() {
        this.player = null;
    }

    public MoneyPile getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(MoneyPile betAmount) {
        this.betAmount = betAmount;
    }

    public String getSeatNumber() {
        return String.valueOf(handNumber + 1);
    }

    public boolean isPair() {
        return getNumCards() == 2
                && firstCard.getValue() == secondCard.getValue();
    }

    public boolean canHandBeSplit(TableRules tableRules) {
        if (!isPair()) {
            return false;
        }
        int numSplitsSoFar = splitHands.size() - 1;
        return numSplitsSoFar < tableRules.getMaxNumSplits();
    }

    public void splitTheHand(TableRules tableRules) {
        if (!canHandBeSplit(tableRules)) {
            throw new RuntimeException("Cannot split hand");
        }


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
