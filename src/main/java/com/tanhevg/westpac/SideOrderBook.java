package com.tanhevg.westpac;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.hash.TDoubleObjectHashMap;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SideOrderBook {
    private boolean bid;
    private final TDoubleObjectMap<Level> levelByPrice = new TDoubleObjectHashMap<>();
    private final TDoubleArrayList sortedPrices = new TDoubleArrayList();
    private final PurgeStrategy purgeStrategy;

    boolean isEmpty() {
        return levelByPrice.size() == 0;
    }

    SideOrderBook(boolean bid, PurgeStrategy purgeStrategy) {
        this.bid = bid;
        this.purgeStrategy = purgeStrategy;
    }

    void add(DecoratedOrder order) {
        Level level = levelByPrice.get(order.getPrice());
        if (level != null) {
            level.addOrder(order);
        } else {
            addLevel(order);
        }
    }


    void remove(DecoratedOrder o) {
        Level level = levelByPrice.get(o.getPrice());
        level.remove(o);
        if (level.getTotalSize() == 0) {
            sortedPrices.remove(o.getPrice());
            levelByPrice.remove(o.getPrice());
        }
    }


    void modify(DecoratedOrder o, long newSize) {
        Level level = levelByPrice.get(o.getPrice());
        level.modify(o, newSize);
    }

    private int bidAskLevel(int level) {
        return bid ? sortedPrices.size() - level - 1 : level;
    }


    double getPrice(int level) {
        validateLevel(level);
        return sortedPrice(level - 1);
    }

    private void validateLevel(int level) {
        if (level < 1 || level > sortedPrices.size()) {
            throw new OrderBookException("Level out of bounds: " + level + "; should be between 1 and " + (sortedPrices.size() + 1));
        }
    }


    long getSize(int level) {
        validateLevel(level);
        return sortedLevel(level - 1).getTotalSize();
    }

    private void addLevel(DecoratedOrder order) {
        int insertionPoint = sortedPrices.binarySearch(order.getPrice());
        insertionPoint = -(insertionPoint + 1);
        Level level = new Level(purgeStrategy); //todo use object pool
        level.addOrder(order);
        levelByPrice.put(order.getPrice(), level);
        sortedPrices.insert(insertionPoint, order.getPrice());
    }

    private Level sortedLevel(int i) {
        return levelByPrice.get(sortedPrice(i));
    }

    private double sortedPrice(int i) {
        return sortedPrices.get(bidAskLevel(i));
    }

    Iterator<Order> iterator() {
        return new SideIterator(); //todo use object pool; release back when hasNext() returns false
    }

    private class SideIterator implements Iterator<Order> {
        private int levelIndex;
        private Iterator<Order> levelIterator;

        SideIterator() {
            if (levelIndex < levelByPrice.size()) {
                levelIterator = sortedLevel(0).iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (levelIterator == null) {
                return false;
            }
            while (!levelIterator.hasNext() && levelIndex < levelByPrice.size() - 1) {
                levelIterator = sortedLevel(++levelIndex).iterator();
            }
            return levelIndex < levelByPrice.size() && levelIterator.hasNext();
        }

        @Override
        public Order next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return levelIterator.next();
        }
    }
}
