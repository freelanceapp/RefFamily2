package com.apps.reffamily.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private SingleProductModel data;
    private int status;

    public SingleProductModel getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
