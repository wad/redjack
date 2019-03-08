package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;
    private MoneyPile houseBankroll = new MoneyPile(100000000);
    private Display display;

    Casino(String casinoName) {
        this(casinoName, null, null);
    }

    Casino(
            String casinoName,
            Display display) {
        this(casinoName, null, display);
    }

    Casino(
            String casinoName,
            Long seed,
            Display display) {
        this.casinoName = casinoName;
        this.display = display == null ? new Display() : display;

        if (seed == null) {
            seed = Randomness.generateRandomSeed();
        }
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

    void createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                this,
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    Randomness getRandomness() {
        return randomness;
    }
}
