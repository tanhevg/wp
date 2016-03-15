package com.tanhevg.westpac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by tanhevg on 05/03/2016.
 */
@RunWith(JUnit4.class)
public class OrderBookTest {

    private OrderBook orderBook = new OrderBook();

    @Test
    public void testBid() {
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

        List<Order> orders = orderBook.getOrders("X", 'B');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 4);
        assertEquals(orders.get(1).getId(), 3);
        assertEquals(orders.get(2).getId(), 1);
    }

    @Test
    public void testAsk() {
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

        List<Order> orders = orderBook.getOrders("X", 'A');
        assertEquals(orders.size(), 3);
        assertEquals(orders.get(0).getId(), 1);
        assertEquals(orders.get(1).getId(), 2);
        assertEquals(orders.get(2).getId(), 3);


    }
}
