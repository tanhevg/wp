package com.tanhevg.westpac;

import java.util.Iterator;

class SymbolOrderBook {
    private final SideOrderBook bidBook;
    private final SideOrderBook askBook;

    SymbolOrderBook(PurgeStrategy purgeStrategy) {
        bidBook = new SideOrderBook(true, purgeStrategy);
        askBook = new SideOrderBook(false, purgeStrategy);

    }

    private SideOrderBook getOrderBook(char side) {
        if (side == 'B') {
            return bidBook;
        } else if (side == 'A') {
            return askBook;
        } else {
            throw new OrderBookException("Invalid side: " + side);
        }
    }

    void add(DecoratedOrder o) {
        getOrderBook(o.getSide()).add(o);
    }


    void remove(DecoratedOrder o) {
        getOrderBook(o.getSide()).remove(o);
    }


    void modify(DecoratedOrder o, long newSize) {
        getOrderBook(o.getSide()).modify(o, newSize);
    }


    double getPrice(char side, int level) {
        return getOrderBook(side).getPrice(level);
    }


    long getSize(char side, int level) {
        return getOrderBook(side).getSize(level);
    }


    Iterator<Order> iterator(char side) {
        return getOrderBook(side).iterator();
    }

    boolean isEmpty() {
        return bidBook.isEmpty() && askBook.isEmpty();
    }

    boolean hasBid() {
        return !bidBook.isEmpty();
    }

    boolean hasAsk() {
        return !askBook.isEmpty();
    }
}
