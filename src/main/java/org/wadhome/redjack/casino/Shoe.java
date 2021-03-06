package org.wadhome.redjack.casino;

import org.wadhome.redjack.Randomness;
import org.wadhome.redjack.rules.Blackjack;

import java.util.ArrayList;

public class Shoe extends CardStack {
    private Table table;
    private int numDecks;
    private int numCardsAfterCutCard;
    private boolean hasCutCardDrawnBeenAnnounced;

    Shoe(int numDecks, Table table) {
        setStackNumber(table.getTableNumber());
        this.numDecks = numDecks;
        this.table = table;
        this.hasCutCardDrawnBeenAnnounced = false;

        cards = new ArrayList<>(Blackjack.NUM_CARDS_PER_DECK * numDecks);
        for (int deckNumber = 0; deckNumber < numDecks; deckNumber++) {
            Deck deck = new Deck(deckNumber);
            while (deck.hasCards()) {
                cards.add(deck.drawTopCard());
            }
        }
    }

    @Override
    Card drawTopCard() {
        Card topCard = super.drawTopCard();
        table.showCard(topCard);
        return topCard;
    }

    void dumpAllCards() {
        while (hasCards()) {
            drawTopCard();
        }
    }

    void placeCutCard(int numCardsAfterCutCardInShoe) {
        if (numCardsAfterCutCardInShoe > Blackjack.NUM_CARDS_PER_DECK * numDecks) {
            throw new RuntimeException("Invalid cut card position: " + numCardsAfterCutCardInShoe);
        }

        numCardsAfterCutCard = numCardsAfterCutCardInShoe;
        hasCutCardDrawnBeenAnnounced = false;
    }

    boolean hasCutCardBeenDrawn() {
        return cards.size() <= this.numCardsAfterCutCard;
    }

    @Override
    protected void extraHandlingOnCardDraw() {
        if (hasCutCardBeenDrawn()) {
            if (!hasCutCardDrawnBeenAnnounced) {
                hasCutCardDrawnBeenAnnounced = true;
                table.getCasino().getOutput().showMessage("Cut card was drawn. Will shuffle after this hand.");
            }
        }
    }

    void shuffle(Randomness randomness) {
        randomness.shuffle(cards);
    }
}
