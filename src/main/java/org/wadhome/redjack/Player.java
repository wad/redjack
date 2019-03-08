package org.wadhome.redjack;

class Player {
    private String playerName;
    private Gender gender;
    private MoneyPile bankroll;
    private PlayerStrategy playerStrategy;
    private boolean takesMaxInsurance = true;
    private MoneyPile favoriteBet = null;

    Player(
            String playerName,
            Gender gender,
            MoneyPile bankroll,
            PlayerStrategy playerStrategy) {
        this.playerName = playerName;
        this.gender = gender;
        this.bankroll = bankroll;
        this.playerStrategy = playerStrategy;
    }

    String getPlayerName() {
        return playerName;
    }

    void setFavoriteBet(MoneyPile favoriteBet) {
        this.favoriteBet = favoriteBet;
    }

    void setTakesMaxInsurance(
            @SuppressWarnings("SameParameterValue") boolean takesMaxInsurance) {
        this.takesMaxInsurance = takesMaxInsurance;
    }

    MoneyPile getBankroll() {
        return this.bankroll;
    }

    MoneyPile getBet(TableRules tableRules) {
        MoneyPile minPossibleBet = tableRules.getMinBet();
        MoneyPile maxPossibleBet = MoneyPile.getLesserOf(tableRules.getMaxBet(), bankroll);
        if (minPossibleBet.isGreaterThan(bankroll)) {
            return MoneyPile.zero();
        }

        if (favoriteBet.isGreaterThan(maxPossibleBet)) {
            // it will be more than the table minimum bet
            return maxPossibleBet;
        }

        // it will be less than or equal to the max possible bet
        return favoriteBet.copy();
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard,
            TableRules tableRules) {
        switch (playerStrategy) {
            case BasicStrategy:
                return BasicStrategy.choosePlay(
                        hand,
                        dealerUpcard,
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

    Gender getGender() {
        return gender;
    }
}
