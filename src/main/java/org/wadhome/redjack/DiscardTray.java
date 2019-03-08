package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

class DiscardTray extends CardStack {

    DiscardTray(int numDecks) {
        cards = new ArrayList<>(TableRules.NUM_CARDS_PER_DECK * numDecks);
    }

    void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }
}
