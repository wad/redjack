package org.wadhome.redjack;

// This one uses the actual number of remaining cards in the shoe to calculate the true count.
class CardCountHighLowPerfect extends CardCount {

    int runningCount;

    CardCountHighLowPerfect(int numDecks) {
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

    int getTrueCount(int numCardsRemainingInShoe) {
        double numDecksRemaining = ((double) (numCardsRemainingInShoe)) / ((double) Blackjack.NUM_CARDS_PER_DECK);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }
}
