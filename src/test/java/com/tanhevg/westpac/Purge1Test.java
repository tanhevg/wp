package com.tanhevg.westpac;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Purge1Test extends OrderBookTest {
    @Before
    public void setUp() throws Exception {
        orderBook = new OrderBook(new PurgeStrategy(1));
    }

}
