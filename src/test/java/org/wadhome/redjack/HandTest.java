package org.wadhome.redjack;

import static org.junit.Assert.*;

import org.junit.Test;

public class HandTest
{
    @Test
    public void testIsBustSimple()
    {
        Hand hand = new PlayerHand(0);
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Two));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Queen));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Jack));
        assertTrue(hand.isBust());
    }

    @Test
    public void testIsBustWithAce()
    {
        Hand hand = new PlayerHand(0);
        hand.addCard(new Card(Value.Ace));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Five));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Two));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Ten));
        assertFalse(hand.isBust());
        hand.addCard(new Card(Value.Jack));
        assertTrue(hand.isBust());
    }

    @Test
    public void testIsBlackjack()
    {
        Hand hand = new PlayerHand(0);
        hand.addCard(new Card(Value.Ace));
        assertFalse(hand.isBlackjack());
        hand.addCard(new Card(Value.Queen));
        assertTrue(hand.isBlackjack());
        hand.addCard(new Card(Value.Jack));
        assertFalse(hand.isBlackjack());
    }

    @Test
    public void testIsBlackjackNoAces()
    {
        Hand hand = new PlayerHand(0);
        hand.addCard(new Card(Value.Two));
        assertFalse(hand.isBlackjack());
        hand.addCard(new Card(Value.Queen));
        assertFalse(hand.isBlackjack());
    }

    @Test
    public void testCompareSame()
    {
        Hand hand1 = new PlayerHand(1);
        hand1.addCard(new Card(Value.Two));
        hand1.addCard(new Card(Value.Queen));
        Hand hand2 = new PlayerHand(2);
        hand2.addCard(new Card(Value.Two));
        hand2.addCard(new Card(Value.Queen));

        assertEquals(ComparisonResult.same, hand1.compareWith(hand2));
    }

    @Test
    public void testCompareNotSame()
    {
        Hand hand1 = new PlayerHand(1);
        hand1.addCard(new Card(Value.Two));
        hand1.addCard(new Card(Value.Nine));
        Hand hand2 = new PlayerHand(2);
        hand2.addCard(new Card(Value.Two));
        hand2.addCard(new Card(Value.Queen));

        assertEquals(ComparisonResult.ThisLoses, hand1.compareWith(hand2));
        assertEquals(ComparisonResult.ThisWins, hand2.compareWith(hand1));
    }
}
