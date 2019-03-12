package org.wadhome.redjack;

// This one estimates cards in the discard rack with half-deck precision.
// It also doesn't make plays that would make it obvious that card-counting is happening.
class CardCountMethodHighLowRealistic extends CardCountMethod {

    private int runningCount;

    CardCountMethodHighLowRealistic(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
        runningCount = 0;
    }

    @Override
    void observeCard(Card card) {
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
    void observeShuffle() {
        runningCount = 0;
    }

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Player player,
            Randomness randomness) {
        return bettingStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet,
                getTrueCount(table.getDiscardTray()),
                player,
                getRandomness());
    }

    int getRunningCount() {
        return runningCount;
    }

    int getTrueCount(DiscardTray discardTray) {
        double numDecksInDiscardTray = estimateNumDecksInDiscardTray(discardTray);
        double numDecksRemaining = estimateNumDecksRemainingInShoe(
                tableRules.getNumDecks(),
                numDecksInDiscardTray);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }

    // Accuracy is to half a deck
    static double estimateNumDecksInDiscardTray(DiscardTray discardTray) {
        double numCards = (double) discardTray.cards.size();
        double numCardsPerDeck = Blackjack.NUM_CARDS_PER_DECK;
        return roundToHalf(numCards / numCardsPerDeck);
    }

    // Accuracy is to half a deck
    static double estimateNumDecksRemainingInShoe(
            int numDecks,
            double numDecksInDiscardTray) {
        return ((double)numDecks) - numDecksInDiscardTray;
    }
}
