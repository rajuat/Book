package com.itservz.bookex.android.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Raju on 12/18/2016.
 * Each item can have list of categories
 */

public class BookCategory implements Serializable {
    public String longText = "";

    public BookCategory() {
        //for firebase
    }

    public BookCategory(String longText) {
        this.longText = longText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookCategory that = (BookCategory) o;

        return longText.equals(that.longText);

    }

    @Override
    public int hashCode() {
        return longText.hashCode();
    }

    @Override
    public String toString() {
        return "BookCategory{" +
                "longText='" + longText + '\'' +
                '}';
    }
}
