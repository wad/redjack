package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.Set;

public class DiscardTray extends CardStack
{
    public DiscardTray(int numDecks)
    {
        cards = new ArrayList<>(TableRules.NUM_CARDS_PER_DECK * numDecks);
    }

    @Override
    protected void cardDrawCheck()
    {
    }

    public void addCards(Set<Card> cardsToAdd)
    {
        cards.addAll(cardsToAdd);
    }
}
