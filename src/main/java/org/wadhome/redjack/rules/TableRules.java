package org.wadhome.redjack.rules;

import org.wadhome.redjack.money.CurrencyAmount;

public abstract class TableRules {

    CurrencyAmount minBet;
    CurrencyAmount maxBet;
    boolean hitSoftSeventeen;
    BlackjackPayOptions blackjackPayOptions;
    boolean canDoubleDownAfterSplit;
    DoubleDownRuleOptions doubleDownOptions;
    boolean canHitSplitAces;
    int maxNumSplits;
    int numBurnCards;
    int numDecks;
    int numCardsAfterCutCard;
    boolean canSurrender;
    boolean sevenCardCharlieRuleIsActive;

    public TableRules() {
        minBet = new CurrencyAmount(10L);
        maxBet = new CurrencyAmount(1000L);
        hitSoftSeventeen = true;
        blackjackPayOptions = BlackjackPayOptions.ThreeToTwo;
        canDoubleDownAfterSplit = true;
        doubleDownOptions = DoubleDownRuleOptions.Any;
        canHitSplitAces = false;
        maxNumSplits = 3; // This means that a hand can turn into a max of 4 split hands.
        numBurnCards = 1;
        numDecks = 6;
        numCardsAfterCutCard = Blackjack.NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS;
        canSurrender = false;
        sevenCardCharlieRuleIsActive = true;
    }

    @Override
    public String toString() {
        return "=== Number of decks: " + getNumDecks() + "\n" +
                "=== Minimum bet: " + getMinBet() + "\n" +
                "=== Maximum bet: " + getMaxBet() + "\n" +
                "=== Dealer hits soft 17: " + (getMustHitSoftSeventeen() ? "yes" : "no") + "\n" +
                "=== Blackjack pays: " + getBlackjackPayOptions().displayValue + "\n" +
                "=== Can double down after split: " + (getCanDoubleDownAfterSplit() ? "yes" : "no") + "\n" +
                "=== Can hit split aces: " + (getCanHitSplitAces() ? "yes" : "no") + "\n" +
                "=== Maximum number of splits: " + getMaxNumSplits() + "\n" +
                "=== Surrender allowed: " + (getCanSurrender() ? "yes" : "no") + "\n";
    }

    public String getRulesDisplay(int tableNumber) {
        String message = toString();
        return "\n=== Table number " + tableNumber + " rules\n" + message;
    }

    public CurrencyAmount getMinBet() {
        return minBet;
    }

    public CurrencyAmount getMaxBet() {
        return maxBet;
    }

    public boolean getMustHitSoftSeventeen() {
        return hitSoftSeventeen;
    }

    public BlackjackPayOptions getBlackjackPayOptions() {
        return blackjackPayOptions;
    }

    public boolean getCanDoubleDownAfterSplit() {
        return canDoubleDownAfterSplit;
    }

    public DoubleDownRuleOptions getDoubleDownOptions() {
        return this.doubleDownOptions;
    }

    public boolean getCanHitSplitAces() {
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

    public boolean getCanSurrender() {
        return canSurrender;
    }

    public boolean isSevenCardCharlieRuleIsActive() {
        return sevenCardCharlieRuleIsActive;
    }
}
