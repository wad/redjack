package org.wadhome.redjack;

import java.util.Collections;
import java.util.Set;

import static org.wadhome.redjack.TableRules.DEALER_STAND_TOTAL;

public class DealerHand extends Hand {

    boolean shouldHit(TableRules tableRules) {
        final int seven = Value.Ace.getPoints() + Value.Six.getPoints();
        boolean isSoftSeventeen = hasAtLeastOneAce() && computeMinSum() == seven;
        if (isSoftSeventeen && tableRules.mustHitSoftSeventeen()) {
            return true;
        }

        return computeMaxSum() < DEALER_STAND_TOTAL;
    }

    @Override
    protected boolean hasAnyCardsHelper() {
        return false;
    }

    @Override
    protected Set<Card> removeCardsHelper() {
        return Collections.emptySet();
    }
}
