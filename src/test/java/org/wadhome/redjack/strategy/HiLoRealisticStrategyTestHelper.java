package org.wadhome.redjack.strategy;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesForTest;

public class HiLoRealisticStrategyTestHelper extends TestHelper {
    private MoneyPile bankroll = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(10000L));
    private Seat seat = new Seat(SeatNumber.one);

    BlackjackPlay compute(Card... cards) {
        return compute(new TableRulesForTest(), cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            Card... cards) {
        Casino casino = new Casino();
        Table table = casino.createTable(0, tableRules);
        int numCards = cards.length;
        if (numCards < 3) {
            throw new RuntimeException("Bug in test code!");
        }
        Card dealerUpcard = cards[numCards - 1];

        PlayerHand playerHand = new PlayerHand(seat);
        for (int i = 0; i < numCards - 1; i++) {
            playerHand.addCard(cards[i]);
        }

        PlayStrategyHighLowRealistic strategy = new PlayStrategyHighLowRealistic(table, new BettingStrategyAlwaysFavorite());
        Player player = new Player(
                "testPlayer",
                Gender.female,
                casino,
                bankroll,
                strategy,
                tableRules.getMinBet());
        return strategy.choosePlay(
                player,
                playerHand,
                dealerUpcard);
    }
}
