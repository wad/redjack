package org.wadhome.redjack;

import java.util.HashSet;
import java.util.Set;

import static org.wadhome.redjack.Value.OPTIONAL_EXTRA_ACE_POINTS;

public abstract class Hand {
    protected Set<Card> cards = new HashSet<>();
    protected Card firstCard = null;
    protected Card secondCard = null;

    public void addCard(Card card) {
        if (cards.size() == 0) {
            firstCard = card;
        }
        if (cards.size() == 1) {
            secondCard = card;
        }
        cards.add(card);
    }

    public boolean hasAnyCards() {
        return !cards.isEmpty() || hasAnyCardsHelper();
    }

    protected abstract boolean hasAnyCardsHelper();

    protected abstract Set<Card> removeCardsHelper();

    public Set<Card> removeCards() {
        return removeCards(true);
    }

    public Set<Card> removeCards(boolean shouldRecurse) {
        Set<Card> cardsToRemove = new HashSet<>(cards);
        cards.clear();
        firstCard = null;
        secondCard = null;

        if (shouldRecurse) {
            cardsToRemove.addAll(removeCardsHelper());
        }

        return cardsToRemove;
    }

    public int getNumCards() {
        return cards.size();
    }

    public Card getFirstCard() {
        return firstCard;
    }

    public Card getSecondCard() {
        return firstCard;
    }

    protected boolean hasAtLeastOneAce() {
        for (Card card : cards) {
            if (card.getValue() == Value.Ace) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAtLeastOneTenPointCard() {
        for (Card card : cards) {
            if (card.getValue().getPoints() == 10) {
                return true;
            }
        }
        return false;
    }

    protected int computeMinSum() {
        int minSum = 0;
        for (Card card : cards) {
            minSum += card.getValue().getPoints();
        }
        return minSum;
    }

    protected int computeMaxSum() {
        int maxSum = 0;
        for (Card card : cards) {
            maxSum += card.getValue().getPoints();
        }

        if (hasAtLeastOneAce()) {
            if (maxSum + OPTIONAL_EXTRA_ACE_POINTS < TableRules.MAX_VALID_HAND_POINTS) {
                maxSum += OPTIONAL_EXTRA_ACE_POINTS;
            }
        }
        return maxSum;
    }

    public boolean isBust() {
        return computeMinSum() > TableRules.MAX_VALID_HAND_POINTS;
    }

    public boolean isBlackjack() {
        return cards.size() == 2
                && hasAtLeastOneAce()
                && hasAtLeastOneTenPointCard();
    }

    public ComparisonResult compareWith(Hand hand) {
        int thisSum = computeMaxSum();
        int thatSum = hand.computeMaxSum();
        if (thisSum == thatSum) {
            if (thisSum > TableRules.MAX_VALID_HAND_POINTS) {
                throw new RuntimeException("What? Comparing two busted hands?");
            }

            return ComparisonResult.same;
        }

        if (isBust()) {
            return ComparisonResult.ThisLoses;
        }

        if (hand.isBust()) {
            return ComparisonResult.ThisWins;
        }

        return thisSum > thatSum ? ComparisonResult.ThisWins : ComparisonResult.ThisLoses;
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "(empty hand)";
        }

        StringBuilder output = new StringBuilder();
        for (Card card : cards) {
            output.append(card.toString());
            output.append(" ");
        }
        String contents = output.toString();
        return contents.substring(0, contents.length() - 1);
    }

    String showCardsWithTotal() {
        if (cards.isEmpty()) {
            return "(empty hand)";
        }

        String total = toString() + " (";
        int minSum = this.computeMinSum();
        int maxSum = this.computeMaxSum();
        if (minSum == maxSum) {
            total += minSum;
        } else {
            total += ("" + minSum + " or " + maxSum);
        }
        return total + ")";
    }
}
