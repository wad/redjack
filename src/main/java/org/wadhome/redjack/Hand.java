package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

import static org.wadhome.redjack.Value.OPTIONAL_EXTRA_ACE_POINTS;

abstract class Hand {
    protected List<Card> cards = new ArrayList<>();
    private Card firstCard = null;
    private Card secondCard = null;

    void addCard(Card card) {
        if (cards.size() == 0) {
            firstCard = card;
        }
        if (cards.size() == 1) {
            secondCard = card;
        }
        cards.add(card);
    }

    List<Card> removeCards() {
        List<Card> cardsToRemove = new ArrayList<>(cards);
        cards.clear();
        firstCard = null;
        secondCard = null;
        return cardsToRemove;
    }

    int getNumCards() {
        return cards.size();
    }

    Card getFirstCard() {
        return firstCard;
    }

    Card getSecondCard() {
        return secondCard;
    }

    boolean hasAtLeastOneAce() {
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

    int computeMinSum() {
        int minSum = 0;
        for (Card card : cards) {
            minSum += card.getValue().getPoints();
        }
        return minSum;
    }

    int computeMaxSum() {
        int maxSum = 0;
        for (Card card : cards) {
            maxSum += card.getValue().getPoints();
        }

        if (hasAtLeastOneAce()) {
            if (maxSum + OPTIONAL_EXTRA_ACE_POINTS <= Blackjack.MAX_VALID_HAND_POINTS) {
                maxSum += OPTIONAL_EXTRA_ACE_POINTS;
            }
        }
        return maxSum;
    }

    boolean isTwentyOne() {
        int minSum = computeMinSum();
        if (minSum == Blackjack.MAX_VALID_HAND_POINTS) {
            return true;
        }
        if (minSum > Blackjack.MAX_VALID_HAND_POINTS) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (computeMaxSum() == Blackjack.MAX_VALID_HAND_POINTS) {
            return true;
        }
        return false;
    }

    private boolean hasExactlyTwoCards() {
        return cards.size() == 2;
    }

    boolean isPair() {
        return hasExactlyTwoCards()
                && firstCard.getValue().getPoints() == secondCard.getValue().getPoints();
    }

    boolean isBust() {
        return computeMinSum() > Blackjack.MAX_VALID_HAND_POINTS;
    }

    boolean isBlackjack() {
        return hasExactlyTwoCards()
                && hasAtLeastOneAce()
                && hasAtLeastOneTenPointCard();
    }

    boolean isCharlie() {
        return cards.size() == Blackjack.NUM_CARDS_IN_CHARLIE_HAND
                && !isBust();
    }

    ComparisonResult compareWith(Hand hand) {
        int thisSum = computeMaxSum();
        int thatSum = hand.computeMaxSum();
        if (thisSum == thatSum) {
            if (thisSum > Blackjack.MAX_VALID_HAND_POINTS) {
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

    String showCardsWithTotal() {
        if (cards.isEmpty()) {
            return "(empty hand)";
        }

        String total;
        if (isBlackjack()) {
            total = "blackjack";
        } else {
            int minSum = this.computeMinSum();
            int maxSum = this.computeMaxSum();
            if (minSum == maxSum) {
                total = String.valueOf(minSum);
            } else {
                total = (minSum + " or " + maxSum);
            }
        }
        total = " (" + total + ")";
        return toString() + total;
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
}
