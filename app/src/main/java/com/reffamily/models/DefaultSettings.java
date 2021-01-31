
package com.reffamily.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultSettings implements Serializable {
    private boolean isLanguageSelected = false;


    public DefaultSettings() {
    }

    public boolean isLanguageSelected() {
        return isLanguageSelected;
    }

    public void setLanguageSelected(boolean languageSelected) {
        isLanguageSelected = languageSelected;
    }


}
