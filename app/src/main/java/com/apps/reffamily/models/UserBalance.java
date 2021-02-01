package com.apps.reffamily.models;

import java.io.Serializable;

public class UserBalance implements Serializable {
    private int orders;
    private int user_balance;
    private int delivery_fee;

    public int getOrders() {
        return orders;
    }

    public int getUser_balance() {
        return user_balance;
    }

    public int getDelivery_fee() {
        return delivery_fee;
    }
}
