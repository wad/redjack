package org.wadhome.redjack.casino;

import org.wadhome.redjack.rules.Blackjack;

import java.util.ArrayList;
import java.util.List;

public class Deck extends CardStack {

    public static final int MAX_DECK_NUMBER = 9999;

    public Deck(int deckNumber) {
        if (deckNumber > MAX_DECK_NUMBER) {
            throw new RuntimeException("Deck number > " + MAX_DECK_NUMBER);
        }
        setStackNumber(deckNumber);
        this.cards = createCards(deckNumber);
    }

    private static List<Card> createCards(int deckNumber) {
        List<Card> cards = new ArrayList<>(Blackjack.NUM_CARDS_PER_DECK);
        for (Suite suite : Suite.values()) {
            for (Value value : Value.values()) {
                cards.add(new Card(deckNumber, suite, value));
            }
        }
        return cards;
    }
}
