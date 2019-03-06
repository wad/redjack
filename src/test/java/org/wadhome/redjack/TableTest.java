package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest extends TestHelper {

    @Test
    public void testSimplePlay() {
        TableRules tableRules = TableRules.getDefaultRules();
        Table table = new Table(0, tableRules);
        Shoe shoe = table.getShoe();
        shoe.dumpAllCards();

        Player abe = new Player(
                "Abe",
                PlayerGender.male,
                new MoneyPile(10000L),
                PlayerSmarts.BasicStrategy);
        int handNumber = 0;
        table.assignPlayerToHand(handNumber, abe);

        table.placeBet(handNumber, new MoneyPile(1000L));
        shoe.addCardToBottom(cT(), cT(), cT(), cT());
        table.playRound();

        assertEquals("$100.00", abe.getBankroll().toString());
    }
}
