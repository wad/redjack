package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionRealisticBukBut4k extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 500L;
        long initialPlayerBankrollsInCents = 400000L;
        long retirementBankroll = initialPlayerBankrollsInCents + 100000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing until " + new MoneyPile(retirementBankroll) + ", bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(500L);
        tableRules.maxBet = new MoneyPile(10000L);

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                Randomness.generateRandomSeed(),
                false,
                true);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Real Buky Anne");
            add("Real Buky Beth");
            add("Real Buky Callie");
            add("Real Buky Dora");
            add("Real Buky Edna");
            add("Real Buky Fran");
            add("Real Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = new MoneyPile(initialPlayerBankrollsInCents);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                    new MoneyPile(playerFavoriteBetInCents));
            player.setRetirementTriggerBankroll(new MoneyPile(retirementBankroll));
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
