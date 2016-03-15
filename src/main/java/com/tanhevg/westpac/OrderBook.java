package com.tanhevg.westpac;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanhevg on 05/03/2016.
 */
public class OrderBook {

    private final TLongObjectMap<DecoratedOrder> orderById = new TLongObjectHashMap<>();
    private final Map<String, SymbolOrderBook> orderBookBySymbol = new HashMap<>();

    public void add(Order o) {
        DecoratedOrder order = new DecoratedOrder(o);// todo use object pool;
        orderById.put(order.getId(), order);

        SymbolOrderBook book = orderBookBySymbol.computeIfAbsent(o.getSym(),
                sym -> new SymbolOrderBook(orderById));
        book.add(order);
    }


    public boolean remove(long orderId) {
        DecoratedOrder order = orderById.get(orderId);
        if (order == null) {
            return false;
        }
        getSymbolOrderBook(order.getSym()).remove(order);
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


    public List<Order> getOrders(String symbol, char side) {
        return getSymbolOrderBook(symbol).getOrders(side);
    }

    private SymbolOrderBook getSymbolOrderBook(String symbol) {
        SymbolOrderBook ret = orderBookBySymbol.get(symbol);
        if (ret == null) {
            throw new OrderBookException("No such symbol: " + symbol);
        }
        return ret;
    }
}
