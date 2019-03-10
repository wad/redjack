package org.wadhome.redjack;

public class Blackjack {
    static final int NUM_CARDS_PER_DECK = Value.values().length * Suite.values().length; // 52
    static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS = NUM_CARDS_PER_DECK
            + (NUM_CARDS_PER_DECK >> 1);
    static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_DECK = NUM_CARDS_PER_DECK;
    static final int MAX_VALID_HAND_POINTS = 21;
    static final int DEALER_STAND_TOTAL = 17;
    static final int NUM_CARDS_IN_CHARLIE_HAND = 7;
}
