package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.Blackjack;

// This one uses the actual number of remaining cards in the shoe to calculate the true count.
public class CardCountMethodUstonApcPerfect extends CardCountMethod {

    private int runningCount;
    private int aceCount;

    public CardCountMethodUstonApcPerfect(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
    }

    @Override
    protected void resetCounts() {
        runningCount = 0;
        aceCount = 0;
    }

    @Override
    protected CardCountStatus getCardCountStatusHelper() {
        return new CardCountStatusRunningAndTrueAndAces(
                runningCount,
                getTrueCount(table.getShoe().numCards()),
                aceCount);
    }

    @Override
    public void observeCard(Card card) {
        switch (card.getValue()) {
            case Two:
                runningCount += 1;
                break;
            case Three:
            case Four:
                runningCount += 2;
                break;
            case Five:
                runningCount += 3;
                break;
            case Six:
            case Seven:
                runningCount += 2;
                break;
            case Eight:
                runningCount += 1;
                break;
            case Nine:
                runningCount -= 1;
                break;
            case Ten:
            case Jack:
            case Queen:
            case King:
                runningCount -= 3;
            case Ace:
                aceCount++;
                break;
            default:
                throw new RuntimeException("bug");
        }
    }

    @Override
    public void observeShuffle() {
        runningCount = 0;
        aceCount = 0;
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
        double numHalfDecksRemaining = ((double) numCardsRemainingInShoe)
                / ((double) Blackjack.NUM_CARDS_PER_HALF_DECK);
        double exactTrueCount = ((double) runningCount) / numHalfDecksRemaining;
        return roundToInt(exactTrueCount);
    }
}
