package org.wadhome.redjack;

import org.wadhome.redjack.rules.TableRules;

import java.util.ArrayList;
import java.util.List;

import static org.wadhome.redjack.rules.Blackjack.DEALER_STAND_TOTAL;

class DealerHand extends Hand {

    private static final int SEVEN = Value.Ace.getPoints() + Value.Six.getPoints();
    private static final int INDEX_OF_HOLE_CARD = 1;

    private boolean isHoleCardRevealed = false;

    boolean shouldHit(TableRules tableRules) {
        boolean isSoftSeventeen = hasAtLeastOneAce() && computeMinSum() == SEVEN;
        if (isSoftSeventeen && tableRules.mustHitSoftSeventeen()) {
            return true;
        }

        return computeMaxSum() < DEALER_STAND_TOTAL;
    }

    void revealHoleCard(Table table) {
        this.isHoleCardRevealed = true;
        table.showCard(getSecondCard());
    }

    @Override
    List<Card> removeCards() {
        this.isHoleCardRevealed = false;
        return super.removeCards();
    }

    List<Card> getVisibleCards() {
        List<Card> cardsSeen = new ArrayList<>(cards);
        if (getSecondCard() != null && !isHoleCardRevealed) {
            cards.remove(INDEX_OF_HOLE_CARD);
        }
        return cardsSeen;
    }
}
