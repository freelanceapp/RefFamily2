package com.apps.reffamily.models;

import java.io.Serializable;
import java.util.List;

public class AllSubCategoryModel implements Serializable {
    private List<SingleSubCategoryModel> data;
    private int status;

    public List<SingleSubCategoryModel> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}