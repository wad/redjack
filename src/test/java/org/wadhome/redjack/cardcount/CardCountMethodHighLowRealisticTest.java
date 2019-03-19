package org.wadhome.redjack.cardcount;

import org.junit.Test;
import org.wadhome.redjack.Casino;
import org.wadhome.redjack.DiscardTray;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class CardCountMethodHighLowRealisticTest extends TestHelper {

    @Test
    public void testCounting() {
        Casino casino = new Casino();
        Table table = casino.createTable(0, new TableRulesForTest());
        CardCountMethodHighLowRealistic method = new CardCountMethodHighLowRealistic(table, new BettingStrategyAlwaysFavorite());
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

    @Test
    public void testEstimateNumDecksInDiscardTray() {
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(0))));
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1))));
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(12))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(13))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(14))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(25))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(26))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(27))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(38))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(39))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(40))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(51))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(52))));
    }

    private DiscardTray makeDiscardTray(int numCards) {
        DiscardTray discardTray = new DiscardTray(1);
        for (int i = 0; i < numCards; i++) {
            discardTray.addCardToBottom(cA());
        }
        return discardTray;
    }
}
