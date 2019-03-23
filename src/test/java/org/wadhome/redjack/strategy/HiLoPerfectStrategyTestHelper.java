package org.wadhome.redjack.strategy;

import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrue;
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
        return compute(
                false,
                new TableRulesForTest(),
                null, cards);
    }

    BlackjackPlay computeCannotSplit(Card... cards) {
        return compute(
                true,
                new TableRulesForTest(),
                null, cards);
    }

    BlackjackPlay compute(
            CardCountStatus cardCountStatus,
            Card... cards) {
        return compute(
                false,
                new TableRulesForTest(),
                cardCountStatus,
                cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            Card... cards) {
        return compute(
                false,
                tableRules,
                null,
                cards);
    }

    BlackjackPlay compute(
            TableRules tableRules,
            CardCountStatus cardCountStatus,
            Card... cards) {
        return compute(
                false,
                tableRules, cardCountStatus,
                cards);
    }

    BlackjackPlay compute(
            boolean preventSplitting,
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
                preventSplitting ? MoneyPile.extractMoneyFromFederalReserve(CurrencyAmount.zero()) : bankroll,
                strategy,
                tableRules.getMinBet());

        if (cardCountStatus != null) {
            strategy.getCardCountMethod().temporarilyOverrideCardCountStatus(cardCountStatus);
        }

        return strategy.choosePlay(
                player,
                playerHand,
                dealerUpcard).getBlackjackPlay();
    }

    CurrencyAmount computeInsuranceBet(int trueCount) {
        Casino casino = new Casino();
        Table table = casino.createTable(0, new TableRulesForTest());
        Seat seat = new Seat(SeatNumber.one);
        Player player = new Player(
                "Zack",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(1000L)),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(30L));
        PlayerHand hand = new PlayerHand(seat);
        hand.addCard(cT());
        hand.addCard(cT());
        CardCountStatusRunningAndTrue cardCountStatus = new CardCountStatusRunningAndTrue(0, trueCount);
        player.getPlayStrategy().getCardCountMethod().temporarilyOverrideCardCountStatus(cardCountStatus);
        return player.getPlayStrategy().getInsuranceBet(
                new CurrencyAmount(15L),
                hand,
                cT(),
                new CurrencyAmount(500L));
    }
}
