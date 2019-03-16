package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoneyPileTest {
    @Test
    public void testConvertToString() {
        assertEquals("$123.45", new MoneyPile(12345L).toString());
        assertEquals("$120.00", new MoneyPile(12000L).toString());
    }

    @Test
    public void testAdd() {
        MoneyPile pile1 = new MoneyPile(1234L);
        MoneyPile pile2 = new MoneyPile(5678L);
        pile1.addToPile(pile2);
        assertEquals("$69.12", pile1.toString());
    }

    @Test
    public void testComputeHalf() {
        assertEquals("$40.00", (new MoneyPile(8000L)).computeHalf().toString());
        assertEquals("$0.00", (new MoneyPile(0L)).computeHalf().toString());
    }

    @Test
    public void testComputeSixFifths() {
        assertEquals("$96.00", (new MoneyPile(8000L)).computeSixFifths().toString());
        assertEquals("$0.00", (new MoneyPile(0L)).computeSixFifths().toString());
    }

    @Test
    public void testComputeOneAndHalf() {
        assertEquals("$120.00", (new MoneyPile(8000L)).computeOneAndHalf().toString());
        assertEquals("$0.00", (new MoneyPile(0L)).computeOneAndHalf().toString());
    }

    @Test
    public void testComputeDouble() {
        assertEquals("$160.00", (new MoneyPile(8000L)).computeDouble().toString());
        assertEquals("$0.00", (new MoneyPile(0L)).computeDouble().toString());
    }

    @Test
    public void testComputeDifference() {
        MoneyPile pile1 = new MoneyPile(4000L);
        MoneyPile pile2 = new MoneyPile(1000L);
        assertEquals("won $30.00", MoneyPile.computeDifference(pile1, pile2));
        assertEquals("lost $30.00", MoneyPile.computeDifference(pile2, pile1));
    }
}
