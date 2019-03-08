package org.wadhome.redjack;

import static org.wadhome.redjack.TableRules.DEALER_STAND_TOTAL;

class DealerHand extends Hand {

    private static final int SEVEN = Value.Ace.getPoints() + Value.Six.getPoints();

    boolean shouldHit(TableRules tableRules) {
        boolean isSoftSeventeen = hasAtLeastOneAce() && computeMinSum() == SEVEN;
        if (isSoftSeventeen && tableRules.mustHitSoftSeventeen()) {
            return true;
        }

        return computeMaxSum() < DEALER_STAND_TOTAL;
    }
}
