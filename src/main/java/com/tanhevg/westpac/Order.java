package com.tanhevg.westpac;

public class Order {

    // private variables
    long id;
    double price;
    char side; // should be B or A
    long size;
    String sym;

    //constructor
    public Order(long orderid, double orderprice, char orderside, long ordersize, String ordersym) {
        id = orderid;
        price = orderprice;
        size = ordersize;
        side = orderside;
        sym = ordersym;
    }

    //methods
    long getId() {
        return id;
    }

    double getPrice() {
        return price;
    }

    long getSize() {
        return size;
    }

    String getSym() {
        return sym;
    }

    char getSide() {
        return side;
    }



 }
