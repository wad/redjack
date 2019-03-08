package org.wadhome.redjack;

import java.util.ArrayList;

class Shoe extends CardStack {
    private Casino casino;
    private int numDecks;
    private int numCardsAfterCutCard;

    Shoe(Casino casino,
            int shoeNumber,
            int numDecks) {
        this.casino = casino;
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

    void dumpAllCards() {
        while (hasCards()) {
            drawTopCard();
        }
    }

    void placeCutCard(int numCardsAfterCutCardInShoe) {
        if (numCardsAfterCutCardInShoe > TableRules.NUM_CARDS_PER_DECK * numDecks) {
            throw new RuntimeException("Invalid cut card position: " + numCardsAfterCutCardInShoe);
        }

        numCardsAfterCutCard = numCardsAfterCutCardInShoe;
    }

    boolean hasCutCardBeenDrawn() {
        return cards.size() <= this.numCardsAfterCutCard;
    }

    @Override
    protected void extraHandlingOnCardDraw() {
        if (hasCutCardBeenDrawn()) {
            casino.getDisplay().showMessage("Cut card was drawn. Will shuffle after this hand.");
        }
    }

    void shuffle(Randomness randomness) {
        randomness.shuffle(cards);
    }
}
