package org.wadhome.redjack;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicStrategyHardHandsTest
{
    MoneyPile bankroll = new MoneyPile(1000000);
    TableRules rules = TableRules.getHomeCasinoRules();

    Card c2 = Card.two();
    Card c3 = Card.three();
    Card c4 = Card.four();
    Card c5 = Card.five();
    Card c6 = Card.six();
    Card c7 = Card.seven();
    Card c8 = Card.eight();
    Card c9 = Card.nine();
    Card cT = Card.ten();
    Card cA = Card.ace();

    @Test
    public void testIt()
    {
        assertEquals(BlackjackPlay.Hit, compute(c2, c3, c2));
    }

    private BlackjackPlay compute(
            Card playerCard1,
            Card playerCard2,
            Card dealerUpcard)
    {
        return BasicStrategy.compute(
                new PlayerHand(playerCard1, playerCard2),
                dealerUpcard,
                0,
                bankroll,
                rules);
    }
}
