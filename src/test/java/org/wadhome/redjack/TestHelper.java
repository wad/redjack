package org.wadhome.redjack;

public class TestHelper {

    Card c(Value value) {
        return new Card(0, Suite.Clubs, value);
    }

    Card c2() {
        return new Card(0, Suite.Spades, Value.Two);
    }

    Card c3() {
        return new Card(0, Suite.Spades, Value.Three);
    }

    Card c4() {
        return new Card(0, Suite.Spades, Value.Four);
    }

    Card c5() {
        return new Card(0, Suite.Spades, Value.Five);
    }

    Card c6() {
        return new Card(0, Suite.Spades, Value.Six);
    }

    Card c7() {
        return new Card(0, Suite.Spades, Value.Seven);
    }

    Card c8() {
        return new Card(0, Suite.Spades, Value.Eight);
    }

    Card c9() {
        return new Card(0, Suite.Spades, Value.Nine);
    }

    Card cT() {
        return new Card(0, Suite.Spades, Value.Ten);
    }

    Card cA() {
        return new Card(0, Suite.Spades, Value.Ace);
    }
}
