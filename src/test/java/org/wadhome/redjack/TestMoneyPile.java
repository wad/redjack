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
        MoneyPile pile1 = new MoneyPile(1234);
        MoneyPile pile2 = new MoneyPile(5678);
        pile1.addToPile(pile2);
        assertEquals("$69.12", pile1.toString());
    }
}
