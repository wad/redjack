package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.Set;

public class DiscardTray extends CardStack {

    DiscardTray(int numDecks) {
        cards = new ArrayList<>(TableRules.NUM_CARDS_PER_DECK * numDecks);
    }

    void addCards(Set<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }

    @Override
    protected void extraHandlingOnCardDraw() {
    }
}
