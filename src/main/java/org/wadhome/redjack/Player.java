package org.wadhome.redjack;

public class Player {
    private String playerName;
    private PlayerGender playerGender;
    private MoneyPile bankroll;
    private PlayerSmarts playerSmarts;
    private boolean takesMaxInsurance = false;

    Player(
            String playerName,
            PlayerGender playerGender,
            MoneyPile bankroll,
            PlayerSmarts playerSmarts) {
        this.playerName = playerName;
        this.playerGender = playerGender;
        this.bankroll = bankroll;
        this.playerSmarts = playerSmarts;
    }

    String getPlayerName() {
        return playerName;
    }

    void setTakesMaxInsurance(
            @SuppressWarnings("SameParameterValue") boolean takesMaxInsurance) {
        this.takesMaxInsurance = takesMaxInsurance;
    }

    String getHisHer(boolean shouldCapitalize) {
        switch (playerGender) {
            case male:
                return shouldCapitalize ? "His" : "his";
            case female:
                return shouldCapitalize ? "Her" : "her";
            default:
                throw new RuntimeException("Bug");
        }
    }

    String getHeShe(
            @SuppressWarnings("SameParameterValue") boolean shouldCapitalize) {
        switch (playerGender) {
            case male:
                return shouldCapitalize ? "He" : "he";
            case female:
                return shouldCapitalize ? "She" : "she";
            default:
                throw new RuntimeException("Bug");
        }
    }

    MoneyPile getBankroll() {
        return this.bankroll;
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard,
            int numSplitsSoFar,
            TableRules tableRules) {
        switch (playerSmarts) {
            case BasicStrategy:
                return BasicStrategy.choosePlay(
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

    MoneyPile getInsuranceBet(MoneyPile maximumInsuranceBet) {
        if (takesMaxInsurance) {
            if (bankroll.isGreaterThanOrEqualTo(maximumInsuranceBet)) {
                return maximumInsuranceBet.copy();
            } else {
                return bankroll.copy();
            }
        }
        return MoneyPile.zero();
    }

    @Override
    public String toString() {
        return playerName;
    }
}
