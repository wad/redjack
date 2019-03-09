package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

public class Redjack {

    public static void main(String... args) {
        Redjack redjack = new Redjack();
        redjack.runBasicStrategyAtTwentyFiveDollarMinimums();
    }

    private void runBasicStrategyAtTwentyFiveDollarMinimums() {
        int numRoundsToPlay = 100000;
        long playerFavoriteBetInCents = 2500L;
        long initialPlayerBankrollsInCents = 10000000L;

        Display display = new Display();
        Casino casino = new Casino("Redjack", display);
        display.setMute(true);
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
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Bart",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Chris",
                Gender.getRandomGender(casino.getRandomness()),
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Dora",
                Gender.female,
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Eddie",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Fran",
                Gender.female,
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));
        players.add(new Player(
                "Gonzo",
                Gender.male,
                new MoneyPile(initialPlayerBankrollsInCents),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(playerFavoriteBetInCents)));

        MoneyPile initialPlayerBankrolls = getSumOfPlayerBankrolls(players, false);

        for (Player player : players) {
            if (table.areAnySeatsAvailable()) {
                table.assignPlayerToSeat(
                        table.getAnAvailableSeatNumber(),
                        player);
            }
        }

        System.out.print("Running " + numRoundsToPlay + " rounds of play");
        for (int roundNumber = 0; roundNumber < numRoundsToPlay; roundNumber++) {
            if (roundNumber % 1000 == 0) {
                System.out.print(".");
            }
            if (table.playRound()) {
                table.shuffleAndStuff();
            }
        }
        System.out.println();

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
