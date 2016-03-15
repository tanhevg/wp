package com.tanhevg.westpac;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.hash.TDoubleObjectHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanhevg on 05/03/2016.
 */
public class SideOrderBook {
    private boolean bid;
    private final TDoubleObjectMap<Level> levelByPrice = new TDoubleObjectHashMap<>();
    private final TDoubleArrayList sortedPrices = new TDoubleArrayList();

    public SideOrderBook(boolean bid) {
        this.bid = bid;
    }

    public void add(DecoratedOrder order) {
        Level level = levelByPrice.get(order.getPrice());
        if (level != null) {
            level.addOrder(order);
        } else {
            addLevel(order);
        }
    }


    public void remove(DecoratedOrder o) {
        Level level = levelByPrice.get(o.getPrice());
        level.remove(o);
        if (level.getTotalSize() == 0) {
            sortedPrices.remove(o.getPrice());
            levelByPrice.remove(o.getPrice());
        }
    }


    public void modify(DecoratedOrder o, long newSize) {
        Level level = levelByPrice.get(o.getPrice());
        level.modify(o, newSize);
    }

    private int bidAskLevel(int level) {
        return bid ? sortedPrices.size() - level  - 1 : level;
    }


    public double getPrice(int level) {
        validateLevel(level);
        return sortedPrice(level - 1);
    }

    private void validateLevel(int level) {
        if (level < 1 || level > sortedPrices.size()) {
            throw new OrderBookException("Level out of bounds: " + level + "; should be between 1 and " + (sortedPrices.size() + 1));
        }
    }


    public long getSize(int level) {
        validateLevel(level);
        return sortedLevel(level - 1).getTotalSize();
    }

    private void addLevel(DecoratedOrder order) {
        int insertionPoint = sortedPrices.binarySearch(order.getPrice());
        insertionPoint = -(insertionPoint + 1);
        Level level = new Level();
        level.addOrder(order);
        levelByPrice.put(order.getPrice(), level);
        sortedPrices.insert(insertionPoint, order.getPrice());
    }


    // todo rework
    public List<Order> getOrders() {
        List<Order> ret = new ArrayList<>();
        for (int i = 0; i < sortedPrices.size(); i++) {
            Level level = sortedLevel(i);
            for (DecoratedOrder order : level.getList()) {
                ret.add(order.getOrder());
            }
        }
        return ret;
    }

    private Level sortedLevel(int i) {
        return levelByPrice.get(sortedPrice(i));
    }

    private double sortedPrice(int i) {
        return sortedPrices.get(bidAskLevel(i));
    }
}
