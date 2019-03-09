package org.wadhome.redjack;

import java.util.List;

class CardCount {
    int runningCount;
    int aceCount;
    double trueCount;

    CardCount(Table table) {
        runningCount = 0;
        aceCount = 0;
        updateNumbers(table.getAllCardsSeen());
        double numDecksRemaining = ((double) (table.getShoe().cards.size())) / ((double) TableRules.NUM_CARDS_PER_DECK);
        trueCount = ((double) runningCount) / numDecksRemaining;
    }

    int getRunningCount() {
        return runningCount;
    }

    int getAceCount() {
        return aceCount;
    }

    double getTrueCount() {
        return trueCount;
    }

    private void updateNumbers(List<Card> newCardsSeen) {
        for (Card card : newCardsSeen) {
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
                    runningCount--;
                    break;
                case Ace:
                    runningCount--;
                    aceCount++;
                    break;
                default:
                    throw new RuntimeException("bug");
            }
        }
    }
}
