package org.wadhome.redjack.rules;

import org.wadhome.redjack.MoneyPile;

// todo: make subclasses
public class TableRules {

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

    public MoneyPile getMinBet() {
        return minBet;
    }

    public MoneyPile getMaxBet() {
        return maxBet;
    }

    public void setMinBet(MoneyPile minBet) {
        this.minBet = minBet;
    }

    public void setMaxBet(MoneyPile maxBet) {
        this.maxBet = maxBet;
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

    public DoubleDownRuleOptions getDoubleDownOptions() {
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

    public boolean sevenCardCharlieRuleIsActive() {
        return sevenCardCharlieRuleIsActive;
    }

    public void setCanSurrender(boolean canSurrender) {
        this.canSurrender = canSurrender;
    }

    public void setCanHitSplitAces(boolean canHitSplitAces) {
        this.canHitSplitAces = canHitSplitAces;
    }

    public void setDoubleDownOptions(DoubleDownRuleOptions tenAndAceOnly) {
        this.doubleDownOptions = tenAndAceOnly;
    }
}
