package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.DiscardTray;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.Blackjack;

// This one estimates cards in the discard rack with half-deck precision.
// It also doesn't make plays that would make it obvious that card-counting is happening.
public class CardCountMethodHighLowRealistic extends CardCountMethod {

    private int runningCount;

    public CardCountMethodHighLowRealistic(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
    }

    @Override
    protected void resetCounts() {
        runningCount = 0;
    }

    @Override
    protected CardCountStatus getCardCountStatusHelper() {
        return new CardCountStatusRunningAndTrue(
                runningCount,
                getTrueCount(table.getDiscardTray()));
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
    public void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(getTrueCount(table.getDiscardTray()));
        bettingStrategy.getBet(betRequest);
    }

    public int getRunningCount() {
        return runningCount;
    }

    private int getTrueCount(DiscardTray discardTray) {
        double numDecksInDiscardTray = estimateNumDecksInDiscardTray(discardTray);
        double numDecksRemaining = estimateNumDecksRemainingInShoe(
                tableRules.getNumDecks(),
                numDecksInDiscardTray);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }

    // Accuracy is to half a deck
    static double estimateNumDecksInDiscardTray(DiscardTray discardTray) {
        double numCards = (double) discardTray.numCards();
        return roundToHalf(numCards
                / (double) Blackjack.NUM_CARDS_PER_DECK);
    }

    // Accuracy is to half a deck
    private static double estimateNumDecksRemainingInShoe(
            int numDecks,
            double numDecksInDiscardTray) {
        return ((double) numDecks) - numDecksInDiscardTray;
    }
}
