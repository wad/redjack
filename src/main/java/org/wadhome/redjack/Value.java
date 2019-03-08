package org.wadhome.redjack;

enum Value {
    Two("2", 2),
    Three("3", 3),
    Four("4", 4),
    Five("5", 5),
    Six("6", 6),
    Seven("7", 7),
    Eight("8", 8),
    Nine("9", 9),
    Ten("T", 10),
    Jack("J", 10),
    Queen("Q", 10),
    King("K", 10),
    Ace("A", 1); // Can also be 11 points.

    static final int OPTIONAL_EXTRA_ACE_POINTS = 10;

    private String symbol;
    private int points;

    Value(String symbol, int points) {
        this.symbol = symbol;
        this.points = points;
    }

    int getPoints() {
        return points;
    }

    boolean isTen() {
        switch (this) {
            case Two:
            case Three:
            case Four:
            case Five:
            case Six:
            case Seven:
            case Eight:
            case Nine:
                return false;
            case Ten:
            case Jack:
            case Queen:
            case King:
                return true;
            case Ace:
                return false;
            default:
                throw new RuntimeException("Bug!");
        }
    }

    @Override
    public String toString() {
        return symbol;
    }

    public String getIndefiniteArticle(boolean shouldCapitalize) {
        switch (this) {
            case Two:
            case Three:
            case Four:
            case Five:
            case Six:
            case Seven:
                return shouldCapitalize ? "A" : "a";
            case Eight:
                return shouldCapitalize ? "An" : "an";
            case Nine:
            case Ten:
            case Jack:
            case Queen:
            case King:
                return shouldCapitalize ? "A" : "a";
            case Ace:
                return shouldCapitalize ? "An" : "an";
            default:
                throw new RuntimeException("Bug!");
        }
    }
}
