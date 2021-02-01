package com.apps.reffamily.models;

import java.io.Serializable;

public class SinglePakcgesModel implements Serializable {
    private int id;
    private double price;
    private String currency;
    private int number_of_months;
    private String is_free;
    private String type;
    private String is_shown;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getNumber_of_months() {
        return number_of_months;
    }

    public String getIs_free() {
        return is_free;
    }

    public String getType() {
        return type;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
