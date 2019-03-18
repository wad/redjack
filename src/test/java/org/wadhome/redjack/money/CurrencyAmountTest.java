package org.wadhome.redjack.money;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrencyAmountTest {
    @Test
    public void testConstructor() {
        assertEquals("$12.00", new CurrencyAmount(12L).toString());
        assertEquals("$12.34", new CurrencyAmount(12L, 34L).toString());
        assertEquals("$12.34", new CurrencyAmount(0L, 1234L).toString());
    }

    @Test
    public void testZero() {
        CurrencyAmount currencyAmount = CurrencyAmount.zero();
        assertEquals("$0.00", currencyAmount.toString());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testCopy() {
        CurrencyAmount initial = new CurrencyAmount(42L);
        CurrencyAmount clone = initial;
        CurrencyAmount copy = initial.copy();
        assertEquals("$42.00", initial.toString());
        assertEquals("$42.00", clone.toString());
        assertEquals("$42.00", copy.toString());

        initial.increaseBy(new CurrencyAmount(3L));

        assertEquals("$45.00", initial.toString());
        assertEquals("$45.00", clone.toString());
        assertEquals("$42.00", copy.toString());
    }

    @Test
    public void testDescribeDifference() {
        CurrencyAmount amount1 = new CurrencyAmount(40L, 4L);
        CurrencyAmount amount2 = new CurrencyAmount(10L, 1L);
        assertEquals("won $30.03", amount1.describeDifference(amount2));
        assertEquals("lost $30.03", amount2.describeDifference(amount1));
    }

    @Test
    public void testIsZero() {
        assertEquals("$0.00", CurrencyAmount.zero().toString());
    }

    @Test
    public void testComparisons() {
        assertTrue(new CurrencyAmount(5L, 2L).isLessThanOrEqualTo(new CurrencyAmount(5L, 3L)));
        assertTrue(new CurrencyAmount(5L, 2L).isLessThanOrEqualTo(new CurrencyAmount(5L, 2L)));
        assertFalse(new CurrencyAmount(5L, 2L).isLessThanOrEqualTo(new CurrencyAmount(5L, 1L)));

        assertTrue(new CurrencyAmount(5L).isLessThanOrEqualTo(new CurrencyAmount(6L)));
        assertTrue(new CurrencyAmount(5L).isLessThanOrEqualTo(new CurrencyAmount(5L)));
        assertFalse(new CurrencyAmount(5L).isLessThanOrEqualTo(new CurrencyAmount(4L)));

        assertTrue(new CurrencyAmount(5L).isLessThan(new CurrencyAmount(6L)));
        assertFalse(new CurrencyAmount(5L).isLessThan(new CurrencyAmount(5L)));
        assertFalse(new CurrencyAmount(5L).isLessThan(new CurrencyAmount(4L)));

        assertFalse(new CurrencyAmount(5L).isGreaterThanOrEqualTo(new CurrencyAmount(6L)));
        assertTrue(new CurrencyAmount(5L).isGreaterThanOrEqualTo(new CurrencyAmount(5L)));
        assertTrue(new CurrencyAmount(5L).isGreaterThanOrEqualTo(new CurrencyAmount(4L)));

        assertFalse(new CurrencyAmount(5L).isGreaterThan(new CurrencyAmount(6L)));
        assertFalse(new CurrencyAmount(5L).isGreaterThan(new CurrencyAmount(5L)));
        assertTrue(new CurrencyAmount(5L).isGreaterThan(new CurrencyAmount(4L)));

        assertFalse(new CurrencyAmount(5L).isGreaterThanOrEqualToDollars(6L));
        assertTrue(new CurrencyAmount(5L).isGreaterThanOrEqualToDollars(5L));
        assertTrue(new CurrencyAmount(5L).isGreaterThanOrEqualToDollars(4L));
    }

    @Test
    public void testCompute() {
        assertEquals("$40.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.halfOf).toString());
        assertEquals("$96.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.sixFifthsOf).toString());
        assertEquals("$120.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.oneAndHalfOf).toString());
        assertEquals("$160.00", (new CurrencyAmount(80L)).compute(CurrencyComputation.doubleOf).toString());
    }

    @Test
    public void testIncreaseAndReduce() {
        CurrencyAmount initial = new CurrencyAmount(12L, 34L);
        initial.increaseBy(new CurrencyAmount(11L, 11L));
        assertEquals("$23.45", initial.toString());
        initial.reduceBy(new CurrencyAmount(10L, 10L));
        assertEquals("$13.35", initial.toString());
    }
}
