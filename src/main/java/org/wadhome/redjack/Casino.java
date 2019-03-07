package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

public class Casino {
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;

    Casino(String casinoName) {
        this.casinoName = casinoName;
        Display.showMessage("Welcome to the " + casinoName + " casino.");
    }

    void createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    public String getCasinoName() {
        return casinoName;
    }
}
