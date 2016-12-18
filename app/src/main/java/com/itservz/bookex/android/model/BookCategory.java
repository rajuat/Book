package com.itservz.bookex.android.model;

/**
 * Created by Raju on 12/18/2016.
 * Each item can have list of categories
 */

public class BookCategory {
    public String longText;
    public String shortText;

    public BookCategory(String longText, String shortText) {
        this.longText = longText;
        this.shortText = shortText;
    }
}
