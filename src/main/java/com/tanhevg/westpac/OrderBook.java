package com.tanhevg.westpac;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderBook {

    private final TLongObjectMap<DecoratedOrder> orderById = new TLongObjectHashMap<>();
    private final Map<String, SymbolOrderBook> orderBookBySymbol = new HashMap<>();
    private final PurgeStrategy purgeStrategy;

    public OrderBook(PurgeStrategy purgeStrategy) {
        this.purgeStrategy = purgeStrategy;
    }

    public void add(Order o) {
        DecoratedOrder order = new DecoratedOrder(o);// todo use object pool;
        orderById.put(order.getId(), order);

        SymbolOrderBook book = orderBookBySymbol.computeIfAbsent(o.getSym(),
                sym -> new SymbolOrderBook(purgeStrategy));
        book.add(order);
    }


    public boolean remove(long orderId) {
        DecoratedOrder order = orderById.get(orderId);
        if (order == null) {
            return false;
        }
        SymbolOrderBook symbolOrderBook = getSymbolOrderBook(order.getSym());
        symbolOrderBook.remove(order);
        if (symbolOrderBook.isEmpty()) {
            orderBookBySymbol.remove(order.getSym());
        }
        return true;
    }


    public boolean modify(long orderId, long newSize) {
        DecoratedOrder order = orderById.get(orderId);
        if (order == null) {
            return false;
        }
        getSymbolOrderBook(order.getSym()).modify(order, newSize);
        return true;
    }


    public double getPrice(String symbol, char side, int level) {
        return getSymbolOrderBook(symbol).getPrice(side, level);
    }


    public long getSize(String symbol, char side, int level) {
        return getSymbolOrderBook(symbol).getSize(side, level);
    }


    public Iterator<Order> iterator(String symbol, char side) {
        return getSymbolOrderBook(symbol).iterator(side);
    }

    private SymbolOrderBook getSymbolOrderBook(String symbol) {
        SymbolOrderBook ret = orderBookBySymbol.get(symbol);
        if (ret == null) {
            throw new OrderBookException("No such symbol: " + symbol);
        }
        return ret;
    }
}
