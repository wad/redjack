package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.*;

public class HandTest extends TestHelper {
    @Test
    public void testIsBustSimple() {
        Hand hand = new PlayerHand(0);
        assertFalse(hand.isBust());
        hand.addCard(c2());
        assertFalse(hand.isBust());
        hand.addCard(cT());
        assertFalse(hand.isBust());
        hand.addCard(cT());
        assertTrue(hand.isBust());
    }

    @Test
    public void testIsBustWithAce() {
        Hand hand = new PlayerHand(0);
        hand.addCard(cA());
        assertFalse(hand.isBust());
        hand.addCard(c5());
        assertFalse(hand.isBust());
        hand.addCard(c2());
        assertFalse(hand.isBust());
        hand.addCard(cT());
        assertFalse(hand.isBust());
        hand.addCard(cT());
        assertTrue(hand.isBust());
    }

    @Test
    public void testIsBlackjack() {
        Hand hand = new PlayerHand(0);
        hand.addCard(cA());
        assertFalse(hand.isBlackjack());
        hand.addCard(cT());
        assertTrue(hand.isBlackjack());
        hand.addCard(cT());
        assertFalse(hand.isBlackjack());
    }

    @Test
    public void testIsBlackjackNoAces() {
        Hand hand = new PlayerHand(0);
        hand.addCard(c2());
        assertFalse(hand.isBlackjack());
        hand.addCard(cT());
        assertFalse(hand.isBlackjack());
    }

    @Test
    public void testCompareSame() {
        Hand hand1 = new PlayerHand(1);
        hand1.addCard(c2());
        hand1.addCard(cT());
        Hand hand2 = new PlayerHand(2);
        hand2.addCard(c2());
        hand2.addCard(cT());

        assertEquals(ComparisonResult.same, hand1.compareWith(hand2));
    }

    @Test
    public void testCompareNotSame() {
        Hand hand1 = new PlayerHand(1);
        hand1.addCard(c2());
        hand1.addCard(c9());
        Hand hand2 = new PlayerHand(2);
        hand2.addCard(c2());
        hand2.addCard(cT());

        assertEquals(ComparisonResult.ThisLoses, hand1.compareWith(hand2));
        assertEquals(ComparisonResult.ThisWins, hand2.compareWith(hand1));
    }
}
