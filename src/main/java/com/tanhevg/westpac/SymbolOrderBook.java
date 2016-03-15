package com.tanhevg.westpac;

import gnu.trove.map.TLongObjectMap;

import java.util.List;

/**
 * Created by tanhevg on 05/03/2016.
 */
public class SymbolOrderBook {
    private final SideOrderBook bidBook;
    private final SideOrderBook askBook;

    public SymbolOrderBook(TLongObjectMap<DecoratedOrder> orderById) {
        bidBook = new SideOrderBook(true);
        askBook = new SideOrderBook(false);

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

    public void add(DecoratedOrder o) {
        getOrderBook(o.getSide()).add(o);
    }


    public void remove(DecoratedOrder o) {
        getOrderBook(o.getSide()).remove(o);
    }


    public void modify(DecoratedOrder o, long newSize) {
        getOrderBook(o.getSide()).modify(o, newSize);
    }


    public double getPrice( char side, int level) {
        return getOrderBook(side).getPrice(level);
    }


    public long getSize(char side, int level) {
        return getOrderBook(side).getSize(level);
    }


    public List<Order> getOrders(char side) {
        return getOrderBook(side).getOrders();
    }
}
