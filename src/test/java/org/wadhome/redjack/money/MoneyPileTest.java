package org.wadhome.redjack.money;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoneyPileTest {
    @Test
    public void testConvertToString() {
        assertEquals("$123.45", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(123L, 45L)).toString());
        assertEquals("$120.00", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(120L)).toString());
        assertEquals("$120.00", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(0L, 12000L)).toString());
    }

    @Test
    public void testMoveMoneyToTargetPile() {
        MoneyPile pile1 = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L));
        MoneyPile pile2 = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(10L));
        pile1.moveMoneyToTargetPile(pile2, new CurrencyAmount(3L, 5L));
        assertEquals("$96.95", pile1.toString());
        assertEquals("$13.05", pile2.toString());
    }

    @Test
    public void testGetCurrentAmountCopy() {
        MoneyPile pile = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L));
        CurrencyAmount currencyAmountCopy = pile.getCurrencyAmountCopy();
        currencyAmountCopy.increaseBy(new CurrencyAmount(5L));
        assertEquals("$100.00", pile.toString());
        assertEquals("$105.00", currencyAmountCopy.toString());
    }

    @Test
    public void testCanExtract() {
        MoneyPile pile = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L));
        assertTrue(pile.canExtract(new CurrencyAmount(99L, 99L)));
        assertTrue(pile.canExtract(new CurrencyAmount(100L)));
        assertFalse(pile.canExtract(new CurrencyAmount(100L, 1L)));
    }
}
