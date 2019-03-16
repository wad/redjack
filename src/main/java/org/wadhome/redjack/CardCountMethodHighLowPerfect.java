package org.wadhome.redjack;

// This one uses the actual number of remaining cards in the shoe to calculate the true count.
class CardCountMethodHighLowPerfect extends CardCountMethod {

    private int runningCount;

    CardCountMethodHighLowPerfect(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
        runningCount = 0;
    }

    @Override
    CardCountStatus getCardCountStatus() {
        return new CardCountStatusRunningAndTrue(
                runningCount,
                getTrueCount(table.getShoe().cards.size()));
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
    void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(getTrueCount(table.getShoe().cards.size()));
        bettingStrategy.getBet(betRequest);
    }

    int getRunningCount() {
        return runningCount;
    }

    int getTrueCount(int numCardsRemainingInShoe) {
        double numDecksRemaining = ((double) (numCardsRemainingInShoe)) / ((double) Blackjack.NUM_CARDS_PER_DECK);
        double exactTrueCount = ((double) runningCount) / numDecksRemaining;
        return roundToInt(exactTrueCount);
    }
}
