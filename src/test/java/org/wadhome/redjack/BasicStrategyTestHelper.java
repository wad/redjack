package org.wadhome.redjack;

public class BasicStrategyTestHelper extends TestHelper {

    MoneyPile bankroll = new MoneyPile(1000000);
    TableRules rules = TableRules.getHomeCasinoRules();

    // todo: upcate to take more than 3 cards
    BlackjackPlay compute(
            Card playerCard1,
            Card playerCard2,
            Card dealerUpcard) {
        return BasicStrategy.compute(
                new PlayerHand(playerCard1, playerCard2),
                dealerUpcard,
                0,
                bankroll,
                rules);
    }
}
