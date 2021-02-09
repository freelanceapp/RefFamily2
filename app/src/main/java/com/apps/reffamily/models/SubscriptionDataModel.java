package com.apps.reffamily.models;

import java.io.Serializable;
import java.util.List;

public class SubscriptionDataModel implements Serializable {
    private List<SubscriptionModel> data;

    public List<SubscriptionModel> getData() {
        return data;
    }
}
