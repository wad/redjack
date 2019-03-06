package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.Collections;

public class Shoe extends CardStack {
    private int numDecks;
    private int numCardsAfterCutCard;

    public Shoe(
            int shoeNumber,
            int numDecks) {
        setStackNumber(shoeNumber);
        this.numDecks = numDecks;

        cards = new ArrayList<>(TableRules.NUM_CARDS_PER_DECK * numDecks);
        for (int deckNumber = 0; deckNumber < numDecks; deckNumber++) {
            Deck deck = new Deck(deckNumber);
            while (deck.hasCards()) {
                cards.add(deck.drawTopCard());
            }
        }
    }

    public int getNumDecks() {
        return numDecks;
    }

    public void placeCutCard(int numCardsAfterCutCardInShoe) {
        if (numCardsAfterCutCardInShoe > TableRules.NUM_CARDS_PER_DECK * numDecks) {
            throw new RuntimeException("Invalid cut card position: " + numCardsAfterCutCardInShoe);
        }

        numCardsAfterCutCard = numCardsAfterCutCardInShoe;
    }

    public boolean hasCutCardBeenDrawn() {
        return cards.size() <= this.numCardsAfterCutCard;
    }

    @Override
    protected void cardDrawCheck() {
        if (hasCutCardBeenDrawn()) {
            Display.showMessage("Cut card was drawn.");
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
