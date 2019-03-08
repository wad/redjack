package org.wadhome.redjack;

public class BasicStrategyTestHelper extends TestHelper {

    private MoneyPile bankroll = new MoneyPile(1000000);
    private TableRules tableRules = TableRules.getDefaultRules();
    private Seat seat = new Seat(SeatNumber.one);

    BlackjackPlay compute(Card... cards) {
        return compute(this.tableRules, cards);
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

        PlayerHand playerHand = new PlayerHand(seat);
        for (int i = 0; i < numCards - 1; i++)
        {
            playerHand.addCard(cards[i]);
        }

        return BasicStrategy.choosePlay(
                playerHand,
                dealerUpcard,
                bankroll,
                tableRules);
    }
}
