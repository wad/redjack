package org.wadhome.redjack;

// This one estimates cards in the discard rack with half-deck precision.
class CardCountMethodHighLowRealistic extends CardCountMethod {

    int runningCount;

    CardCountMethodHighLowRealistic(TableRules tableRules) {
        super(tableRules);
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
