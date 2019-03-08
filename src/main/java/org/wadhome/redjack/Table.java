package org.wadhome.redjack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    Casino casino;
    private int tableNumber;
    private TableRules tableRules;
    private Shoe shoe;
    private DiscardTray discardTray;
    private Map<SeatNumber, Seat> seats;
    private DealerHand dealerHand;

    Table(
            Casino casino,
            int tableNumber,
            TableRules tableRules) {

        this.casino = casino;
        this.tableNumber = tableNumber;
        this.tableRules = tableRules;
        int numDecks = tableRules.getNumDecks();
        this.shoe = new Shoe(tableNumber, numDecks);
        this.discardTray = new DiscardTray(numDecks);
        this.dealerHand = new DealerHand();
        this.seats = new HashMap<>(SeatNumber.values().length);

        for (SeatNumber seatNumber : SeatNumber.values()) {
            seats.put(seatNumber, new Seat(this, seatNumber));
        }
    }

    void showPreparationMessages() {
        show("The dealer is preparing table " + tableNumber + " for blackjack.");
        show(tableRules.toString());
    }

    Shoe getShoe() {
        return shoe;
    }

    void prepareForPlay() {
        int numCardsInDiscardTray = discardTray.cards.size();
        while (discardTray.hasCards()) {
            shoe.addCardToBottom(discardTray.drawTopCard());
        }

        if (numCardsInDiscardTray > 0) {
            show("The dealer removed all " + numCardsInDiscardTray + " cards from the discard tray.");
        }

        shoe.shuffle();
        show("The dealer shuffled all " + tableRules.getNumDecks() + " decks, and loaded them into the shoe.");

        shoe.placeCutCard(tableRules.getNumCardsAfterCutCard());
        show("The dealer placed placed the cut card with " + tableRules.getNumCardsAfterCutCard() + " cards behind it.");

        burn();
    }

    private void burn() {
        int numBurnCards = tableRules.getNumBurnCards();
        for (int i = 0; i < numBurnCards; i++) {
            Card burnCard = shoe.drawTopCard();
            show("The dealer burned a card from the shoe: " + burnCard + ".");
            discardTray.addCardToBottom(burnCard);
        }
    }

    boolean areAnySeatsAvailable() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            if (!seats.get(seatNumber).hasPlayer()) {
                return true;
            }
        }
        return false;
    }

    SeatNumber getAnAvailableHandNumber() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            if (!seats.get(seatNumber).hasPlayer()) {
                return seatNumber;
            }
        }
        throw new RuntimeException("No free seats.");
    }

    boolean isSeatOccupied(SeatNumber seatNumber) {
        return seats.get(seatNumber).hasPlayer();
    }

    void assignPlayerToSeat(
            SeatNumber seatNumber,
            Player player) {
        Seat seat = seats.get(seatNumber);
        if (seat.hasPlayer()) {
            throw new RuntimeException("Seat was in use, cannot assign.");
        }

        seat.setPlayer(player);
    }

    public void removePlayerFromSeat(SeatNumber seatNumber) {
        Seat seat = seats.get(seatNumber);
        if (!seat.hasPlayer()) {
            throw new RuntimeException("Nobody is at seat " + seatNumber);
        }

        seat.removePlayer();
    }

    void playerPayCasino(
            Player player,
            MoneyPile amount) {
        moveMoney(
                player.getBankroll(),
                casino.getBankroll(),
                amount);
    }

    void casinoPayPlayer(
            Player player,
            MoneyPile amount) {
        moveMoney(
                casino.getBankroll(),
                player.getBankroll(),
                amount);
    }

    private void moveMoney(
            MoneyPile fromPile,
            MoneyPile toPile,
            MoneyPile amountToMove) {
        fromPile.subtractFromPile(amountToMove);
        toPile.addToPile(amountToMove);
    }

    boolean playRound() {
        takeBetsFromPlayers();

        if (!anySeatsHaveBets()) {
            show("No seats have hands with bets.");
            return false;
        }

        dealCardsToPlayers();
        dealFirstDealerCard();
        dealCardsToPlayers();
        dealSecondDealerCard();
        handleInsurance();
        boolean didDealerBlackjack = handleDealerBlackjack();
        if (!didDealerBlackjack) {
            handlePlayerBlackjacks();
            playersPlay();
            dealerPlays();
            resolveBets();
        }
        cleanupTable();
        return shoe.hasCutCardBeenDrawn();
    }

    private void takeBetsFromPlayers() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                Player player = seat.getPlayer();
                MoneyPile betAmount = player.getBet(tableRules);
                playerPayCasino(player, betAmount);
                PlayerHand hand = seat.addHand(betAmount);
                show(hand, "placed a bet of " + betAmount + ".");
            }
        }
    }

    private boolean anySeatsHaveBets() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                if (hand.getBetAmount().hasMoney()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void dealCardsToPlayers() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                if (hand.getBetAmount().hasMoney()) {
                    Card card = shoe.drawTopCard();
                    show(hand, "got a " + card + ".");
                    hand.addCard(card);
                }
            }
        }
    }

    private void dealFirstDealerCard() {
        Card card = shoe.drawTopCard();
        show("Dealer got a face-down card.");
        dealerHand.addCard(card);
    }

    private void dealSecondDealerCard() {
        Card holeCard = shoe.drawTopCard();
        show("Dealer got a face-down hole card.");
        dealerHand.addCard(holeCard);
        show("Dealer reveals her first card, which was " + dealerHand.getFirstCard() + ".");
    }

    private void handleInsurance() {
        if (dealerHand.getFirstCard().getValue() == Value.Ace) {
            show("Dealer asks the players if they want insurance.");

            Map<SeatNumber, MoneyPile> insuranceBets = new HashMap<>();

            for (SeatNumber seatNumber : SeatNumber.values()) {
                Seat seat = seats.get(seatNumber);
                MoneyPile maxInsuranceBet = seat.computeBetSum().computeHalf();
                if (maxInsuranceBet.hasMoney()) {
                    MoneyPile desiredInsuranceBet = seat.getPlayer().getInsuranceBet(maxInsuranceBet);
                    if (desiredInsuranceBet.hasMoney()) {
                        playerPayCasino(seat.getPlayer(), desiredInsuranceBet);
                        show(seat, "put down " + desiredInsuranceBet + " as insurance against the dealer having blackjack.");
                        insuranceBets.put(seatNumber, desiredInsuranceBet);
                    } else {
                        show(seat, "declines the insurance bet.");
                    }
                }
            }

            if (insuranceBets.isEmpty()) {
                show("Nobody made an insurance bet.");
            } else {
                if (dealerHand.secondCard.getValue().isTen()) {
                    show("Dealer turns over the hole card, and it's " + dealerHand.secondCard + ". Blackjack!"
                            + " All hands lose, insurance bets all pay double.");

                    // for each player, pay insurance winnings, if any.
                    for (SeatNumber seatNumber : SeatNumber.values()) {
                        MoneyPile insuranceBet = insuranceBets.get(seatNumber);
                        if (insuranceBet != null && insuranceBet.hasMoney()) {
                            Seat seat = seats.get(seatNumber);
                            Player player = seat.getPlayer();

                            casinoPayPlayer(player, insuranceBet);
                            MoneyPile winnings = insuranceBet.computeDouble();
                            casinoPayPlayer(player, winnings);
                            show("Seat " + seatNumber
                                    + " : " + player.getPlayerName()
                                    + " wins " + winnings + " for blackjack insurance.");
                        }
                    }

                    for (SeatNumber seatNumber : SeatNumber.values()) {
                        Seat seat = seats.get(seatNumber);
                        if (seat.hasPlayer()) {
                            for (PlayerHand hand : seat.getHands()) {
                                show(hand, "loses " + hand.getBetAmount() + " to the dealer's blackjack.");
                                discardTray.addCards(hand.removeCards());
                            }
                        }
                    }
                } else {
                    show("Dealer turns over the hole card, and it's " + dealerHand.secondCard + ". Not a blackjack."
                            + " insurance bets all lose.");
                }
            }
        }
    }

    private boolean handleDealerBlackjack() {
        if (!dealerHand.isBlackjack()) {
            show("Dealer checks the hole card. No blackjack for the dealer.");
            return false;
        }

        show("Dealer checks the hole card... and got a blackjack: " + dealerHand.showCardsWithTotal());
        for (int i = SeatNumber.values().length - 1; i >= 0; --i) {
            SeatNumber seatNumber = SeatNumber.values()[i];
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                MoneyPile betAmount = hand.removeBet();
                if (betAmount.hasMoney()) {
                    Player player = seat.getPlayer();
                    if (hand.isBlackjack()) {
                        casinoPayPlayer(player, betAmount); // they get their original bet back.
                        show(hand, "also had a blackjack, so it's a push.");
                    } else {
                        show(hand, "loses " + betAmount + " to the dealers blackjack.");
                    }
                    discardTray.addCards(hand.removeCards());
                }
            }
        }
        return true;
    }

    private void handlePlayerBlackjacks() {
        for (int i = SeatNumber.values().length - 1; i >= 0; --i) {
            SeatNumber seatNumber = SeatNumber.values()[i];
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                if (hand.getBetAmount().hasMoney()) {
                    if (hand.isBlackjack()) {
                        Player player = hand.getSeat().getPlayer();
                        MoneyPile betAmount = hand.removeBet();
                        casinoPayPlayer(player, betAmount); // original bet returned
                        MoneyPile winnings = betAmount.computeOneAndHalf();
                        casinoPayPlayer(player, winnings);
                        show(hand, "got a blackjack, and won " + winnings + ".");
                        discardTray.addCards(hand.removeCards());
                    }
                }
            }
        }
    }

    private void playersPlay() {
        Card dealerUpcard = dealerHand.getFirstCard();
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            List<PlayerHand> hands = seat.getHands();

            // Note: the number of hands can increase while inside the loop, due to splits, so we can't for-each over it.
            for (int handIndex = 0; handIndex < hands.size(); handIndex++) {
                PlayerHand hand = hands.get(handIndex);
                if (hand.getBetAmount().hasMoney()) {
                    Player player = seat.getPlayer();
                    boolean shallContinue = true;
                    while (shallContinue) {
                        String initialHand = hand.showCardsWithTotal();
                        BlackjackPlay playerAction = player.getPlay(hand, dealerUpcard, tableRules);
                        switch (playerAction) {
                            case Stand:
                                show(hand, "decides to stand.");
                                shallContinue = false;
                                break;
                            case Hit:
                                shallContinue = handlePlayerHit(initialHand, hand);
                                break;
                            case DoubleDown:
                                handlePlayerDoubleDown(initialHand, hand);
                                shallContinue = false;
                                break;
                            case Split:
                                shallContinue = handlePlayerSplit(hand);
                                break;
                            case Surrender:
                                handlePlayerSurrender(hand);
                                shallContinue = false;
                                break;
                            default:
                                throw new RuntimeException("Bug in code!");
                        }
                    }
                }
            }
        }
    }

    private boolean handlePlayerHit(
            String initialHand,
            PlayerHand hand) {
        Card hitCard = shoe.drawTopCard();
        hand.addCard(hitCard);
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();

        if (hand.isBust()) {
            MoneyPile betAmount = hand.removeBet();
            show("Seat " + seat.getSeatNumeral()
                    + " : " + player.getPlayerName()
                    + " decides to hit with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + hitCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". That's a bust. Lost bet of " + betAmount + ".");
            return false;
        }

        if (hand.isCharlie()) {
            handlePlayerCharlie(initialHand, hitCard, hand);
            return false;
        }

        if (hand.isTwentyOne()) {
            show("Seat " + seat.getSeatNumeral()
                    + " : " + player.getPlayerName()
                    + " decides to hit with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + hitCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". " + player.getHisHer(true) + " must now stand.");
            return false;
        }

        // The hit didn't bust them, they can have another play if they want.
        show("Seat " + seat.getSeatNumeral()
                + " : " + player.getPlayerName()
                + " decides to hit with " + player.getHisHer(false)
                + " hand of " + initialHand
                + ", and got a " + hitCard
                + ". Hand is now " + hand.showCardsWithTotal());
        return true;
    }

    private void handlePlayerDoubleDown(
            String initialHand,
            PlayerHand hand) {
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();

        MoneyPile originalBetAmount = hand.getBetAmount();
        playerPayCasino(player, originalBetAmount);
        hand.setBetAmount(originalBetAmount.computeDouble());

        Card doubleDownCard = shoe.drawTopCard();
        hand.addCard(doubleDownCard);

        if (hand.isBust()) {
            MoneyPile doubledBet = hand.removeBet();
            show("Seat " + seat.getSeatNumeral()
                    + " : " + player.getPlayerName()
                    + " decides to double down with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + doubleDownCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". That's a bust. Lost doubled bet of " + doubledBet + ".");
            return;
        }

        show("Seat " + seat.getSeatNumeral()
                + " : " + player.getPlayerName()
                + " decides to double down with " + player.getHisHer(false)
                + " hand of " + initialHand
                + ", and got a " + doubleDownCard
                + ". Hand is now " + hand.showCardsWithTotal()
                + ". Must now stand. Bet is now " + originalBetAmount.computeDouble() + ".");
    }

    private void handlePlayerCharlie(
            String initialHand,
            Card hitCard,
            PlayerHand hand) {
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();
        MoneyPile betAmount = hand.removeBet();
        casinoPayPlayer(player, betAmount); // original bet returned
        MoneyPile winnings = betAmount.computeDouble();
        casinoPayPlayer(player, winnings);
        show("Seat " + seat.getSeatNumeral()
                + " : " + player.getPlayerName()
                + " decides to hit with " + player.getHisHer(false)
                + " hand of " + initialHand
                + ", and got a " + hitCard
                + ". Hand is now " + hand.showCardsWithTotal()
                + ". That's a seven-card charlie! Win was double, "
                + player.getHeShe(false) + " got " + winnings + ".");
        discardTray.addCards(hand.removeCards());
    }

    private boolean handlePlayerSplit(PlayerHand hand) {
        Seat seat = hand.getSeat();
        int numSplitsSoFar = seat.getNumSplitsSoFar();
        if (numSplitsSoFar == 0) {
            show(hand, "decides to split.");
        } else {
            show(hand, "decides to split again.");
        }

        // todo: split

        // todo: handle aces differently

        //todo: check for blackjacks

        show(hand, "accidentally surrenders instead of splitting.");
        handlePlayerSurrender(hand);

        return false;
    }

    private void handlePlayerSurrender(PlayerHand hand) {
        Player player = hand.getSeat().getPlayer();
        MoneyPile halfBetAmount = hand.removeBet().computeHalf();
        casinoPayPlayer(player, halfBetAmount);
        show(hand, "surrendered, and loses half of "
                + player.getHisHer(false) + " bet (" + halfBetAmount + ").");
    }

    private void dealerPlays() {
        show("Dealer turns over the hole card, and it's a " + dealerHand.getSecondCard()
                + ". Dealer is now showing " + dealerHand.showCardsWithTotal() + ".");
        while (dealerHand.shouldHit(tableRules)) {
            Card dealerHitCard = shoe.drawTopCard();
            dealerHand.addCard(dealerHitCard);
            if (dealerHand.isBust()) {
                show("Dealer hits, and gets a " + dealerHitCard
                        + ". Now showing " + dealerHand.showCardsWithTotal()
                        + ". That's a bust, remaining players all win.");
            } else {
                show("Dealer hits, and gets a " + dealerHitCard
                        + ". Now showing " + dealerHand.showCardsWithTotal()
                        + ". Dealer stands.");
            }
        }
    }

    private void resolveBets() {
        show("Play is complete for this hand. Resolving remaining bets.");
        for (int i = SeatNumber.values().length - 1; i >= 0; --i) {
            SeatNumber seatNumber = SeatNumber.values()[i];
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                MoneyPile betAmount = hand.removeBet();
                if (betAmount.hasMoney()) {
                    Player player = seat.getPlayer();
                    switch (hand.compareWith(dealerHand)) {
                        case ThisLoses:
                            show(hand, "loses " + betAmount + ".");
                            break;
                        case same:
                            casinoPayPlayer(player, betAmount); // they get their original bet back.
                            show(hand, "pushes.");
                            break;
                        case ThisWins:
                            casinoPayPlayer(player, betAmount); // they get their original bet back.
                            casinoPayPlayer(player, betAmount); // winnings
                            show(hand, "wins " + betAmount + ".");
                            break;
                        default:
                            throw new RuntimeException("Bug!");
                    }
                }
            }
        }
    }

    private void cleanupTable() {
        discardTray.addCards(dealerHand.removeCards());
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                Player player = seat.getPlayer();
                for (PlayerHand hand : seat.getHands()) {
                    if (hand.getBetAmount().hasMoney()) {
                        throw new RuntimeException("There is still a bet at seat number " + seat.getSeatNumber());
                    }
                    discardTray.addCards(hand.removeCards());
                }
                seat.destroyHands();
                show(seat, "has a bankroll of " + player.getBankroll() + ".");
            }
        }
    }

    private void show(String message) {
        Display.showMessage(message);
    }

    private void show(
            Seat seat,
            String message) {
        show("Seat " + seat.getSeatNumber() + " : " + seat.getPlayer().getPlayerName() + " " + message);
    }

    private void show(
            PlayerHand hand,
            String message) {
        Seat seat = hand.getSeat();
        String playerName = seat.getPlayer().getPlayerName();
        String msg;
        if (hand.getNumCards() == 0) {
            msg = "Seat "
                    + seat.getSeatNumeral()
                    + " : "
                    + playerName
                    + " "
                    + message;
        } else {
            msg = "Seat "
                    + seat.getSeatNumeral()
                    + " with hand "
                    + hand.showCardsWithTotal()
                    + " : "
                    + playerName
                    + " "
                    + message;
        }
        Display.showMessage(msg);
    }
}
