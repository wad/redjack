package org.wadhome.redjack;

public class TableRules {
    public static final int MAX_VALID_HAND_POINTS = 21;
    public static final int NUM_CARDS_PER_DECK = Value.values().length * Suite.values().length;
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_DECK = NUM_CARDS_PER_DECK;
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS = NUM_CARDS_PER_DECK
            + (NUM_CARDS_PER_DECK >> 1);
    public static final int DEALER_STAND_TOTAL = 17;
    public static final int NUM_CARDS_IN_CHARLIE_HAND = 7;

    enum BlackjackPayOptions {
        TwoToOne,
        ThreeToTwo,
        SixToFive
    }

    enum DoubleDownOptions {
        Any,
        TenAndAceOnly
    }

    MoneyPile minBet = new MoneyPile(1000);
    MoneyPile maxBet = new MoneyPile(100000);
    boolean hitSoftSeventeen = true;
    BlackjackPayOptions blackjackPayOptions = BlackjackPayOptions.ThreeToTwo;
    boolean canDoubleDownAfterSplit = true;
    DoubleDownOptions doubleDownOptions = DoubleDownOptions.Any;
    boolean canHitSplitAces = false;
    int maxNumSplits = 3;
    int numBurnCards = 1;
    int numDecks = 6;
    int numCardsAfterCutCard = NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS;
    boolean canSurrender = true;
    boolean sevenCardCharlie = true;

    private TableRules() {
        // use a creator method
    }

    public static TableRules getDefaultRules() {
        return new TableRules();
    }

    @Override
    public String toString() {
        // todo: show all the rules
        StringBuilder builder = new StringBuilder();
        builder.append("===================\n");
        builder.append("=================== Table rules\n");
        builder.append("=================== Number of decks: ").append(getNumDecks()).append("\n");
        builder.append("===================");
        return builder.toString();
    }

    public MoneyPile getMinBet() {
        return minBet;
    }

    public MoneyPile getMaxBet() {
        return maxBet;
    }

    public boolean mustHitSoftSeventeen() {
        return hitSoftSeventeen;
    }

    public BlackjackPayOptions getBlackjackPayOptions() {
        return blackjackPayOptions;
    }

    public boolean canDoubleDownAfterSplit() {
        return canDoubleDownAfterSplit;
    }

    public DoubleDownOptions getDoubleDownOptions() {
        return this.doubleDownOptions;
    }

    public boolean canHitSplitAces() {
        return canHitSplitAces;
    }

    public int getMaxNumSplits() {
        return maxNumSplits;
    }

    public int getNumBurnCards() {
        return numBurnCards;
    }

    public int getNumDecks() {
        return numDecks;
    }

    public int getNumCardsAfterCutCard() {
        return numCardsAfterCutCard;
    }

    public boolean canSurrender() {
        return canSurrender;
    }

    public boolean sevenCardCharlie() {
        return sevenCardCharlie;
    }
}
