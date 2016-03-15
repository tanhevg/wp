package com.tanhevg.westpac;

class DecoratedOrder implements Comparable<DecoratedOrder> {
    private Order order;

    private int queuePosition;
    private boolean removed;

    DecoratedOrder(Order order) {
        this.order = order;
    }

    Order getOrder() {
        return order;
    }

    void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    void setSize(long newSize) {
        order.size = newSize;
    }

    long getId() {
        return order.getId();
    }

    char getSide() {
        return order.getSide();
    }

    double getPrice() {
        return order.getPrice();
    }

    long getSize() {
        return order.getSize();
    }

    String getSym() {
        return order.getSym();
    }

    boolean isRemoved() {
        return removed;
    }

    void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public int compareTo(DecoratedOrder o) {
        return this.queuePosition - o.queuePosition;
    }
}
