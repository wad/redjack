package org.wadhome.redjack;

public class TableRules
{
    public static final int MAX_VALID_HAND_POINTS = 21;
    public static final int NUM_CARDS_PER_DECK = Value.values().length * Suite.values().length;
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_DECK = NUM_CARDS_PER_DECK;
    public static final int NUM_CARDS_AFTER_CUT_CARD_FOR_ONE_POINT_FIVE_DECKS = NUM_CARDS_PER_DECK
            + (NUM_CARDS_PER_DECK >> 1);
    public static final int DEALER_STAND_TOTAL = 17;

    enum BlackjackPayOptions
    {
        TwoToOne,
        ThreeToTwo,
        SixToFive
    }

    enum DoubleDownOptions
    {
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

    public static TableRules getHomeCasinoRules()
    {
        return new TableRules();
    }

    public MoneyPile getMinBet()
    {
        return minBet;
    }

    public MoneyPile getMaxBet()
    {
        return maxBet;
    }

    public boolean mustHitSoftSeventeen()
    {
        return hitSoftSeventeen;
    }

    public BlackjackPayOptions getBlackjackPayOptions()
    {
        return blackjackPayOptions;
    }

    public boolean canDoubleDownAfterSplit()
    {
        return canDoubleDownAfterSplit;
    }

    public DoubleDownOptions getDoubleDownOptions()
    {
        return this.doubleDownOptions;
    }

    public boolean canHitSplitAces()
    {
        return canHitSplitAces;
    }

    public int getMaxNumSplits()
    {
        return maxNumSplits;
    }

    public int getNumBurnCards()
    {
        return numBurnCards;
    }

    public int getNumDecks()
    {
        return numDecks;
    }

    public int getNumCardsAfterCutCard()
    {
        return numCardsAfterCutCard;
    }

    public boolean canSurrender()
    {
        return canSurrender;
    }

    public boolean sevenCardCharlie()
    {
        return sevenCardCharlie;
    }
}
