package org.wadhome.redjack;

import org.wadhome.redjack.rules.TableRules;

import java.util.HashMap;
import java.util.Map;

public class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private MoneyPile houseBankroll = new MoneyPile(100000000000000L);
    private Output output;

    // used for testing
    Casino() {
        this(
                "test",
                Randomness.generateRandomSeed(),
                false,
                false);
    }

    public Casino(
            String casinoName,
            Long seed,
            boolean isDisplaying,
            boolean isLogging) {
        randomness = new Randomness(seed);
        this.output = new Output(isDisplaying, isLogging);
        output.showMessage("Running " + casinoName + " with seed " + randomness.getSeed() + ".");
    }

    MoneyPile getBankroll() {
        return houseBankroll;
    }

    public Output getOutput() {
        return output;
    }

    public Table createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                this,
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
        table.shuffleAndStuff();
        return table;
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    public Randomness getRandomness() {
        return randomness;
    }

    public void closeCasino() {
        if (output != null) {
            output.closeLogs();
        }
    }
}
