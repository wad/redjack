package org.wadhome.redjack;

public class Redjack {
    public static void main(String... args) {
        Casino casino = new Casino("Redjack");
        casino.createTable(0, TableRules.getDefaultRules());

        Table table = casino.getTable(7);
        Shoe shoe = table.getShoe();
        shoe.shuffle();
        while (shoe.hasCards()) {
            Card topCard = shoe.drawTopCard();
            System.out.println(topCard.print(false));
        }
    }
}
