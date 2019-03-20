package org.wadhome.redjack.casino;

import org.junit.Test;
import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.rules.Blackjack;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class ShoeTest extends TestHelper {
    @Test
    public void testAddAndDraw() {
        int numDecks = 2;
        int tableNumber = 0;
        Casino casino = new Casino();
        TableRules tableRules = new TableRulesForTest();
        Table table = new Table(casino, tableNumber, tableRules);
        Shoe shoe = new Shoe(numDecks, table);
        assertEquals(Blackjack.NUM_CARDS_PER_DECK * 2, shoe.cards.size());
        shoe.dumpAllCards();

        assertEquals(0, shoe.cards.size());
        shoe.addCardToBottom(c5());
        shoe.addCardToBottom(c6(), c7());
        shoe.addCardToBottom(c8());
        assertEquals(4, shoe.cards.size());
        assertEquals(c5().getValue(), shoe.drawTopCard().getValue());
        assertEquals(c6().getValue(), shoe.drawTopCard().getValue());
        assertEquals(c7().getValue(), shoe.drawTopCard().getValue());
        assertEquals(c8().getValue(), shoe.drawTopCard().getValue());
        assertEquals(0, shoe.cards.size());
    }
}
