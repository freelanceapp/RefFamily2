
package com.apps.reffamily.models;

import java.io.Serializable;

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
