package com.itservz.bookex.android.model;

import java.io.Serializable;

/**
 * Created by Raju on 12/18/2016.
 * Each item can have list of categories
 */

public class BookCategory implements Serializable {
    public String longText;
    public String shortText;

    public BookCategory() {
        //for firebase
    }

    public BookCategory(String longText, String shortText) {
        this.longText = longText;
        this.shortText = shortText;
    }

    @Override
    public String toString() {
        return "BookCategory{" +
                "longText='" + longText + '\'' +
                ", shortText='" + shortText + '\'' +
                '}';
    }
}
