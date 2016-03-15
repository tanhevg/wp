package com.tanhevg.westpac;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;

import java.util.*;

/**
 * Created by tanhevg on 05/03/2016.
 */
public class Level {
    private long totalSize;
    private TLongObjectMap<DecoratedOrder> orderById = new TLongObjectHashMap<>();


    public void addOrder(DecoratedOrder o) {
        orderById.put(o.getId(), o);
        totalSize += o.getSize();
        o.setQueuePosition(orderById.size());
    }

    public void modify(DecoratedOrder o, long newSize) {
        totalSize += (newSize - o.getSize());
        o.setSize(newSize);
    }

    public void remove(DecoratedOrder o) {
        totalSize -= o.getSize();
        orderById.remove(o.getId());
    }

    public long getTotalSize() {
        return totalSize;
    }

    // todo re-work
    public List<DecoratedOrder> getList() {
        List<DecoratedOrder> ret = new ArrayList<>();
        ret.addAll(orderById.valueCollection());
        Collections.sort(ret, (o1, o2) ->
                o1.getQueuePosition() - o2.getQueuePosition());
        return ret;
    }

}
