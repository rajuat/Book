package com.itservz.bookex.android.model;

import com.itservz.bookex.android.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

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
    public byte[] image;
    public List<BookCategory> categories = new CategoryService().getCategories();

    public Book() {
    }

    public Book(String ISBN, String title) {
        this.ISBN = ISBN;
        this.title = title;
    }

    public String getCategoriesAsString() {
        StringBuilder sb = new StringBuilder();
        for (BookCategory category : categories) {
            sb.append(category.longText);
            sb.append(" / ");
        }
        sb.setLength(sb.length() - 3);
        return sb.toString();
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
