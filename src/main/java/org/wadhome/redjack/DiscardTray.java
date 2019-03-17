package org.wadhome.redjack;

import org.wadhome.redjack.rules.Blackjack;

import java.util.ArrayList;
import java.util.List;

public class DiscardTray extends CardStack {

    public DiscardTray(int numDecks) {
        cards = new ArrayList<>(Blackjack.NUM_CARDS_PER_DECK * numDecks);
    }

    void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }
}
