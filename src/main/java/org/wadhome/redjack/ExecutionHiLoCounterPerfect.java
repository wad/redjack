package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionHiLoCounterPerfect extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 1000L;
        long initialPlayerBankrollsInCents = 1000000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(1000L);
        tableRules.maxBet = new MoneyPile(30000L);

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                Randomness.generateRandomSeed(),
                false,
                true);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Perfect Anne");
            add("Perfect Beth");
            add("Perfect Callie");
            add("Perfect Dora");
            add("Perfect Edna");
            add("Perfect Fran");
            add("Perfect Grace");
        }}.stream().map(name -> new Player(
                name,
                Gender.female,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
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
