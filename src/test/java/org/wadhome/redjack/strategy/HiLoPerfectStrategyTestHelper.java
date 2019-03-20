package org.wadhome.redjack.strategy;

import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.casino.*;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesForTest;

class HiLoPerfectStrategyTestHelper extends TestHelper {
    private MoneyPile bankroll = MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(10000L));
    private Seat seat = new Seat(SeatNumber.one);

    BlackjackPlay compute(Card... cards) {
        return compute(new TableRulesForTest(), null, cards);
    }

    BlackjackPlay compute(
            CardCountStatus cardCountStatus,
            Card... cards) {
        return compute(new TableRulesForTest(), cardCountStatus, cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            Card... cards) {
        return compute(tableRules, null, cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            CardCountStatus cardCountStatus,
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

        PlayStrategyHighLowPerfect strategy = new PlayStrategyHighLowPerfect(table, new BettingStrategyAlwaysFavorite());
        Player player = new Player(
                "testPlayer",
                Gender.female,
                casino,
                bankroll,
                strategy,
                tableRules.getMinBet());

        if (cardCountStatus != null) {
            strategy.getCardCountMethod().temporarilyOverrideCardCountStatus(cardCountStatus);
        }

        return strategy.choosePlay(
                player,
                playerHand,
                dealerUpcard);
    }
}
