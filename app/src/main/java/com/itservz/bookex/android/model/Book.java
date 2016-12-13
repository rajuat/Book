package com.itservz.bookex.android.model;

/**
 * Created by Raju on 12/7/2016.
 */

public class Book {
    public String uuid = null;
    public String ISBN = null;
    public String title = null;
    public String edition = null;
    public String condition = null;
    public boolean missingPage = false;
    public int yourPrice = 0;
    public int msp = 0;
    public int mrp = 0;
    public String description;
    public String image;

    public Book() {
    }

    public Book(String ISBN, String title) {
        this.ISBN = ISBN;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid='" + uuid + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", edition='" + edition + '\'' +
                ", condition='" + condition + '\'' +
                ", missingPage=" + missingPage +
                ", yourPrice=" + yourPrice +
                ", msp=" + msp +
                ", mrp=" + mrp +
                ", description='" + description + '\'' +
                '}';
    }
}
