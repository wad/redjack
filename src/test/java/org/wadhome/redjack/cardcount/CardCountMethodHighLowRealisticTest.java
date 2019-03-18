package org.wadhome.redjack.cardcount;

import org.junit.Test;
import org.wadhome.redjack.DiscardTray;
import org.wadhome.redjack.TestHelper;

import static org.junit.Assert.assertEquals;

public class CardCountMethodHighLowRealisticTest extends TestHelper {

    @Test
    public void testEstimateNumDecksInDiscardTray() {
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 0))));
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 1))));
        assertEquals("0.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 12))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 13))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 14))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 25))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 26))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 27))));
        assertEquals("0.5", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 38))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 39))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 40))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 51))));
        assertEquals("1.0", String.valueOf(CardCountMethodHighLowRealistic.estimateNumDecksInDiscardTray(makeDiscardTray(1, 52))));
    }

    private DiscardTray makeDiscardTray(
            int numDecks,
            int numCards) {
        DiscardTray discardTray = new DiscardTray(numDecks);
        for (int i = 0; i < numCards; i++) {
            discardTray.addCardToBottom(cA());
        }
        return discardTray;
    }
}