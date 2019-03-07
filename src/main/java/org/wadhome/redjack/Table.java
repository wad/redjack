package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private static final int MAX_HANDS_PER_TABLE = 7;

    private int tableNumber;
    private TableRules tableRules;
    private Shoe shoe;
    private DiscardTray discardTray;
    private Map<Integer, PlayerHand> hands;
    private DealerHand dealerHand;

    public Table(
            int tableNumber,
            TableRules tableRules) {
        this.tableNumber = tableNumber;
        this.tableRules = tableRules;
        int numDecks = tableRules.getNumDecks();
        this.shoe = new Shoe(tableNumber, numDecks);
        this.discardTray = new DiscardTray(numDecks);
        this.dealerHand = new DealerHand();
        this.hands = new HashMap<>(MAX_HANDS_PER_TABLE);
        for (int i = 0; i < MAX_HANDS_PER_TABLE; i++) {
            hands.put(i, new PlayerHand(i));
        }
    }

    public void showPreparationMessages() {
        show("The dealer is preparing table " + tableNumber + " for blackjack.");
        show(tableRules.toString());
    }

    Shoe getShoe() {
        return shoe;
    }

    public int getTableNumber() {
        return tableNumber;
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
            show("The dealer burned a card from the show: " + burnCard.print(false) + ".");
            discardTray.addCardToBottom(burnCard);
        }
    }

    public boolean assignPlayerToHand(
            int handNumber,
            Player player) {
        PlayerHand hand = hands.get(handNumber);
        if (hand.isInUse()) {
            show("Cannot seat player " + player.getPlayerName()
                    + " at hand " + handNumber
                    + ", it's occupied by " + hand.getPlayer() + ".");
            return false;
        }

        hand.setPlayer(player);
        return true;
    }

    public void removePlayerFromHand(int handNumber) {
        PlayerHand hand = hands.get(handNumber);
        if (!hand.isInUse()) {
            throw new RuntimeException("Nobody is at hand " + handNumber);
        }

        hand.removePlayer();
    }

    public void placeBet(
            int handNumber,
            MoneyPile betAmount) {
        if (betAmount.isLessThan(tableRules.getMinBet())) {
            throw new RuntimeException("Bet is too small.");
        }
        if (betAmount.isGreaterThan(tableRules.getMaxBet())) {
            throw new RuntimeException("Bet is too large.");
        }

        PlayerHand hand = hands.get(handNumber);
        if (hand.getBetAmount().hasMoney()) {
            throw new RuntimeException("Bug! Already a bet amount set on hand " + handNumber);
        }

        Player player = hand.getPlayer();
        if (betAmount.isGreaterThan(player.getBankroll())) {
            throw new RuntimeException("Bet exceeds bankroll");
        }

        hand.setBetAmount(betAmount);
        player.getBankroll().subtract(betAmount);
        show(hand, "placed a bet of " + betAmount + ".");
    }

    public boolean playRound() {
        if (!anyHandsHaveBets()) {
            show("No hands have bets.");
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

    private boolean anyHandsHaveBets() {
        for (int handNumber = 0; handNumber < MAX_HANDS_PER_TABLE; handNumber++) {
            if (hands.get(handNumber).getBetAmount().hasMoney()) {
                return true;
            }
        }
        return false;
    }

    private void dealCardsToPlayers() {
        for (int handNumber = 0; handNumber < MAX_HANDS_PER_TABLE; handNumber++) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse()) {
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
            show("Dealer decides not to ask for insurance.");
            // todo
        }
    }

    private boolean handleDealerBlackjack() {
        if (!dealerHand.isBlackjack()) {
            show("Dealer checks the hole card. No blackjack for the dealer.");
            return false;
        }

        show("Dealer checks the hole card... and got a blackjack: " + dealerHand.showCardsWithTotal());
        for (int handNumber = MAX_HANDS_PER_TABLE - 1; handNumber >= 0; handNumber--) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse()) {
                if (hand.getBetAmount().hasMoney()) {
                    Player player = hand.getPlayer();
                    if (hand.isBlackjack()) {
                        show(hand, "also had a blackjack, so it's a push.");
                        player.getBankroll().add(hand.getBetAmount()); // they get their original bet back.
                    } else {
                        show(hand, "loses " + hand.getBetAmount() + " to the dealers blackjack.");
                    }
                    discardTray.addCards(hand.removeCards());
                    hand.setBetAmount(MoneyPile.zero());
                }
            }
        }
        return true;
    }

    private void handlePlayerBlackjacks() {
        for (int handNumber = 0; handNumber < MAX_HANDS_PER_TABLE; handNumber++) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse()) {
                if (hand.getBetAmount().hasMoney()) {
                    handlePlayerBlackjack(hand);
                }
            }
        }
    }

    private void handlePlayerBlackjack(PlayerHand hand) {
        Player player = hand.getPlayer();
        if (hand.isBlackjack()) {
            MoneyPile betAmount = hand.getBetAmount();
            player.getBankroll().add(betAmount); // original bet returned
            MoneyPile winnings = betAmount.computeOneAndHalf();
            show(hand, "got a blackjack, and won " + winnings + ".");
            player.getBankroll().add(winnings);
            discardTray.addCards(hand.removeCards());
            hand.setBetAmount(MoneyPile.zero());
        }
    }

    private void playersPlay() {
        Card dealerUpcard = dealerHand.getFirstCard();
        for (int handNumber = 0; handNumber < MAX_HANDS_PER_TABLE; handNumber++) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse()) {
                if (hand.getBetAmount().hasMoney()) {
                    if (hand.hasAnyCards()) {
                        Player player = hand.getPlayer();
                        int numSplits = 0;
                        boolean shallContinue = true;
                        while (shallContinue) {
                            String initialHand = hand.showCardsWithTotal();
                            BlackjackPlay playerAction = player.getPlay(
                                    hand,
                                    dealerUpcard,
                                    numSplits,
                                    tableRules);
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
    }

    private boolean handlePlayerHit(
            String initialHand,
            PlayerHand hand) {
        Card hitCard = shoe.drawTopCard();
        hand.addCard(hitCard);
        Player player = hand.getPlayer();

        if (hand.isBust()) {
            show("Seat " + hand.getSeatNumber()
                    + " : " + player.getPlayerName()
                    + " decides to hit with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + hitCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". That's a bust. Lost bet of " + hand.getBetAmount() + ".");
            hand.removeCards();
            hand.setBetAmount(MoneyPile.zero());
            return false;
        }

        if (hand.isCharlie()) {
            handlePlayerCharlie(initialHand, hitCard, hand);
            return false;
        }

        if (hand.isTwentyOne()) {
            show("Seat " + hand.getSeatNumber()
                    + " : " + player.getPlayerName()
                    + " decides to hit with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + hitCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". " + player.getHisHer(true) + " must now stand.");
            return false;
        }

        // The hit didn't bust them, they can have another play if they want.
        show("Seat " + hand.getSeatNumber()
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

        Player player = hand.getPlayer();
        player.getBankroll().subtract(hand.getBetAmount());
        hand.setBetAmount(hand.getBetAmount().computeDouble());
        Card doubleDownCard = shoe.drawTopCard();
        hand.addCard(doubleDownCard);

        if (hand.isBust()) {
            show("Seat " + hand.getSeatNumber()
                    + " : " + player.getPlayerName()
                    + " decides to double down with " + player.getHisHer(false)
                    + " hand of " + initialHand
                    + ", and got a " + doubleDownCard
                    + ". Hand is now " + hand.showCardsWithTotal()
                    + ". That's a bust. Lost bet of " + hand.getBetAmount() + ".");
            hand.removeCards();
            hand.setBetAmount(MoneyPile.zero());
            return;
        }

        show("Seat " + hand.getSeatNumber()
                + " : " + player.getPlayerName()
                + " decides to double down with " + player.getHisHer(false)
                + " hand of " + initialHand
                + ", and got a " + doubleDownCard
                + ". Hand is now " + hand.showCardsWithTotal()
                + ". Must now stand. Bet is now " + hand.getBetAmount().computeDouble() + ".");
    }

    private void handlePlayerCharlie(
            String initialHand,
            Card hitCard,
            PlayerHand hand) {
        Player player = hand.getPlayer();
        MoneyPile betAmount = hand.getBetAmount();
        player.getBankroll().add(betAmount); // original bet returned
        MoneyPile winnings = betAmount.computeDouble();
        show("Seat " + hand.getSeatNumber()
                + " : " + player.getPlayerName()
                + " decides to hit with " + player.getHisHer(false)
                + " hand of " + initialHand
                + ", and got a " + hitCard
                + ". Hand is now " + hand.showCardsWithTotal()
                + ". That's a seven-card charlie! Win was double, "
                + player.getHeShe(false) + " got " + winnings + ".");
        player.getBankroll().add(winnings);
        discardTray.addCards(hand.removeCards());
        hand.setBetAmount(MoneyPile.zero());
    }

    private boolean handlePlayerSplit(PlayerHand playerHand) {
        playerHand.splitTheHand(tableRules);

        //todo
        return false;
    }

    private void handlePlayerSurrender(PlayerHand hand) {
        Player player = hand.getPlayer();
        MoneyPile halfBetAmount = hand.getBetAmount().computeHalf();
        show(hand, "surrendered, and loses half of "
                + player.getHisHer(false) + " bet (" + halfBetAmount + ").");

        // their original bet was already removed. This adds half back.
        player.getBankroll().add(halfBetAmount);

        discardTray.addCards(hand.removeCards());
        hand.setBetAmount(MoneyPile.zero());
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
        for (int handNumber = MAX_HANDS_PER_TABLE - 1; handNumber >= 0; handNumber--) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse() && hand.hasAnyCards()) {
                MoneyPile betAmount = hand.getBetAmount();
                if (betAmount.hasMoney()) {
                    Player player = hand.getPlayer();
                    switch (hand.compareWith(dealerHand)) {
                        case ThisLoses:
                            show(hand, "loses " + betAmount + ".");
                            break;
                        case same:
                            player.getBankroll().add(betAmount); // they get their original bet back.
                            show(hand, "pushes.");
                            break;
                        case ThisWins:
                            show(hand, "wins " + betAmount + ".");
                            player.getBankroll().add(betAmount); // they get their original bet back.
                            player.getBankroll().add(betAmount); // winnings
                            break;
                        default:
                            throw new RuntimeException("Bug!");
                    }
                    discardTray.addCards(hand.removeCards());
                    hand.setBetAmount(MoneyPile.zero());
                }
            }
        }
    }

    private void cleanupTable() {
        discardTray.addCards(dealerHand.removeCards());

        for (int handNumber = 0; handNumber < MAX_HANDS_PER_TABLE; handNumber++) {
            PlayerHand hand = hands.get(handNumber);
            if (hand.isInUse()) {
                if (hand.getBetAmount().hasMoney()) {
                    throw new RuntimeException("There is still a bet at seat number " + hand.getSeatNumber());
                }
                if (hand.getNumCards() != 0) {
                    throw new RuntimeException("There are still cards at seat number " + hand.getSeatNumber());
                }

                Player player = hand.getPlayer();
                show(hand, "has a bankroll of " + player.getBankroll() + ".");
            }
        }
    }

    private void show(String message) {
        Display.showMessage(message);
    }

    private void show(
            PlayerHand hand,
            String message) {
        String playerName = hand.getPlayer().getPlayerName();
        String msg;
        if (hand.getNumCards() == 0) {
            msg = "Seat "
                    + hand.getSeatNumber()
                    + " : "
                    + playerName
                    + " "
                    + message;
        } else {
            msg = "Seat "
                    + hand.getSeatNumber()
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
