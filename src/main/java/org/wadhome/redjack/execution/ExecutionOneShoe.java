package org.wadhome.redjack.execution;

import org.wadhome.redjack.Casino;
import org.wadhome.redjack.Gender;
import org.wadhome.redjack.Player;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyBasic;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

class ExecutionOneShoe extends Execution {
    @Override
    Casino execute(Command command) {
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(25L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(100000L);
        System.out.println("Two players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing one shoe.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.setMinBet(new CurrencyAmount(25L));
        tableRules.setMaxBet(new CurrencyAmount(300L));

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                true,
                true);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<>();
        Player andy = new Player(
                "AndyAdvancedPerfect",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                playerFavoriteBet);
        andy.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(
                BukofskyBankrollLevel.Level20k);
        players.add(andy);

        Player andy2 = new Player(
                "AndyAdvancedRealistic",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                playerFavoriteBet);
        andy2.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(
                BukofskyBankrollLevel.Level20k);
        players.add(andy2);

        players.add(new Player(
                "BobbyBasic",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                playerFavoriteBet));

        assignPlayersToTable(players, table);
        table.playRoundsUntilEndOfShoe();

        return casino;
    }
}
