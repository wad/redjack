package org.wadhome.redjack.execution;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionPerfectBuk extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 500L;
        long initialPlayerBankrollsInCents = 200000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing until double, bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.setMinBet(new MoneyPile(500L));
        tableRules.setMaxBet(new MoneyPile(10000L));

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Perfect Buky Anne");
            add("Perfect Buky Beth");
            add("Perfect Buky Callie");
            add("Perfect Buky Dora");
            add("Perfect Buky Edna");
            add("Perfect Buky Fran");
            add("Perfect Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = new MoneyPile(initialPlayerBankrollsInCents);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                    new MoneyPile(playerFavoriteBetInCents));
            player.setRetirementTriggerBankroll(initialPlayerBankroll.computeDouble());
            return player;
        }).collect(toList());

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        System.out.println();
        System.out.println("Retired players: " + countRetiredPlayers(players)
                + ". Bankrupt players: " + countBankruptPlayers(players) + ".");

        return casino;
    }
}
