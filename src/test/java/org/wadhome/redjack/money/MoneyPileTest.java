package org.wadhome.redjack.money;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoneyPileTest {
    @Test
    public void testConvertToString() {
        assertEquals("$123.45", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(123L, 45L)).toString());
        assertEquals("$120.00", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(120L)).toString());
        assertEquals("$120.00", MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(0L, 12000L)).toString());
    }
}
