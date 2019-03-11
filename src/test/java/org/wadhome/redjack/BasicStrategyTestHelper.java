package org.wadhome.redjack;

public class BasicStrategyTestHelper extends TestHelper {

    private MoneyPile bankroll = new MoneyPile(1000000);
    private TableRules tableRules = TableRules.getDefaultRules();
    private Seat seat = new Seat(SeatNumber.one);
    private PlayStrategyBasic basicStrategy = new PlayStrategyBasic(tableRules);

    BlackjackPlay compute(Card... cards) {
        return compute(this.tableRules, cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            Card... cards) {
        Table table = new Table(new Casino("test"), 0, tableRules);
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

        return basicStrategy.choosePlay(
                playerHand,
                dealerUpcard,
                bankroll,
                table);
    }
}
