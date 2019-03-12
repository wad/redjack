package org.wadhome.redjack;

class Player {
    private String playerName;
    private Gender gender;
    private MoneyPile initialBankroll;
    private MoneyPile bankroll;
    private boolean takesMaxInsurance = true;
    private MoneyPile favoriteBet;
    private PlayStrategy playStrategy;

    Player(
            String playerName,
            Gender gender,
            MoneyPile bankroll,
            PlayStrategy playStrategy,
            MoneyPile favoriteBet) {
        this.playerName = playerName;
        this.gender = gender;
        this.initialBankroll = bankroll.copy();
        this.bankroll = bankroll;
        this.favoriteBet = favoriteBet;
        this.playStrategy = playStrategy;
    }

    String getPlayerName() {
        return playerName;
    }

    void setTakesMaxInsurance(
            @SuppressWarnings("SameParameterValue") boolean takesMaxInsurance) {
        this.takesMaxInsurance = takesMaxInsurance;
    }

    MoneyPile getInitialBankroll() {
        return initialBankroll.copy();
    }

    MoneyPile getBankroll() {
        return this.bankroll;
    }

    MoneyPile getBet(Table table) {
        TableRules tableRules = table.getTableRules();
        MoneyPile minPossibleBet = tableRules.getMinBet();
        MoneyPile maxPossibleBet = MoneyPile.getLesserOf(tableRules.getMaxBet(), bankroll);
        if (minPossibleBet.isGreaterThan(bankroll)) {
            return MoneyPile.zero();
        }

        return playStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet,
                this);
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard) {

        return playStrategy.choosePlay(
                hand,
                dealerUpcard,
                bankroll.copy());
    }

    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard) {

        // this overrides strategy
        if (takesMaxInsurance) {
            if (bankroll.isGreaterThanOrEqualTo(maximumInsuranceBet)) {
                return maximumInsuranceBet.copy();
            } else {
                return bankroll.copy();
            }
        }

        return playStrategy.getInsuranceBet(
                maximumInsuranceBet,
                hand,
                dealerUpcard,
                bankroll.copy());
    }

    @Override
    public String toString() {
        return playerName;
    }

    Gender getGender() {
        return gender;
    }

    public PlayStrategy getPlayStrategy() {
        return playStrategy;
    }

    void observeCard(Card card) {
        this.playStrategy.getCardCountMethod().observeCard(card);
    }

    void observeShuffle() {
        this.playStrategy.getCardCountMethod().observeShuffle();
    }
}
