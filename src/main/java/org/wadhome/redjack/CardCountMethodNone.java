package org.wadhome.redjack;

class CardCountMethodNone extends CardCountMethod {

    CardCountMethodNone(TableRules tableRules) {
        super(tableRules);
    }

    @Override
    void observeCard(Card card) {
        // do nothing
    }

    @Override
    void observeShuffle() {
        // do nothing
    }
}
