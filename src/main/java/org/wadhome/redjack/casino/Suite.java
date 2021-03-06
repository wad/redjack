package org.wadhome.redjack.casino;

public enum Suite {
    Spades("♠"),
    Hearts("♥"),
    Clubs("♣"),
    Diamonds("♦");

    private String symbol;

    Suite(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
