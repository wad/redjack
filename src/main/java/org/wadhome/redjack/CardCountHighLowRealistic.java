package org.wadhome.redjack;

// This one estimates cards in the discard rack with half-deck precision.
class CardCountHighLowRealistic extends CardCount {

    int runningCount;

    CardCountHighLowRealistic(int numDecks) {
        super(numDecks);
        runningCount = 0;
    }

    @Override
    void observeCard(Card newCardsSeen) {
        switch (newCardsSeen.getValue()) {
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
    void newShoe() {
        runningCount = 0;
    }

    int getTrueCount(DiscardTray discardTray) {
        double numDecksInDiscardTray = estimateNumDecksInDiscardTray(discardTray);
        double numDecksRemaining = estimateNumDecksRemaining(numDecksInDiscardTray);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }

    // Accuracy is to half a deck
    static double estimateNumDecksInDiscardTray(DiscardTray discardTray) {
        double numCards = (double) discardTray.cards.size();
        double numCardsPerDeck = Blackjack.NUM_CARDS_PER_DECK;

        // todo
        return 0D;
    }

    // Accuracy is to half a deck
    static double estimateNumDecksRemaining(double numDecksInDiscardTray) {
        // todo
        return 0D;
    }
}
