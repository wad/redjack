package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

class ExecutionOneShoe extends Execution {
    @Override
    Casino execute(Command command) {
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 10000000L;
        System.out.println("Two players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing one shoe.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(2500L);
        tableRules.maxBet = new MoneyPile(30000L);

        long seed = Randomness.generateRandomSeed();
        Casino casino = new Casino(
                "Redjack (" + command + ")",
                seed,
                true,
                true);
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
        table.playRoundsUntilEndOfShoe();
        showPlayerResults(initialPlayerBankrollsInCents, players);

        return casino;
    }
}