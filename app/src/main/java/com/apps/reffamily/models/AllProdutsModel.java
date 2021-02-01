package com.apps.reffamily.models;

import java.io.Serializable;
import java.util.List;

public class AllProdutsModel implements Serializable {
    private List<SingleProductModel> data;

    public List<SingleProductModel> getData() {
        return data;
    }


}