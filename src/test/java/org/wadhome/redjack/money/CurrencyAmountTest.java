package org.wadhome.redjack.money;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrencyAmountTest {
    @Test
    public void testZero() {
        CurrencyAmount currencyAmount = CurrencyAmount.zero();
        assertEquals(0L, currencyAmount.numCents);
    }

    @Test
    public void testComputeHalf() {
        assertEquals("$40.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.halfOf).toString());
    }

    @Test
    public void testComputeSixFifths() {
        assertEquals("$96.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.sixFifthsOf).toString());
    }

    @Test
    public void testComputeOneAndHalf() {
        assertEquals("$120.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.oneAndHalfOf).toString());
    }

    @Test
    public void testComputeDouble() {
        assertEquals("$160.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.doubleOf).toString());
    }

    @Test
    public void testComputeDifference() {
        CurrencyAmount amount1 = new CurrencyAmount(40L);
        CurrencyAmount amount2 = new CurrencyAmount(10L);
        assertEquals("won $30.00", amount1.computeDifference(amount2));
        assertEquals("lost $30.00", amount2.computeDifference(amount1));
    }
}
