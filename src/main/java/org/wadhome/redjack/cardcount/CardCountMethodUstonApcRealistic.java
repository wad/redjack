package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.DiscardTray;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.Blackjack;

// This one estimates cards in the discard rack with half-deck precision.
// It also doesn't make plays that would make it obvious that card-counting is happening.
public class CardCountMethodUstonApcRealistic extends CardCountMethod {

    private int runningCount;
    private int aceCount;

    public CardCountMethodUstonApcRealistic(
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
                getTrueCountForPlay(table.getDiscardTray()),
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
    public void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(getTrueCountForBetting(table.getDiscardTray()));
        bettingStrategy.getBet(betRequest);
    }

    public int getRunningCount() {
        return runningCount;
    }

    // positive numbers indicate this is how many aces above average remain
    // negative numbers indicate this is how many aces below the average density remain
    private int getAceWealth(int numCardsRemainingInShoe) {
        int numAcesToExpectRemainAtThisPoint = getNumQuarterDecksLeftInShoe(numCardsRemainingInShoe);
        return numAcesToExpectRemainAtThisPoint - aceCount;
    }

    private int getNumQuarterDecksLeftInShoe(int numCardsRemainingInShoe) {
        return roundToInt((double) numCardsRemainingInShoe
                / (double) Blackjack.NUM_CARDS_PER_QUARTER_DECK);
    }

    private int getTrueCount(
            DiscardTray discardTray,
            int runningCount) {
        double numHalfDecksInDiscardTray = estimateNumHalfDecksInDiscardTray(discardTray);
        double numHalfDecksRemaining = estimateNumHalfDecksRemainingInShoe(
                tableRules.getNumDecks(),
                numHalfDecksInDiscardTray);
        return roundToInt(((double) runningCount) / numHalfDecksRemaining);
    }

    // Accuracy is to half a deck
    private static int estimateNumHalfDecksInDiscardTray(DiscardTray discardTray) {
        return roundToInt((double) discardTray.numCards()
                / (double) Blackjack.NUM_CARDS_PER_HALF_DECK);
    }

    // Accuracy is to half a deck
    private static double estimateNumHalfDecksRemainingInShoe(
            int numDecks,
            double numHalfDecksInDiscardTray) {
        return ((double) (numDecks << 1) - numHalfDecksInDiscardTray);
    }
}
