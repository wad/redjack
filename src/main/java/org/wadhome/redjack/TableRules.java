package org.wadhome.redjack;

class TableRules {

    MoneyPile minBet = new MoneyPile(1000);
    MoneyPile maxBet = new MoneyPile(100000);
    boolean hitSoftSeventeen = true;
    BlackjackPayOptions blackjackPayOptions = BlackjackPayOptions.ThreeToTwo;
    boolean canDoubleDownAfterSplit = true;
    DoubleDownRuleOptions doubleDownOptions = DoubleDownRuleOptions.Any;
    boolean canHitSplitAces = false;
    int maxNumSplits = 3; // This means that a hand can turn into a max of 4 split hands.
    int numBurnCards = 1;
    int numDecks = 6;
    int numCardsAfterCutCard = Blackjack.NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS;
    boolean canSurrender = false;
    boolean sevenCardCharlieRuleIsActive = true;

    private TableRules() {
        // use a creator method
    }

    static TableRules getDefaultRules() {
        return new TableRules();
    }

    static TableRules getRulesForHomeCasino() {
        return new TableRules();
    }

    static TableRules getRulesForHomeCasinoWithSurrender() {
        TableRules tableRules = new TableRules();
        tableRules.canSurrender = true;
        return tableRules;
    }

    @Override
    public String toString() {
        return "=== Number of decks: " + getNumDecks() + "\n" +
                "=== Minimum bet: " + getMinBet() + "\n" +
                "=== Maximum bet: " + getMaxBet() + "\n" +
                "=== Dealer hits soft 17: " + (mustHitSoftSeventeen() ? "yes" : "no") + "\n" +
                "=== Blackjack pays: " + getBlackjackPayOptions().displayValue + "\n" +
                "=== Can double down after split: " + (canDoubleDownAfterSplit() ? "yes" : "no") + "\n" +
                "=== Can hit split aces: " + (canHitSplitAces() ? "yes" : "no") + "\n" +
                "=== Maximum number of splits: " + getMaxNumSplits() + "\n" +
                "=== Surrender allowed: " + (canSurrender() ? "yes" : "no") + "\n";
    }

    public String getRulesDisplay(int tableNumber) {
        String message = toString();
        return "\n=== Table number " + tableNumber + " rules\n" + message;
    }

    MoneyPile getMinBet() {
        return minBet;
    }

    MoneyPile getMaxBet() {
        return maxBet;
    }

    boolean mustHitSoftSeventeen() {
        return hitSoftSeventeen;
    }

    BlackjackPayOptions getBlackjackPayOptions() {
        return blackjackPayOptions;
    }

    boolean canDoubleDownAfterSplit() {
        return canDoubleDownAfterSplit;
    }

    DoubleDownRuleOptions getDoubleDownOptions() {
        return this.doubleDownOptions;
    }

    boolean canHitSplitAces() {
        return canHitSplitAces;
    }

    int getMaxNumSplits() {
        return maxNumSplits;
    }

    int getNumBurnCards() {
        return numBurnCards;
    }

    int getNumDecks() {
        return numDecks;
    }

    int getNumCardsAfterCutCard() {
        return numCardsAfterCutCard;
    }

    boolean canSurrender() {
        return canSurrender;
    }

    boolean sevenCardCharlieRuleIsActive() {
        return sevenCardCharlieRuleIsActive;
    }
}
