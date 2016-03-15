package com.tanhevg.westpac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * todo rather than using <code>DecoratedOrder.isRemoved()</code> here, re-use the
 * <code>orderById</code> map
 */
class Level {
    private long totalSize;
    private final ArrayList<DecoratedOrder> orders = new ArrayList<>();
    private final PurgeStrategy purgeStrategy;
    private int removeCount;

    Level(PurgeStrategy purgeStrategy) {
        this.purgeStrategy = purgeStrategy;
    }

    void addOrder(DecoratedOrder o) {
        orders.add(o);
        totalSize += o.getSize();
        o.setQueuePosition(orders.size());
    }

    void modify(DecoratedOrder o, long newSize) {
        totalSize += (newSize - o.getSize());
        o.setSize(newSize);
    }

    void remove(DecoratedOrder o) {
        totalSize -= o.getSize();
        o.setRemoved(true);
        purge(o);
    }

    private void purge(DecoratedOrder o) {
        int purgeRemoveCount = purgeStrategy.getRemoveCount();
        if (purgeRemoveCount == 1) {
            orders.remove(Collections.binarySearch(orders, o));
        } else if (purgeRemoveCount > 0 && ++removeCount == purgeRemoveCount) {
            removeCount = 0;
            for (Iterator<DecoratedOrder> iterator = orders.iterator(); iterator.hasNext(); ) {
                DecoratedOrder order = iterator.next();
                if (order.isRemoved()) {
                    iterator.remove();
                }
            }
        }
    }

    long getTotalSize() {
        return totalSize;
    }

    Iterator<Order> iterator() {
        return new LevelIterator(); //todo use object pool; release back when hasNext() returns false
    }

    private class LevelIterator implements Iterator<Order> {
        int index;

        @Override
        public boolean hasNext() {
            return advanceToRealOrder();
        }

        @Override
        public Order next() {
            if (!advanceToRealOrder()) {
                throw new NoSuchElementException(String.valueOf(index));
            }
            return orders.get(index++).getOrder();
        }

        private boolean advanceToRealOrder() {
            while (orders.size() > index && orders.get(index).isRemoved()) {
                if (purgeStrategy.removeInIterator()) {
                    orders.remove(index);
                } else {
                    index++;
                }
            }
            return index < orders.size();
        }
    }

}
