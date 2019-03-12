package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

// todo: add multithreading

class Redjack {

    public static void main(String... args) {
        if (args.length != 1) {
            String listOfCommands = Arrays
                    .stream(Command.values())
                    .map(Enum::toString)
                    .collect(joining(", "));
            System.out.println("Supply a command. One of " + listOfCommands);
            return;
        }

        Redjack redjack = new Redjack();

        String commandName = args[0];
        Command command = Command.determine(commandName);
        switch (command) {
            case playBasic_100k:
                redjack.runBasicStrategyAtTwentyFiveDollarMinimums();
                break;
            case strategyCompare_basic_vs_highLow:
                redjack.runStrategyComparisonBasicVsHighLow();
                break;
            case unknown:
                System.out.println("Unknown command: " + commandName);
                break;
            default:
                throw new RuntimeException("Bug.");
        }
    }

    private void runBasicStrategyAtTwentyFiveDollarMinimums() {
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
                new Display(true));
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
    }

    private void runStrategyComparisonBasicVsHighLow() {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 10000000L;
        System.out.println("Two players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(2500L);
        tableRules.maxBet = new MoneyPile(30000L);

        long seed = Randomness.generateRandomSeed();
        Casino casino = new Casino(
                "Redjack Strategy Comparison",
                seed,
                new Display(true));
        System.out.println("Seed: " + seed);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<>();
        Player andy = new Player(
                "AndyAdvanced",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyMaxOnGoodCount()),
                new MoneyPile(playerFavoriteBetInCents));
        andy.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(
                BettingStrategyBukofsky.BukofskyBankrollLevel.Level2k);
        players.add(andy);

        players.add(new Player(
                "BobbyBasic",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(playerFavoriteBetInCents)));

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);
        showPlayerResults(initialPlayerBankrollsInCents, players);
    }

    private void assignPlayersToTable(
            List<Player> players,
            Table table) {
        for (Player player : players) {
            if (table.areAnySeatsAvailable()) {
                table.assignPlayerToSeat(
                        table.getAnAvailableSeatNumber(),
                        player);
            }
        }
    }

    private void showPlayerResults(
            long initialPlayerBankrollsInCents,
            List<Player> players) {
        System.out.println();
        for (Player player : players) {
            MoneyPile initial = new MoneyPile(initialPlayerBankrollsInCents);
            System.out.println("Player " + player.getPlayerName()
                    + " started with " + initial
            + " and " + MoneyPile.computeDifference(player.getBankroll(), initial)
            + ".");
        }
    }

    private MoneyPile getSumOfPlayerBankrolls(
            List<Player> players,
            boolean showEach) {
        MoneyPile sum = MoneyPile.zero();
        for (Player player : players) {
            sum.addToPile(player.getBankroll());
            if (showEach) {
                System.out.println(player.getPlayerName() + ": " + player.getBankroll());
            }
        }
        return sum;
    }
}
