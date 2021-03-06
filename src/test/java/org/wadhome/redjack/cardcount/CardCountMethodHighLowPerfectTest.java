package org.wadhome.redjack.cardcount;

import org.junit.Test;
import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class CardCountMethodHighLowPerfectTest extends TestHelper {

    @Test
    public void testCounting() {
        Casino casino = new Casino();
        Table table = casino.createTable(0, new TableRulesForTest());
        CardCountMethodHighLowPerfect method = new CardCountMethodHighLowPerfect(table, new BettingStrategyAlwaysFavorite());
        assertEquals("RC=0 TC=0", method.getCardCountStatus().getReport());

        // not counted
        method.observeCard(c7());
        method.observeCard(c8());
        method.observeCard(c9());

        method.observeCard(c2());
        assertEquals("RC=1 TC=0", method.getCardCountStatus().getReport());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        method.observeCard(c2());
        assertEquals("RC=20 TC=3", method.getCardCountStatus().getReport());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        method.observeCard(cT());
        assertEquals("RC=0 TC=0", method.getCardCountStatus().getReport());
    }
}
