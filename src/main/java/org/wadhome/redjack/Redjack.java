package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

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
            case unknown:
                System.out.println("Unknown command: " + commandName);
                break;
            default:
                throw new RuntimeException("Bug.");
        }
    }

    void runBasicStrategyAtTwentyFiveDollarMinimums() {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 500L;
        long initialPlayerBankrollsInCents = 200000L;
        System.out.println("Seven players, each with "
                + new MoneyPile(initialPlayerBankrollsInCents)
                + ", betting " + new MoneyPile(playerFavoriteBetInCents)
                + ", playing " + numRoundsToPlay + " rounds.");

        Display display = new Display(true);
        Casino casino = new Casino("Redjack", Randomness.generateRandomSeed(), display);
        int tableNumber = 0;
        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(2500L);
        tableRules.maxBet = new MoneyPile(100000L);
        casino.createTable(tableNumber, tableRules);
        Table table = casino.getTable(tableNumber);
        table.shuffleAndStuff();

        List<Player> players = new ArrayList<>();
        players.add(new Player(
                "Anne",
                Gender.female,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Bart",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Chris",
                Gender.getRandomGender(casino.getRandomness()),
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Dora",
                Gender.female,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Eddie",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Fran",
                Gender.female,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Gonzo",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                new PlayStrategyBasic(tableRules),
                new MoneyPile(playerFavoriteBetInCents)));

        MoneyPile initialPlayerBankrolls = getSumOfPlayerBankrolls(players, false);

        for (Player player : players) {
            if (table.areAnySeatsAvailable()) {
                table.assignPlayerToSeat(
                        table.getAnAvailableSeatNumber(),
                        player);
            }
        }

        table.playRounds(numRoundsToPlay);

        MoneyPile finalPlayerBankrolls = getSumOfPlayerBankrolls(players, false);

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerBankrolls);
        System.out.println("Final player bankrolls: " + finalPlayerBankrolls);
        System.out.println("Players " + MoneyPile.computeDifference(finalPlayerBankrolls, initialPlayerBankrolls));
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
