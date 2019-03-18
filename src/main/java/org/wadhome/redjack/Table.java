package org.wadhome.redjack;

import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.CurrencyComputation;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private Casino casino;
    private int tableNumber;
    private TableRules tableRules;
    private Shoe shoe;
    private DiscardTray discardTray;
    private Map<SeatNumber, Seat> seats;
    private DealerHand dealerHand;
    private Gender dealerGender;

    Table(
            Casino casino,
            int tableNumber,
            TableRules tableRules) {

        this.casino = casino;
        dealerGender = Gender.getRandomGender(casino.getRandomness());
        this.tableNumber = tableNumber;
        this.tableRules = tableRules;
        int numDecks = tableRules.getNumDecks();
        this.shoe = new Shoe(numDecks, this);
        this.discardTray = new DiscardTray(numDecks);
        this.dealerHand = new DealerHand();
        this.seats = new HashMap<>(SeatNumber.values().length);

        for (SeatNumber seatNumber : SeatNumber.values()) {
            seats.put(seatNumber, new Seat(seatNumber));
        }
    }

    void showPreparationMessages() {
        show("\nThe dealer prepares table " + tableNumber + " for blackjack.");
        show(tableRules.getRulesDisplay(tableNumber));
    }

    public Casino getCasino() {
        return casino;
    }

    public Shoe getShoe() {
        return shoe;
    }

    int getTableNumber() {
        return tableNumber;
    }

    void shuffleAndStuff() {
        int numCardsInDiscardTray = discardTray.cards.size();
        while (discardTray.hasCards()) {
            shoe.addCardToBottom(discardTray.drawTopCard());
        }

        if (numCardsInDiscardTray > 0) {
            show("The dealer removes all " + numCardsInDiscardTray + " cards from the discard tray.");
        }

        shoe.shuffle(casino.getRandomness());
        show("The dealer shuffles all " + tableRules.getNumDecks() + " decks, and loads them into the shoe.");

        shoe.placeCutCard(tableRules.getNumCardsAfterCutCard());
        show("The dealer places the cut card with " + tableRules.getNumCardsAfterCutCard() + " cards behind it.");

        showPlayersShuffle();

        burn();
    }

    public TableRules getTableRules() {
        return tableRules;
    }

    public DiscardTray getDiscardTray() {
        return this.discardTray;
    }

    private void burn() {
        int numBurnCards = tableRules.getNumBurnCards();
        for (int i = 0; i < numBurnCards; i++) {
            Card burnCard = shoe.drawTopCard();
            show("The dealer burns " + burnCard.toString(true, false)
                    + " from the shoe.");
            discardTray.addCardToBottom(burnCard);
        }
    }

    private void showPlayersShuffle() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                seat.getPlayer().observeShuffle();
            }
        }
    }

    void showCard(Card card) {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                seat.getPlayer().observeCard(card);
            }
        }
    }

    public boolean areAnySeatsAvailable() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            if (!seats.get(seatNumber).hasPlayer()) {
                return true;
            }
        }
        return false;
    }

    public SeatNumber getAnAvailableSeatNumber() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            if (!seats.get(seatNumber).hasPlayer()) {
                return seatNumber;
            }
        }
        throw new RuntimeException("No free seats. Should have checked for an available seat first.");
    }

    public void assignPlayerToSeat(
            SeatNumber seatNumber,
            Player player) {
        Seat seat = seats.get(seatNumber);
        if (seat.hasPlayer()) {
            throw new RuntimeException("Seat was in use, cannot assign. Should have checked first.");
        }

        seat.setPlayer(player);
        show(seat, "sat down in seat " + seatNumber + ".");
        String notes = player.getNotes();
        if (notes != null) {
            show(notes);
        }
    }

    void removePlayerFromSeat(SeatNumber seatNumber) {
        Seat seat = seats.get(seatNumber);
        if (!seat.hasPlayer()) {
            throw new RuntimeException("Nobody is at seat " + seatNumber);
        }

        show(seat, "leaves seat " + seatNumber + ".");
        seat.removePlayer();
    }

    private void playerPayCasino(
            Player player,
            CurrencyAmount amount) {
        MoneyPile playerBankroll = player.getBankroll();
        if (!playerBankroll.canExtract(amount)) {
            throw new RuntimeException("Bug! Player ran out of money. Check first!");
        }

        playerBankroll.moveMoneyToTargetPile(casino.getBankroll(), amount);
    }

    private void casinoPayPlayer(
            Player player,
            CurrencyAmount amount) {
        MoneyPile casinoBankroll = casino.getBankroll();
        if (!casinoBankroll.canExtract(amount)) {
            throw new RuntimeException("Casino ran out of money!");
        }

        casinoBankroll.moveMoneyToTargetPile(player.getBankroll(), amount);
    }

    public void playRoundsUntilEndOfShoe() {
        showAndDisplay("\nRunning hands until the end of this shoe is reached.");
        int roundNumber = 0;
        boolean continueRounds = true;
        while (continueRounds) {
            roundNumber++;

            show("");
            show("===> Round " + roundNumber + " begins <===");

            RoundResult roundResult = playRound(roundNumber);
            if (roundResult.isCutCardDrawn()) {
                shuffleAndStuff();
                continueRounds = false;
            }

            if (roundResult.haveAllPlayersQuit()) {
                showAndDisplay("\nAll players have quit on round number " + roundNumber + ".");
                continueRounds = false;
            }
        }
        showAndDisplayPlayerResults();
    }

    public void playRounds(int numRoundsToPlay) {
        Output output = casino.getOutput();
        showAndDisplay("\nRunning " + numRoundsToPlay + " rounds of play.");
        boolean continueRounds = true;
        int shoeCount = 1;
        int roundNumber;
        for (roundNumber = 0; continueRounds && (roundNumber < numRoundsToPlay); roundNumber++) {
            if (!output.isDisplaying()) {
                if (roundNumber > 0 && roundNumber % 1000 == 0) {
                    System.out.print(".");
                }
            }

            show("");
            show("===> Round " + (roundNumber + 1) + " begins <===");

            RoundResult roundResult = playRound(roundNumber);
            if (roundResult.isCutCardDrawn()) {
                shuffleAndStuff();
                shoeCount++;
            }

            if (roundResult.haveAllPlayersQuit()) {
                showAndDisplay("\nAll players have quit on round number " + roundNumber + ".");
                continueRounds = false;
            }
        }

        if (!output.isDisplaying()) {
            System.out.println();
        }
        showAndDisplay("Completed " + roundNumber + " rounds, in " + shoeCount + " shoes.");
        showAndDisplayPlayerResults();
    }

    // used for testing
    void playRound() {
        playRound(0);
    }

    private RoundResult playRound(int roundNumber) {
        logRound(roundNumber);

        if (areAllPlayersBankrupt()) {
            return RoundResult.allPlayersQuit();
        }

        takeBetsFromPlayers();
        if (!anySeatsHaveBets()) {
            show("No seats have hands with bets.");
            return RoundResult.allPlayersQuit();
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
        if (shoe.hasCutCardBeenDrawn()) {
            return RoundResult.cutCardDrawn();
        }
        return RoundResult.normal();
    }

    private void logRound(int roundNumber) {
        Output output = getCasino().getOutput();
        if (output.isLogging()) {
            List<Player> players = new ArrayList<>(SeatNumber.values().length);
            for (SeatNumber seatNumber : SeatNumber.values()) {
                Seat seat = seats.get(seatNumber);
                if (seat.hasPlayer()) {
                    players.add(seat.getPlayer());
                }
            }
            output.logRound(roundNumber, players);
        }
    }

    private boolean areAllPlayersBankrupt() {
        CurrencyAmount minBet = tableRules.getMinBet();
        int numPlayersNotBankrupt = 0;
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                Player player = seat.getPlayer();
                if (player.getBankroll().isGreaterThanOrEqualTo(minBet)) {
                    numPlayersNotBankrupt++;
                }
            }
        }
        return numPlayersNotBankrupt == 0;
    }

    private void takeBetsFromPlayers() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                Player player = seat.getPlayer();
                CurrencyAmount betAmount = player.getBet(this);
                playerPayCasino(player, betAmount);
                PlayerHand hand = seat.addNewHand(betAmount);
                show(hand, "placed a bet of " + betAmount + ".");
            }
        }
    }

    private boolean anySeatsHaveBets() {
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                if (!hand.getBetAmount().isZero()) {
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
                if (!hand.getBetAmount().isZero()) {
                    Card card = shoe.drawTopCard();
                    show(hand, "got " + card.toString(true, false) + ".");
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
        show("Dealer reveals " + dealerGender.getHisHer(false)
                + " first card, which was "
                + dealerHand.getFirstCard().toString(true, false) + ".");
    }

    private void handleInsurance() {
        if (dealerHand.getFirstCard().getValue() == Value.Ace) {
            show("Dealer asks the players if they want insurance.");

            Map<SeatNumber, CurrencyAmount> insuranceBets = new HashMap<>();

            for (SeatNumber seatNumber : SeatNumber.values()) {
                Seat seat = seats.get(seatNumber);
                CurrencyAmount maxInsuranceBet = seat.computeBetSum().compute(CurrencyComputation.halfOf);
                if (!maxInsuranceBet.isZero()) {
                    CurrencyAmount desiredInsuranceBet = seat.getPlayer().getInsuranceBet(
                            maxInsuranceBet,
                            seat.getHands().get(0), // at this point, each occupied seat will have exactly one hand.
                            dealerHand.getFirstCard());
                    if (!desiredInsuranceBet.isZero()) {
                        playerPayCasino(seat.getPlayer(), desiredInsuranceBet);
                        show(seat, "put down " + desiredInsuranceBet
                                + " as insurance against the dealer having blackjack.");
                        insuranceBets.put(seatNumber, desiredInsuranceBet);
                    } else {
                        show(seat, "declines the insurance bet.");
                    }
                }
            }

            if (insuranceBets.isEmpty()) {
                show("Nobody made an insurance bet.");
                return;
            }

            if (dealerHand.getSecondCard().getValue().isTen()) {
                show("Dealer turns over the hole card, and it's "
                        + dealerHand.getSecondCard().toString(true, false) + ". Blackjack!"
                        + " All hands lose, insurance bets all pay double.");
                dealerHand.revealHoleCard(this);

                // for each player, pay insurance winnings, if any.
                for (SeatNumber seatNumber : SeatNumber.values()) {
                    CurrencyAmount insuranceBet = insuranceBets.get(seatNumber);
                    if (insuranceBet != null && !insuranceBet.isZero()) {
                        Seat seat = seats.get(seatNumber);
                        Player player = seat.getPlayer();

                        casinoPayPlayer(player, insuranceBet);
                        CurrencyAmount winnings = insuranceBet.compute(CurrencyComputation.doubleOf);
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
                show("Dealer turns over the hole card, and it's "
                        + dealerHand.getSecondCard().toString(false, true)
                        + ". Not a blackjack. Insurance bets all lose.");
                dealerHand.revealHoleCard(this);
            }
        }
    }

    private boolean handleDealerBlackjack() {
        if (!dealerHand.isBlackjack()) {
            if (dealerHand.getFirstCard().getValue().isTen()) {
                show("Dealer checks the hole card. No blackjack for the dealer.");
            }
            return false;
        }

        show("Dealer checks the hole card, and it was "
                + dealerHand.getSecondCard().toString(false, true)
                + ". Dealer got a blackjack with " + dealerHand.showCardsWithTotal());
        dealerHand.revealHoleCard(this);

        for (int i = SeatNumber.values().length - 1; i >= 0; --i) {
            SeatNumber seatNumber = SeatNumber.values()[i];
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                CurrencyAmount betAmount = hand.removeBet();
                if (!betAmount.isZero()) {
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
                if (!hand.getBetAmount().isZero()) {
                    if (hand.isBlackjack()) {
                        handlePlayerBlackjack(hand);
                    }
                }
            }
        }
    }

    private void handlePlayerBlackjack(PlayerHand hand) {
        Player player = hand.getSeat().getPlayer();
        CurrencyAmount betAmount = hand.removeBet();
        casinoPayPlayer(player, betAmount); // original bet returned
        CurrencyAmount winnings = tableRules.getBlackjackPayOptions().computePlayerBlackjackWinnings(betAmount);
        casinoPayPlayer(player, winnings);
        show(hand, "got a blackjack, and won " + winnings + ".");
        discardTray.addCards(hand.removeCards());
    }

    private void playersPlay() {
        Card dealerUpcard = dealerHand.getFirstCard();
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            List<PlayerHand> hands = seat.getHands();

            // Note: the number of hands can increase while inside the loop, due to splits, so we can't for-each over it.
            //noinspection ForLoopReplaceableByForEach
            for (int handIndex = 0; handIndex < hands.size(); handIndex++) {
                PlayerHand hand = hands.get(handIndex);
                if (!hand.isSplitHandAndIsFinished()) {
                    if (!hand.getBetAmount().isZero()) {
                        Player player = seat.getPlayer();
                        boolean shallContinue = true;
                        while (shallContinue) {
                            String initialHand = hand.showCardsWithTotal();
                            BlackjackPlay playerAction = player.getPlay(hand, dealerUpcard);
                            CardCountStatus cardCountStatus = player.getCardCountStatus();
                            switch (playerAction) {
                                case Stand:
                                    handlePlayerStand(hand, cardCountStatus);
                                    shallContinue = false;
                                    break;
                                case Hit:
                                    shallContinue = handlePlayerHit(initialHand, hand, cardCountStatus);
                                    break;
                                case DoubleDown:
                                    handlePlayerDoubleDown(initialHand, hand, cardCountStatus);
                                    shallContinue = false;
                                    break;
                                case Split:
                                    shallContinue = handlePlayerSplit(hand, cardCountStatus);
                                    break;
                                case Surrender:
                                    handlePlayerSurrender(hand, cardCountStatus);
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
    }

    private void handlePlayerStand(
            PlayerHand hand,
            CardCountStatus cardCountStatus) {
        show(hand, "decides to stand.", cardCountStatus);
    }

    private boolean handlePlayerHit(
            String initialHand,
            PlayerHand hand,
            CardCountStatus cardCountStatus) {
        Card hitCard = shoe.drawTopCard();
        hand.addCard(hitCard);
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();

        if (hand.isBust()) {
            CurrencyAmount betAmount = hand.removeBet();
            show("Seat " + seat.getSeatNumeral()
                            + " : " + player.getPlayerName()
                            + " decides to hit with " + player.getGender().getHisHer(false)
                            + " hand of " + initialHand
                            + ", and got " + hitCard.toString(true, false)
                            + ". Hand is now " + hand.showCardsWithTotal()
                            + ". That's a bust. Lost bet of " + betAmount + ".",
                    cardCountStatus);
            return false;
        }

        if (hand.isCharlie() && tableRules.sevenCardCharlieRuleIsActive()) {
            handlePlayerCharlie(initialHand, hitCard, hand);
            return false;
        }

        if (hand.isTwentyOne()) {
            show("Seat " + seat.getSeatNumeral()
                            + " : " + player.getPlayerName()
                            + " decides to hit with " + player.getGender().getHisHer(false)
                            + " hand of " + initialHand
                            + ", and got " + hitCard.toString(false, true)
                            + ". Hand is now " + hand.showCardsWithTotal()
                            + ". " + player.getGender().getHeShe(true) + " must now stand.",
                    cardCountStatus);
            return false;
        }

        // The hit didn't bust them, they can have another play if they want.
        show("Seat " + seat.getSeatNumeral()
                        + " : " + player.getPlayerName()
                        + " decides to hit with " + player.getGender().getHisHer(false)
                        + " hand of " + initialHand
                        + ", and got " + hitCard.toString(true, false)
                        + ".",
                cardCountStatus);
        return true;
    }

    private void handlePlayerDoubleDown(
            String initialHand,
            PlayerHand hand,
            CardCountStatus cardCountStatus) {
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();

        CurrencyAmount originalBetAmount = hand.getBetAmount();
        playerPayCasino(player, originalBetAmount);
        hand.setBetAmount(originalBetAmount.compute(CurrencyComputation.doubleOf));

        Card doubleDownCard = shoe.drawTopCard();
        hand.addCard(doubleDownCard);

        if (hand.isBust()) {
            CurrencyAmount doubledBet = hand.removeBet();
            show("Seat " + seat.getSeatNumeral()
                            + " : " + player.getPlayerName()
                            + " decides to double down with " + player.getGender().getHisHer(false)
                            + " hand of " + initialHand
                            + ", and got " + doubleDownCard.toString(true, false)
                            + ". Hand is now " + hand.showCardsWithTotal()
                            + ". That's a bust. Lost doubled bet of " + doubledBet + ".",
                    cardCountStatus);
            return;
        }

        show("Seat " + seat.getSeatNumeral()
                        + " : " + player.getPlayerName()
                        + " decides to double down with " + player.getGender().getHisHer(false)
                        + " hand of " + initialHand
                        + ", and got " + doubleDownCard.toString(true, false)
                        + ". Hand is now " + hand.showCardsWithTotal()
                        + ". Must now stand. Bet is now "
                        + originalBetAmount.compute(CurrencyComputation.doubleOf) + ".",
                cardCountStatus);
    }

    private void handlePlayerCharlie(
            String initialHand,
            Card hitCard,
            PlayerHand hand) {
        Seat seat = hand.getSeat();
        Player player = seat.getPlayer();
        CurrencyAmount betAmount = hand.removeBet();
        casinoPayPlayer(player, betAmount); // original bet returned
        CurrencyAmount winnings = betAmount.compute(CurrencyComputation.doubleOf);
        casinoPayPlayer(player, winnings);
        show("Seat " + seat.getSeatNumeral()
                + " : " + player.getPlayerName()
                + " decides to hit with " + player.getGender().getHisHer(false)
                + " hand of " + initialHand
                + ", and got " + hitCard.toString(true, false)
                + ". Hand is now " + hand.showCardsWithTotal()
                + ". That's a seven-card charlie! Win was double, "
                + player.getGender().getHeShe(false) + " got " + winnings + ".");
        discardTray.addCards(hand.removeCards());
    }

    private boolean handlePlayerSplit(
            PlayerHand hand,
            CardCountStatus cardCountStatus) {
        boolean areSplitAces = hand.getFirstCard().getValue() == Value.Ace;

        Seat seat = hand.getSeat();
        int numSplitsSoFar = seat.getNumSplitsSoFar();
        CurrencyAmount betAmount = hand.getBetAmount();
        if (numSplitsSoFar == 0) {
            show(hand, "decides to split, and adds another bet of " + betAmount + ".", cardCountStatus);
        } else {
            show(hand, "decides to split again, and adds another bet of " + betAmount + ".", cardCountStatus);
        }
        playerPayCasino(seat.getPlayer(), betAmount);


        PlayerHand rightSplitHand = hand.separateOutRightSplit(betAmount);
        PlayerHand leftSplitHand = hand;

        hand.getSeat().addHand(rightSplitHand);

        // Make sure we don't use this variable anymore.
        //noinspection UnusedAssignment
        hand = null;

        Card cardForLeftSplit = shoe.drawTopCard();
        leftSplitHand.addCard(cardForLeftSplit);
        show(seat, "got " + cardForLeftSplit.toString(true, false)
                + " on the left split, and now has " + leftSplitHand + ".");

        Card cardForRightSplit = shoe.drawTopCard();
        rightSplitHand.addCard(cardForRightSplit);
        show(seat, "got " + cardForRightSplit.toString(true, false)
                + " on the right split, and now has " + rightSplitHand + ".");

        if (areSplitAces && !tableRules.canHitSplitAces()) {
            // blackjacks pay just like 21, and you can't hit anymore.
            leftSplitHand.setSplitHandAndIsFinished();
            rightSplitHand.setSplitHandAndIsFinished();
            return false;
        }

        if (rightSplitHand.isBlackjack()) {
            handlePlayerBlackjack(rightSplitHand);
            rightSplitHand.setSplitHandAndIsFinished();
        }

        if (leftSplitHand.isBlackjack()) {
            handlePlayerBlackjack(leftSplitHand);

            // we won't continue on this left hand, and the continue setting on the right hand has been set,
            // in case of blackjack on it
            return false;
        }

        // player can play on the left hand still.
        return true;
    }

    private void handlePlayerSurrender(
            PlayerHand hand,
            CardCountStatus cardCountStatus) {
        Player player = hand.getSeat().getPlayer();
        CurrencyAmount halfBetAmount = hand.removeBet().compute(CurrencyComputation.halfOf);
        casinoPayPlayer(player, halfBetAmount);
        show(hand, "surrenders, and loses half of "
                        + player.getGender().getHisHer(false) + " bet (" + halfBetAmount + ").",
                cardCountStatus);
    }

    private void dealerPlays() {
        show("Dealer turns over the hole card, and it's "
                + dealerHand.getSecondCard().toString(true, false)
                + ". Dealer's hand is " + dealerHand.showCardsWithTotal() + ".");
        while (dealerHand.shouldHit(tableRules)) {
            Card dealerHitCard = shoe.drawTopCard();
            dealerHand.addCard(dealerHitCard);
            if (dealerHand.isBust()) {
                show("Dealer hits, and gets a " + dealerHitCard.toString(true, false)
                        + ". Now showing " + dealerHand.showCardsWithTotal()
                        + ". That's a bust, remaining players all win.");
            } else {
                show("Dealer hits, and gets a " + dealerHitCard.toString(true, false)
                        + ". Now showing " + dealerHand.showCardsWithTotal()
                        + ".");
            }
        }
        if (!dealerHand.isBust()) {
            show("Dealer stands with " + dealerHand.showCardsWithTotal() + ".");
        }
    }

    private void resolveBets() {
        show("Play is complete for this hand. Resolving remaining bets.");
        for (int i = SeatNumber.values().length - 1; i >= 0; --i) {
            SeatNumber seatNumber = SeatNumber.values()[i];
            Seat seat = seats.get(seatNumber);
            for (PlayerHand hand : seat.getHands()) {
                CurrencyAmount betAmount = hand.removeBet();
                if (!betAmount.isZero()) {
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
                    if (!hand.getBetAmount().isZero()) {
                        throw new RuntimeException("There is still a bet at seat number " + seat.getSeatNumber());
                    }
                    discardTray.addCards(hand.removeCards());
                }
                seat.destroyHands();
                show(seat, "has a bankroll of " + player.getBankroll() + ".");
            }
        }
    }

    private void showAndDisplayPlayerResults() {
        showAndDisplay("");
        for (SeatNumber seatNumber : SeatNumber.values()) {
            Seat seat = seats.get(seatNumber);
            if (seat.hasPlayer()) {
                Player player = seat.getPlayer();
                CurrencyAmount initial = player.getInitialBankroll();
                showAndDisplay("Player " + player.getPlayerName()
                        + " started with " + initial
                        + " and " + player.getBankroll().getCurrencyAmountCopy().computeDifference(initial)
                        + ".");
            }
        }
    }

    private void showAndDisplay(String message) {
        if (!casino.getOutput().isDisplaying()) {
            System.out.println(message);
        }
        show(message);
    }

    private void show(String message) {
        casino.getOutput().showMessage(message);
    }

    private void show(String message, CardCountStatus cardCountStatus) {
        casino.getOutput().showMessage(message, cardCountStatus);
    }

    private void show(
            Seat seat,
            String message) {
        show("Seat " + seat.getSeatNumeral() + " : " + seat.getPlayer().getPlayerName() + " " + message);
    }

    private void show(
            PlayerHand hand,
            String message) {
        show(hand, message, null);
    }

    private void show(
            PlayerHand hand,
            String message,
            CardCountStatus cardCountStatus) {
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
        casino.getOutput().showMessage(msg, cardCountStatus);
    }
}
