package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShoeTest extends TestHelper {
    @Test
    public void testAddAndDraw() {
        Shoe shoe = new Shoe(0, 2);
        assertEquals(TableRules.NUM_CARDS_PER_DECK * 2, shoe.cards.size());
        shoe.dumpAllCards();

        assertEquals(0, shoe.cards.size());
        shoe.addCardToBottom(c5());
        shoe.addCardToBottom(c6(), c7());
        shoe.addCardToBottom(c8());
        assertEquals(4, shoe.cards.size());
        assertEquals(c5(), shoe.drawTopCard());
        assertEquals(c6(), shoe.drawTopCard());
        assertEquals(c7(), shoe.drawTopCard());
        assertEquals(c8(), shoe.drawTopCard());
        assertEquals(0, shoe.cards.size());
        shoe.addCardToTop(c5());
        shoe.addCardToTop(c6(), c7());
        shoe.addCardToTop(c8());
        assertEquals(4, shoe.cards.size());
        assertEquals(c8(), shoe.drawTopCard());
        assertEquals(c7(), shoe.drawTopCard());
        assertEquals(c6(), shoe.drawTopCard());
        assertEquals(c5(), shoe.drawTopCard());
        assertEquals(0, shoe.cards.size());
    }
}