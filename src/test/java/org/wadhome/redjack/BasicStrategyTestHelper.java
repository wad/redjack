package org.wadhome.redjack;

public class BasicStrategyTestHelper extends TestHelper {

    MoneyPile bankroll = new MoneyPile(1000000);
    TableRules rules = TableRules.getHomeCasinoRules();

    BlackjackPlay compute(Card... cards) {
        return compute(this.rules, cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            Card... cards) {
        int numCards = cards.length;
        if (numCards < 3)
        {
            throw new RuntimeException("Bug in test code!");
        }
        Card dealerUpcard = cards[numCards - 1];

        PlayerHand playerHand = new PlayerHand(0);
        for (int i = 0; i < numCards - 1; i++)
        {
            playerHand.addCard(cards[i]);
        }

        return BasicStrategy.compute(
                playerHand,
                dealerUpcard,
                0,
                bankroll,
                tableRules);
    }
}
