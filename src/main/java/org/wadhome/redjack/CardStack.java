package org.wadhome.redjack;

import java.util.List;

public abstract class CardStack {
    protected List<Card> cards;
    private int stackNumber;

    protected void setStackNumber(int stackNumber) {
        this.stackNumber = stackNumber;
    }

    protected int getStackNumber() {
        return stackNumber;
    }

    boolean hasCards() {
        return !cards.isEmpty();
    }

    protected void addCard(Card card) {
        cards.add(card);
    }

    protected Card drawTopCard() {
        if (!hasCards()) {
            throw new RuntimeException("Cannot draw, no card available in stack " + stackNumber + "!");
        }

        Card topCard = cards.get(0);
        cards.remove(0);
        return topCard;
    }

    protected abstract void extraHandlingOnCardDraw();
}
