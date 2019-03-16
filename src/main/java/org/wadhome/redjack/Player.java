package org.wadhome.redjack;

class Player {
    private String playerName;
    private String notes;
    private Gender gender;
    private MoneyPile initialBankroll;
    private MoneyPile retirementTriggerBankroll;
    private boolean isRetired = false;
    private boolean isBankrupt = false;
    private MoneyPile bankroll;
    private boolean takesMaxInsurance = true;
    private MoneyPile favoriteBet;
    private PlayStrategy playStrategy;
    private Casino casino;

    Player(
            String playerName,
            Gender gender,
            Casino casino,
            MoneyPile bankroll,
            PlayStrategy playStrategy,
            MoneyPile favoriteBet) {
        this.playerName = playerName;
        this.gender = gender;
        this.casino = casino;
        this.initialBankroll = bankroll.copy();
        this.bankroll = bankroll;
        this.favoriteBet = favoriteBet;
        this.playStrategy = playStrategy;
    }

    String getPlayerName() {
        return playerName;
    }

    void setNotes(String notes) {
        this.notes = notes;
    }

    String getNotes() {
        return notes;
    }

    void setRetirementTriggerBankroll(MoneyPile retirementTriggerBankroll) {
        this.retirementTriggerBankroll = retirementTriggerBankroll;
    }

    void setTakesMaxInsurance(
            @SuppressWarnings("SameParameterValue") boolean takesMaxInsurance) {
        this.takesMaxInsurance = takesMaxInsurance;
    }

    void say(String message) {
        casino.getDisplay().showMessage(
                getPlayerName() + " says, \"" + message + "\"");
    }

    MoneyPile getInitialBankroll() {
        return initialBankroll.copy();
    }

    MoneyPile getBankroll() {
        return this.bankroll;
    }

    boolean isRetired() {
        if (retirementTriggerBankroll == null) {
            return false;
        }

        if (!isRetired) {
            if (bankroll.isGreaterThanOrEqualTo(retirementTriggerBankroll)) {
                say("I've doubled my initial bankroll, so I'm retiring.");
                isRetired = true;
            }
        }
        return isRetired;
    }

    boolean isBankrupt() {
        return isBankrupt;
    }

    MoneyPile getBet(Table table) {

        if (isRetired() || isBankrupt()) {
            return MoneyPile.zero();
        }

        TableRules tableRules = table.getTableRules();
        BetRequest betRequest = new BetRequest(
                this,
                casino.getRandomness(),
                favoriteBet,
                tableRules.getMinBet(),
                tableRules.getMaxBet(),
                bankroll.copy());
        playStrategy.getBet(betRequest);
        if (betRequest.canPlaceBet()) {
            String comment = betRequest.getBetComment();
            if (comment != null) {
                say(comment);
            }
            return betRequest.getActualBetAmount();
        } else {
            isBankrupt = true;
            return MoneyPile.zero();
        }
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard) {
        return playStrategy.choosePlay(
                this,
                hand,
                dealerUpcard,
                bankroll.copy());
    }

    CardCountStatus getCardCountStatus() {
        return playStrategy.getCardCountMethod().getCardCountStatus();
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

    PlayStrategy getPlayStrategy() {
        return playStrategy;
    }

    void observeCard(Card card) {
        this.playStrategy.getCardCountMethod().observeCard(card);
    }

    void observeShuffle() {
        this.playStrategy.getCardCountMethod().observeShuffle();
    }
}
