package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionBasic extends Execution {
    @Override
    Casino execute() {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 1000000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(2500L);
        tableRules.maxBet = new MoneyPile(100000L);

        Casino casino = new Casino(
                "Redjack",
                Randomness.generateRandomSeed(),
                true,
                true);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Anne");
            add("Beth");
            add("Callie");
            add("Dora");
            add("Edna");
            add("Fran");
            add("Grace");
        }}.stream().map(name -> new Player(
                name,
                Gender.female,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(playerFavoriteBetInCents))).
                collect(toList());

        MoneyPile initialPlayerBankrolls = getSumOfPlayerBankrolls(players, false);

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        MoneyPile finalPlayerBankrolls = getSumOfPlayerBankrolls(players, false);

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerBankrolls);
        System.out.println("Final player bankrolls: " + finalPlayerBankrolls);
        System.out.println("Players " + MoneyPile.computeDifference(finalPlayerBankrolls, initialPlayerBankrolls));

        return casino;
    }
}
