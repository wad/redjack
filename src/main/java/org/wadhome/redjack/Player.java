package org.wadhome.redjack;

public class Player {
    private String playerName;
    private MoneyPile bankroll;
    private PlayerSmarts playerSmarts;

    public Player(
            String playerName,
            MoneyPile bankroll,
            PlayerSmarts playerSmarts) {
        this.playerName = playerName;
        this.bankroll = bankroll;
        this.playerSmarts = playerSmarts;
    }

    public String getPlayerName() {
        return playerName;
    }

    public MoneyPile getBankroll() {
        return this.bankroll;
    }

    public BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard,
            int numSplitsSoFar,
            TableRules tableRules) {
        switch (playerSmarts) {
            case BasicStrategy:
                return BasicStrategy.compute(
                        hand,
                        dealerUpcard,
                        numSplitsSoFar,
                        bankroll.copy(),
                        tableRules);
            case CardCounter:
                throw new UnsupportedOperationException("We don't handle card counting yet.");
            default:
                throw new RuntimeException("Bug in code!");
        }
    }

    @Override
    public String toString() {
        return playerName;
    }
}
