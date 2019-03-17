package org.wadhome.redjack;

import java.util.List;

abstract class Execution {
    private Long seedOverride = null;

    abstract Casino execute(Command command);

    protected void setSeedOverride(Long seedOverride) {
        this.seedOverride = seedOverride;
    }

    protected long getSeed() {
        if (seedOverride != null) {
            return seedOverride;
        }
        return Randomness.generateRandomSeed();
    }

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

    MoneyPile getSumOfPlayerBankrolls(List<Player> players) {
        MoneyPile sum = MoneyPile.zero();
        for (Player player : players) {
            sum.addToPile(player.getBankroll());
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
