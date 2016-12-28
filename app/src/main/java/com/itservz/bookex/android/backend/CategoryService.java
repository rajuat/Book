package com.itservz.bookex.android.backend;

import com.itservz.bookex.android.model.BookCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju on 12/18/2016.
 */

public class CategoryService {

    public List<BookCategory> getCategories(){
        List<BookCategory> categories = new ArrayList<>();
        categories.add(new BookCategory("Class 9", "09 th"));
        categories.add(new BookCategory("Class 10", "10 th"));
        categories.add(new BookCategory("Class 11", "11 th"));
        categories.add(new BookCategory("Class 12", "12 th"));
        categories.add(new BookCategory("Graduation", "B. Sc."));
        categories.add(new BookCategory("Masters", "M. Sc."));
        categories.add(new BookCategory("Physics",   "Phys."));
        categories.add(new BookCategory("Chemistry", "Chem"));
        categories.add(new BookCategory("Biology", "Bio... "));
        categories.add(new BookCategory("History", "Hist.."));
        categories.add(new BookCategory("Ecomonics", "Eco..."));
        categories.add(new BookCategory("Social Science", "S.Sc."));
        return categories;
    }
}
