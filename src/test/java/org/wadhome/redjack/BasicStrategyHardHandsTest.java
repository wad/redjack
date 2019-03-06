package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicStrategyHardHandsTest extends TestHelper {

    @Test
    public void testIt() {
        assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c2()));
    }

    MoneyPile bankroll = new MoneyPile(1000000);
    TableRules rules = TableRules.getHomeCasinoRules();

    BlackjackPlay compute(
            Card playerCard1,
            Card playerCard2,
            Card dealerUpcard) {
        return BasicStrategy.compute(
                new PlayerHand(playerCard1, playerCard2),
                dealerUpcard,
                0,
                bankroll,
                rules);
    }
}
