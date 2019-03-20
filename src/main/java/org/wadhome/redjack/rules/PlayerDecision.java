package org.wadhome.redjack.rules;

public class PlayerDecision {
    private BlackjackPlay blackjackPlay;
    private String playerComment;

    public PlayerDecision(BlackjackPlay blackjackPlay) {
        this.blackjackPlay = blackjackPlay;
    }

    public PlayerDecision(
            BlackjackPlay blackjackPlay,
            String playerComment) {
        this.blackjackPlay = blackjackPlay;
        this.playerComment = playerComment;
    }

    public BlackjackPlay getBlackjackPlay() {
        return blackjackPlay;
    }

    public String getPlayerComment() {
        return playerComment;
    }
}
