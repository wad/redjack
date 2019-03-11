package org.wadhome.redjack;

class Player {
    private String playerName;
    private Gender gender;
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
                table);
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard,
            Table table) {

        return playStrategy.choosePlay(
                hand,
                dealerUpcard,
                bankroll.copy(),
                table);
    }

    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            Table table) {

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
                bankroll.copy(),
                table);
    }

    @Override
    public String toString() {
        return playerName;
    }

    Gender getGender() {
        return gender;
    }

    public void observeCard(Card card) {
        this.playStrategy.getCardCountMethod().observeCard(card);
    }

    public void observeShuffle() {
        this.playStrategy.getCardCountMethod().observeShuffle();
    }
}
