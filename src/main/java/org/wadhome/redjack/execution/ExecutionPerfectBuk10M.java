package org.wadhome.redjack.execution;

import org.wadhome.redjack.Output;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionPerfectBuk10M extends Execution {

    public static void main(String... args) {
        new ExecutionPerfectBuk10M().execute();
    }

    @Override
    Command getCommand() {
        return Command.playHiLoPerfectBuk10M;
    }

    @Override
    Casino execute() {
        int numRoundsToPlay = 10000000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(25L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(100000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", playing until bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(25, 500);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Buky Anne");
            add("Buky Beth");
            add("Buky Callie");
            add("Buky Dora");
            add("Buky Edna");
            add("Buky Fran");
            add("Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                    playerFavoriteBet);
            player.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(BukofskyBankrollLevel.Level10k);
            return player;
        }).collect(toList());

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        System.out.println();
        System.out.println("Bankrupt players: " + countBankruptPlayers(players) + ".");

        return casino;
    }
}
