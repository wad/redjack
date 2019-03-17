package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

class ExecutionDemo extends Execution {
    @Override
    Casino execute(Command command) {
        long playerFavoriteBetInCents = 1000L;
        long initialPlayerBankrollsInCents = 200000L;
        System.out.println("Three players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing one shoe.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(500L);
        tableRules.maxBet = new MoneyPile(10000L);

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                true,
                true);
        Table table = casino.createTable(1, tableRules);

        List<Player> players = new ArrayList<>();
        Player alice = new Player(
                "Alice Adventurer",
                Gender.female,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyMaxOnGoodCount()),
                new MoneyPile(playerFavoriteBetInCents));
        alice.setNotes("Alice blatantly uses the hi-low count method."
                + " She bets minimums, except when the count goes to 3, when she bets maximum."
                + " She's going to get caught.");
        players.add(alice);

        Player brandon = new Player(
                "Brandon Basic",
                Gender.male,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(playerFavoriteBetInCents));
        brandon.setNotes("Brandon plays perfect Basic Strategy, doesn't count cards,"
                + " and he always bets his favorite bet.");
        players.add(brandon);

        Player callie = new Player(
                "Callie Careful",
                Gender.female,
                casino,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                new MoneyPile(playerFavoriteBetInCents));
        callie.setNotes("Callie read Bukofsky's book, counts cards using the hi-low method,"
                + " and is playing with 5% risk of ruin, at the $2000 bankroll level, betting accordingly."
                + "\nShe is careful to not be too obvious, avoids certain plays,"
                + " and won't flatline the table minimum bet.");
        players.add(callie);

        assignPlayersToTable(players, table);

        table.playRoundsUntilEndOfShoe();

        return casino;
    }
}
