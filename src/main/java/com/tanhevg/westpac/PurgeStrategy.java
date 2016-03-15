package com.tanhevg.westpac;

class PurgeStrategy {
    private final boolean removeInIterator;
    private final int removeCount;

    PurgeStrategy(boolean removeInIterator) {
        this(removeInIterator, 0);
    }

    PurgeStrategy(int removeCount) {
        this(false,removeCount);
    }

    PurgeStrategy() {
        this(false, 0);
    }

    private PurgeStrategy(boolean removeInIterator, int removeCount) {
        this.removeInIterator = removeInIterator;
        this.removeCount = removeCount;
    }

    boolean removeInIterator() {
        return removeInIterator;
    }

    int getRemoveCount() {
        return removeCount;
    }
}
