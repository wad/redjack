package org.wadhome.redjack.rules;

import org.wadhome.redjack.money.CurrencyAmount;

public class TableRulesForTest extends TableRules {

    public TableRulesForTest() {
        super();
    }

    public void setMinBet(CurrencyAmount minBet) {
        this.minBet = minBet;
    }

    public void setMaxBet(CurrencyAmount maxBet) {
        this.maxBet = maxBet;
    }

    public void setHitSoftSeventeen(boolean hitSoftSeventeen) {
        this.hitSoftSeventeen = hitSoftSeventeen;
    }

    public void setBlackjackPayOptions(BlackjackPayOptions blackjackPayOptions) {
        this.blackjackPayOptions = blackjackPayOptions;
    }

    public void setCanDoubleDownAfterSplit(boolean canDoubleDownAfterSplit) {
        this.canDoubleDownAfterSplit = canDoubleDownAfterSplit;
    }

    public void setDoubleDownOptions(DoubleDownRuleOptions doubleDownOptions) {
        this.doubleDownOptions = doubleDownOptions;
    }

    public void setCanHitSplitAces(boolean canHitSplitAces) {
        this.canHitSplitAces = canHitSplitAces;
    }

    public void setMaxNumSplits(int maxNumSplits) {
        this.maxNumSplits = maxNumSplits;
    }

    public void setNumBurnCards(int numBurnCards) {
        this.numBurnCards = numBurnCards;
    }

    public void setNumDecks(int numDecks) {
        this.numDecks = numDecks;
    }

    public void setNumCardsAfterCutCard(int numCardsAfterCutCard) {
        this.numCardsAfterCutCard = numCardsAfterCutCard;
    }

    public void setCanSurrender(boolean canSurrender) {
        this.canSurrender = canSurrender;
    }

    public void setSevenCardCharlieRuleIsActive(boolean sevenCardCharlieRuleIsActive) {
        this.sevenCardCharlieRuleIsActive = sevenCardCharlieRuleIsActive;
    }
}
