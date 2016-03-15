package com.tanhevg.westpac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

//todo more test cases
@SuppressWarnings("Duplicates")
public abstract class OrderBookTest {

    protected OrderBook orderBook;

    @Test
    public void bidIteratorIsCorrectAfterModifyingAndRemovingPartOfLevel() {
        Order o1 = new Order(1, 0.1, 'B', 10, "X");
        Order o2 = new Order(2, 0.2, 'B', 20, "X");
        Order o3 = new Order(3, 0.2, 'B', 25, "X");
        Order o4 = new Order(4, 0.3, 'B', 30, "X");

        orderBook.add(o1);
        orderBook.add(o2);
        orderBook.add(o3);
        orderBook.add(o4);

        assertEquals(orderBook.getPrice("X", 'B', 1), 0.3, 0.0);
        assertEquals(orderBook.getPrice("X", 'B', 2), 0.2, 0.0);
        assertEquals(orderBook.getPrice("X", 'B', 3), 0.1, 0.0);
        assertEquals(orderBook.getSize("X", 'B', 1), 30);
        assertEquals(orderBook.getSize("X", 'B', 2), 45);
        assertEquals(orderBook.getSize("X", 'B', 3), 10);

        orderBook.modify(3, 15);
        assertEquals(orderBook.getSize("X", 'B', 2), 35);

        orderBook.remove(2);
        assertEquals(orderBook.getSize("X", 'B', 2), 15);

        List<Order> orders = buildList("X", 'B');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 4);
        assertEquals(orders.get(1).getId(), 3);
        assertEquals(orders.get(2).getId(), 1);
        orders = buildList("X", 'B');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 4);
        assertEquals(orders.get(1).getId(), 3);
        assertEquals(orders.get(2).getId(), 1);
    }

    @Test
    public void bidIteratorIsCorrectAfterRemovingTopLevel() {
        Order o1 = new Order(1, 0.1, 'B', 10, "X");
        Order o2 = new Order(2, 0.2, 'B', 20, "X");
        Order o3 = new Order(3, 0.2, 'B', 25, "X");
        Order o4 = new Order(4, 0.3, 'B', 30, "X");

        orderBook.add(o1);
        orderBook.add(o2);
        orderBook.add(o3);
        orderBook.add(o4);

        orderBook.remove(1);

        List<Order> orders = buildList("X", 'B');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0), o4);
        assertEquals(orders.get(1), o2);
        assertEquals(orders.get(2), o3);
        orders = buildList("X", 'B');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0), o4);
        assertEquals(orders.get(1), o2);
        assertEquals(orders.get(2), o3);
    }

    @Test
    public void bidIteratorIsCorrectAfterRemovingMiddleLevel() {
        Order o1 = new Order(1, 0.1, 'B', 10, "X");
        Order o2 = new Order(2, 0.2, 'B', 20, "X");
        Order o3 = new Order(3, 0.2, 'B', 25, "X");
        Order o4 = new Order(4, 0.3, 'B', 30, "X");

        orderBook.add(o1);
        orderBook.add(o2);
        orderBook.add(o3);
        orderBook.add(o4);

        orderBook.remove(2);
        orderBook.remove(3);

        List<Order> orders = buildList("X", 'B');
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), o4);
        assertEquals(orders.get(1), o1);
        orders = buildList("X", 'B');
        assertEquals(orders.size(), 2);
        assertEquals(orders.get(0), o4);
        assertEquals(orders.get(1), o1);
    }

    private List<Order> buildList(String symbol, char side) {
        List<Order> ret = new ArrayList<>();
        for (Iterator<Order> it = orderBook.iterator(symbol, side); it.hasNext();) {
            ret.add(it.next());
        }
        return ret;
    }

    @Test
    public void askIteratorIsCorrectAfterModifyingAndRemovingPartOfLevel() {
        Order o1 = new Order(1, 0.1, 'A', 10, "X");
        Order o2 = new Order(2, 0.2, 'A', 20, "X");
        Order o3 = new Order(3, 0.2, 'A', 25, "X");
        Order o4 = new Order(4, 0.3, 'A', 30, "X");

        orderBook.add(o1);
        orderBook.add(o2);
        orderBook.add(o3);
        orderBook.add(o4);

        assertEquals(orderBook.getPrice("X", 'A', 1), 0.1, 0.0);
        assertEquals(orderBook.getPrice("X", 'A', 2), 0.2, 0.0);
        assertEquals(orderBook.getPrice("X", 'A', 3), 0.3, 0.0);
        assertEquals(orderBook.getSize("X", 'A', 1), 10);
        assertEquals(orderBook.getSize("X", 'A', 2), 45);
        assertEquals(orderBook.getSize("X", 'A', 3), 30);

        orderBook.modify(3, 15);
        assertEquals(orderBook.getSize("X", 'A', 2), 35);

        orderBook.remove(4);

        List<Order> orders = buildList("X", 'A');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 1);
        assertEquals(orders.get(1).getId(), 2);
        assertEquals(orders.get(2).getId(), 3);
        orders = buildList("X", 'A');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 1);
        assertEquals(orders.get(1).getId(), 2);
        assertEquals(orders.get(2).getId(), 3);
    }

    @Test(expected = OrderBookException.class)
    public void emptyBookIteratorShouldThrowAnException() {
        buildList("foo", 'B');
    }

    @Test(expected = OrderBookException.class)
    public void iteratorAfterRemovingAllOrdersShouldThrowAnException() {
        Order o1 = new Order(1, 0.1, 'A', 10, "X");
        Order o2 = new Order(2, 0.2, 'A', 20, "X");
        Order o3 = new Order(3, 0.2, 'A', 25, "X");
        Order o4 = new Order(4, 0.3, 'A', 30, "X");

        orderBook.add(o2);
        orderBook.add(o1);
        orderBook.add(o4);
        orderBook.add(o3);

        orderBook.remove(4);
        orderBook.remove(3);
        orderBook.remove(2);
        orderBook.remove(1);
        buildList("X", 'A');
    }

    @Test
    public void iteratorAfterRemovingAllOrdersShouldReturnEmptyListIfOtherSidePresent() {
        Order o1 = new Order(1, 0.1, 'A', 10, "X");
        Order o2 = new Order(2, 0.2, 'A', 20, "X");
        Order o3 = new Order(3, 0.2, 'A', 25, "X");
        Order o4 = new Order(4, 0.3, 'A', 30, "X");

        orderBook.add(o2);
        orderBook.add(o1);
        orderBook.add(o4);
        orderBook.add(o3);
        orderBook.add(new Order(5, 0.01, 'B', 100, "X"));

        orderBook.remove(4);
        orderBook.remove(3);
        orderBook.remove(2);
        orderBook.remove(1);
        List<Order> orders = buildList("X", 'A');
        assertEquals(0, orders.size());
    }

    public void iteratorShouldReturnCorrectOrdering(){
        Order o1 = new Order(1, 0.1, 'A', 10, "X");
        Order o2 = new Order(2, 0.2, 'A', 20, "X");
        Order o3 = new Order(3, 0.2, 'A', 25, "X");
        Order o4 = new Order(4, 0.3, 'A', 30, "X");

        orderBook.add(o2);
        orderBook.add(o1);
        orderBook.add(o4);
        orderBook.add(o3);

        List<Order> orders = buildList("X", 'A');
        assertEquals(orders.size(), 4);
        assertEquals(orders.get(0), o1);
        assertEquals(orders.get(1), o2);
        assertEquals(orders.get(2), o3);
        assertEquals(orders.get(3), o4);

    }
}
