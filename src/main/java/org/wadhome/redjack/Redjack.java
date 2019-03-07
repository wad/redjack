package org.wadhome.redjack;

public class Redjack {

    public static void main(String... args) {
        Casino casino = new Casino("Redjack");
        casino.createTable(0, TableRules.getDefaultRules());
    }
}
