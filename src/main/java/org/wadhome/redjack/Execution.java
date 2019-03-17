package org.wadhome.redjack;

import java.util.List;

abstract class Execution {
    abstract Casino execute(Command command);

    void assignPlayersToTable(
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

    void showPlayerResults(
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

    MoneyPile getSumOfPlayerBankrolls(
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

    int countBankruptPlayers(List<Player> players) {
        int numBankruptPlayers = 0;
        for (Player player : players) {
            if (player.isBankrupt()) {
                numBankruptPlayers++;
            }
        }
        return numBankruptPlayers;
    }

    int countRetiredPlayers(List<Player> players) {
        int numRetiredPlayers = 0;
        for (Player player : players) {
            if (player.isRetired()) {
                numRetiredPlayers++;
            }
        }
        return numRetiredPlayers;
    }
}