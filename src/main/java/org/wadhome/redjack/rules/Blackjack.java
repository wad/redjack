package org.wadhome.redjack.rules;

import org.wadhome.redjack.Suite;
import org.wadhome.redjack.Value;

public class Blackjack {
    public static final int NUM_CARDS_PER_DECK = Value.values().length * Suite.values().length; // 52
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS = NUM_CARDS_PER_DECK
            + (NUM_CARDS_PER_DECK >> 1);
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_DECK = NUM_CARDS_PER_DECK;
    public static final int MAX_VALID_HAND_POINTS = 21;
    public static final int DEALER_STAND_TOTAL = 17;
    public static final int NUM_CARDS_IN_CHARLIE_HAND = 7;
}
