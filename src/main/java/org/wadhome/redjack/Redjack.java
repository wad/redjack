package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

public class Redjack {

    public static void main(String... args) {
        Redjack redjack = new Redjack();
        redjack.runBasicStrategyAtTwentyFiveDollarMinimums();
    }

    private void runBasicStrategyAtTwentyFiveDollarMinimums() {
        Display display = new Display();
        Casino casino = new Casino("Redjack", display);
        int tableNumber = 0;
        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.minBet = new MoneyPile(2500L);
        casino.createTable(tableNumber, tableRules);
        Table table = casino.getTable(tableNumber);
        table.shuffleAndStuff();

        List<Player> players = new ArrayList<>();
        players.add(new Player(
                "Anne",
                Gender.female,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Bart",
                Gender.male,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Chris",
                Gender.getRandomGender(casino.getRandomness()),
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Dora",
                Gender.female,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Eddie",
                Gender.male,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Fran",
                Gender.female,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));
        players.add(new Player(
                "Gonzo",
                Gender.male,
                new MoneyPile(1000000L),
                PlayerStrategy.BasicStrategy,
                new MoneyPile(2500L)));

        MoneyPile initialPlayerBankrolls = getSumOfPlayerBankrolls(players, false);
        MoneyPile initialCasinoBankroll = casino.getBankroll().copy();

        for (Player player : players) {
            if (table.areAnySeatsAvailable()) {
                table.assignPlayerToSeat(
                        table.getAnAvailableSeatNumber(),
                        player);
            }
        }

        int numRoundsToPlay = 10000;
        display.setMute(true);
        for (int roundNumber = 0; roundNumber < numRoundsToPlay; roundNumber++) {
            System.out.println("===========> Round " + roundNumber + " <===========");
            if (table.playRound()) {
                table.shuffleAndStuff();
            }
        }

        MoneyPile finalPlayerBankrolls = getSumOfPlayerBankrolls(players, true);
        MoneyPile finalCasinoBankroll = casino.getBankroll().copy();

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerBankrolls);
        System.out.println("Final player bankrolls: " + finalPlayerBankrolls);
        String casinoEarnings = MoneyPile.computeDifference(initialCasinoBankroll, finalCasinoBankroll);
        System.out.println("Casino started with " + initialCasinoBankroll
                + " and ended with " + finalCasinoBankroll
                + ", earning " + casinoEarnings + ".");
        System.out.println("Casino earned " + casinoEarnings);
        System.out.println("Players earned " + MoneyPile.computeDifference(initialPlayerBankrolls, finalPlayerBankrolls));
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
