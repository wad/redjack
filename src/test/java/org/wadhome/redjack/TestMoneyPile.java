package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMoneyPile {
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
}
