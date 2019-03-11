package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;
    private MoneyPile houseBankroll = new MoneyPile(100000000000000L);
    private Display display;

    Casino(String casinoName) {
        this(casinoName, null, null);
    }

    Casino(
            String casinoName,
            Long seed, // you can supply Randomness.generateRandomSeed() here
            Display display) {
        this.casinoName = casinoName;
        if (seed == null) {
            seed = Randomness.generateRandomSeed();
        }
        this.display = display == null ? new Display(true) : display;
        randomness = new Randomness(seed);
        showWelcomeInfo();
    }

    private void showWelcomeInfo() {
        display.showMessage("Welcome to the " + casinoName + " casino.");
        Long seed = randomness.getSeed();
        display.showMessage("Randomness seed: " + seed);
    }

    MoneyPile getBankroll() {
        return houseBankroll;
    }

    public Display getDisplay() {
        return display;
    }

    Table createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                this,
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
        return table;
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    Randomness getRandomness() {
        return randomness;
    }
}
