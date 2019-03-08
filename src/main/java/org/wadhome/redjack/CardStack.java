package org.wadhome.redjack;

import java.util.Collections;
import java.util.List;

abstract class CardStack {
    protected List<Card> cards;
    private int stackNumber;

    void setStackNumber(int stackNumber) {
        this.stackNumber = stackNumber;
    }

    boolean hasCards() {
        return !cards.isEmpty();
    }

    // These are added to the end (bottom) of the stack, which is the back of the shoe.
    // The first one added will be the first one drawn.
    void addCardToBottom(Card... cardsToAdd) {
        Collections.addAll(cards, cardsToAdd);
    }

    Card drawTopCard() {
        if (!hasCards()) {
            throw new RuntimeException("Cannot draw, no card available in stack " + stackNumber + "!");
        }

        Card topCard = cards.get(0);
        cards.remove(0);
        extraHandlingOnCardDraw();
        return topCard;
    }

    protected void extraHandlingOnCardDraw() {
    }
}
