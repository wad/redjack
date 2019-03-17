package org.wadhome.redjack.execution;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyBasic;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

public class Execution100k extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 10000000L;
        System.out.println("Two players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.setMinBet(new MoneyPile(2500L));
        tableRules.setMaxBet(new MoneyPile(30000L));

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<>();
        Player andy = new Player(
                "AndyAdvancedPerfect",
                Gender.male,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                new MoneyPile(playerFavoriteBetInCents));
        andy.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(
                BukofskyBankrollLevel.Level20k);
        players.add(andy);

        Player andy2 = new Player(
                "AndyAdvancedRealistic",
                Gender.male,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                new MoneyPile(playerFavoriteBetInCents));
        andy2.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(
                BukofskyBankrollLevel.Level20k);
        players.add(andy2);

        players.add(new Player(
                "BobbyBasic",
                Gender.male,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(playerFavoriteBetInCents)));

        assignPlayersToTable(players, table);

        table.playRounds(numRoundsToPlay);

        return casino;
    }
}
