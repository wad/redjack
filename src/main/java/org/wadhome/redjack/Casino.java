package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

public class Casino {
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;

    public Casino(String casinoName) {
        this.casinoName = casinoName;
        Display.showMessage("Welcome to the " + casinoName + " casino.");
    }

    public void createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
    }

    public Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    public String getCasinoName() {
        return casinoName;
    }
}
