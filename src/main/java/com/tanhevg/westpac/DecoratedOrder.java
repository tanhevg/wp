package com.tanhevg.westpac;

/**
 * Created by tanhevg on 05/03/2016.
 */
public class DecoratedOrder {
    private Order order;

    private int queuePosition;

    public DecoratedOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public void setSize(long newSize) {
        order.size = newSize;
    }

    public long getId() {
        return order.getId();
    }

    public char getSide() {
        return order.getSide();
    }

    public double getPrice() {
        return order.getPrice();
    }

    public long getSize() {
        return order.getSize();
    }

    public String getSym() {
        return order.getSym();
    }
}
