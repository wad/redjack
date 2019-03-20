package org.wadhome.redjack.execution;

import org.wadhome.redjack.Randomness;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;

import java.util.List;

public abstract class Execution {
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

    CurrencyAmount getSumOfPlayerBankrolls(List<Player> players) {
        CurrencyAmount sum = CurrencyAmount.zero();
        for (Player player : players) {
            sum.increaseBy(player.getBankroll().getCurrencyAmountCopy());
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
