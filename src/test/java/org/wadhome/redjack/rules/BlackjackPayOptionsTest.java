package org.wadhome.redjack.rules;

import org.junit.Test;
import org.wadhome.redjack.money.CurrencyAmount;

import static org.junit.Assert.assertEquals;

public class BlackjackPayOptionsTest {
    @Test
    public void testComputePlayerBlackjackWinnings() {
        assertEquals("$15.00", BlackjackPayOptions.ThreeToTwo.computePlayerBlackjackWinnings(new CurrencyAmount(10L)).toString());
        assertEquals("$20.00", BlackjackPayOptions.TwoToOne.computePlayerBlackjackWinnings(new CurrencyAmount(10L)).toString());
        assertEquals("$12.00", BlackjackPayOptions.SixToFive.computePlayerBlackjackWinnings(new CurrencyAmount(10L)).toString());
    }
}
