package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.Blackjack;

// This one uses the actual number of remaining cards in the shoe to calculate the true count.
public class CardCountMethodHighLowPerfect extends CardCountMethod {

    private int runningCount;

    public CardCountMethodHighLowPerfect(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
        runningCount = 0;
    }

    @Override
    protected CardCountStatus getCardCountStatusHelper() {
        return new CardCountStatusRunningAndTrue(
                runningCount,
                getTrueCount(table.getShoe().numCards()));
    }

    @Override
    public void observeCard(Card card) {
        switch (card.getValue()) {
            case Two:
            case Three:
            case Four:
            case Five:
            case Six:
                runningCount++;
                break;
            case Seven:
            case Eight:
            case Nine:
                break;
            case Ten:
            case Jack:
            case Queen:
            case King:
            case Ace:
                runningCount--;
                break;
            default:
                throw new RuntimeException("bug");
        }
    }

    @Override
    public void observeShuffle() {
        runningCount = 0;
    }

    @Override
    public void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(getTrueCount(table.getShoe().numCards()));
        bettingStrategy.getBet(betRequest);
    }

    private int getRunningCount() {
        return runningCount;
    }

    private int getTrueCount(int numCardsRemainingInShoe) {
        double numDecksRemaining = ((double) (numCardsRemainingInShoe)) / ((double) Blackjack.NUM_CARDS_PER_DECK);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }
}
