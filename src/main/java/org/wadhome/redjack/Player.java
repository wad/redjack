package org.wadhome.redjack;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategy;

public class Player {
    private String playerName;
    private String notes;
    private Gender gender;
    private CurrencyAmount initialBankroll;
    private CurrencyAmount retirementTriggerBankroll;
    private boolean isRetired = false;
    private boolean isBankrupt = false;
    private MoneyPile bankroll;
    private boolean takesMaxInsurance = false;
    private CurrencyAmount favoriteBet;
    private PlayStrategy playStrategy;
    private Casino casino;

    public Player(
            String playerName,
            Gender gender,
            Casino casino,
            MoneyPile bankroll,
            PlayStrategy playStrategy,
            CurrencyAmount favoriteBet) {
        this.playerName = playerName;
        this.gender = gender;
        this.casino = casino;
        this.initialBankroll = bankroll.getCurrencyAmountCopy();
        this.bankroll = bankroll;
        this.favoriteBet = favoriteBet.copy();
        this.playStrategy = playStrategy;
    }

    String getPlayerName() {
        return playerName;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    String getNotes() {
        return notes;
    }

    public void setRetirementTriggerBankroll(CurrencyAmount retirementTriggerBankroll) {
        this.retirementTriggerBankroll = retirementTriggerBankroll.copy();
    }

    void setTakesMaxInsurance(
            @SuppressWarnings("SameParameterValue") boolean takesMaxInsurance) {
        this.takesMaxInsurance = takesMaxInsurance;
    }

    public void say(String message) {
        casino.getOutput().showMessage(
                getPlayerName() + " says, \"" + message + "\"");
    }

    public CurrencyAmount getInitialBankroll() {
        return initialBankroll.copy();
    }

    public MoneyPile getBankroll() {
        return this.bankroll;
    }

    public boolean isRetired() {
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

    public boolean isBankrupt() {
        return isBankrupt;
    }

    CurrencyAmount getBet(Table table) {

        if (isRetired() || isBankrupt()) {
            return CurrencyAmount.zero();
        }

        TableRules tableRules = table.getTableRules();
        BetRequest betRequest = new BetRequest(
                this,
                casino.getRandomness(),
                favoriteBet,
                tableRules.getMinBet(),
                tableRules.getMaxBet());
        playStrategy.getBet(betRequest);
        if (betRequest.canPlaceBet()) {
            String comment = betRequest.getBetComment();
            if (comment != null) {
                say(comment);
            }
            return betRequest.getActualBetAmount();
        } else {
            isBankrupt = true;
            return CurrencyAmount.zero();
        }
    }

    BlackjackPlay getPlay(
            PlayerHand hand,
            Card dealerUpcard) {
        return playStrategy.choosePlay(
                this,
                hand,
                dealerUpcard);
    }

    CardCountStatus getCardCountStatus() {
        return playStrategy.getCardCountMethod().getCardCountStatus();
    }

    CurrencyAmount getInsuranceBet(
            CurrencyAmount maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard) {

        CurrencyAmount availableBankrollAmount = bankroll.getCurrencyAmountCopy();

        // this overrides strategy
        if (takesMaxInsurance) {
            if (availableBankrollAmount.isGreaterThanOrEqualTo(maximumInsuranceBet)) {
                return maximumInsuranceBet.copy();
            } else {
                return availableBankrollAmount;
            }
        }

        CurrencyAmount desiredInsurance = playStrategy.getInsuranceBet(
                maximumInsuranceBet,
                hand,
                dealerUpcard,
                availableBankrollAmount);

        if (desiredInsurance.isGreaterThan(availableBankrollAmount)) {
            return availableBankrollAmount;
        }
        return desiredInsurance;
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
