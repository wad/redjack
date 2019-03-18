package org.wadhome.redjack.cardcount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardCountMethodTest {
    @Test
    public void testRoundToIn() {
        assertEquals(5, CardCountMethod.roundToInt(5.49D));
        assertEquals(6, CardCountMethod.roundToInt(5.5D));
        assertEquals(6, CardCountMethod.roundToInt(6.49D));
        assertEquals(7, CardCountMethod.roundToInt(6.5D));
    }

    @Test
    public void testRoundToHalf() {
        assertEquals("6.0", String.valueOf(CardCountMethod.roundToHalf(6.24D)));
        assertEquals("6.5", String.valueOf(CardCountMethod.roundToHalf(6.25D)));
        assertEquals("6.5", String.valueOf(CardCountMethod.roundToHalf(6.26D)));
        assertEquals("6.5", String.valueOf(CardCountMethod.roundToHalf(6.4D)));
        assertEquals("6.5", String.valueOf(CardCountMethod.roundToHalf(6.5D)));
        assertEquals("6.5", String.valueOf(CardCountMethod.roundToHalf(6.6D)));
    }
}
