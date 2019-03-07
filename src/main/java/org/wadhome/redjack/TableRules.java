package org.wadhome.redjack;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TableRules {
    static final int NUM_CARDS_PER_DECK = Value.values().length * Suite.values().length; // 52
    static final int MAX_VALID_HAND_POINTS = 21;
    static final int DEALER_STAND_TOTAL = 17;
    static final int NUM_CARDS_IN_CHARLIE_HAND = 7;

    static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_DECK = NUM_CARDS_PER_DECK;
    static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS = NUM_CARDS_PER_DECK
            + (NUM_CARDS_PER_DECK >> 1);

    enum BlackjackPayOptions {
        TwoToOne("2:1"),
        ThreeToTwo("3:2"),
        SixToFive("6:5");

        String displayValue;

        BlackjackPayOptions(String displayValue) {
            this.displayValue = displayValue;
        }
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
    boolean canSurrender = false;
    boolean sevenCardCharlie = true;

    private TableRules() {
        // use a creator method
    }

    public static TableRules getDefaultRules() {
        return new TableRules();
    }

    public static TableRules getRulesForHomeCasino() {
        return new TableRules();
    }

    public static TableRules getRulesForHomeCasinoWithSurrender() {
        TableRules tableRules = new TableRules();
        tableRules.canSurrender = true;
        return tableRules;
    }

    @Override
    public String toString() {
        return "===\n" +
                "=== Table rules\n" +
                "=== Number of decks: " + getNumDecks() + "\n" +
                "=== Minimum bet: " + getMinBet() + "\n" +
                "=== Maximum bet: " + getMaxBet() + "\n" +
                "=== Dealer hits soft 17: " + (mustHitSoftSeventeen() ? "yes" : "no") + "\n" +
                "=== Blackjack pays: " + getBlackjackPayOptions().displayValue + "\n" +
                "=== Can double down after split: " + (canDoubleDownAfterSplit() ? "yes" : "no") + "\n" +
                "=== Can hit split aces: " + (canHitSplitAces() ? "yes" : "no") + "\n" +
                "=== Maximum number of splits: " + getMaxNumSplits() + "\n" +
                "=== Surrender allowed: " + (canSurrender() ? "yes" : "no") + "\n" +
                "===";
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
