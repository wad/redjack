package org.wadhome.redjack.execution;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionRealisticBuk10k extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 1000000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing until double, bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.setMinBet(new MoneyPile(2500L));
        tableRules.setMaxBet(new MoneyPile(50000L));

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Buky Anne");
            add("Buky Beth");
            add("Buky Callie");
            add("Buky Dora");
            add("Buky Edna");
            add("Buky Fran");
            add("Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = new MoneyPile(initialPlayerBankrollsInCents);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
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
