package com.itservz.bookex.android.service;

import com.itservz.bookex.android.model.BookCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju on 12/18/2016.
 */

public class CategoryService {

    public List<BookCategory> getCategories(){
        List<BookCategory> categories = new ArrayList<>();
        categories.add(new BookCategory("Class 9", "9th"));
        categories.add(new BookCategory("Class 10", "10th"));
        categories.add(new BookCategory("Class 11", "11th"));
        categories.add(new BookCategory("Class 12", "12th"));
        categories.add(new BookCategory("Graduation", "BA"));
        categories.add(new BookCategory("Masters", "MA"));
        categories.add(new BookCategory("Physics", "Phys."));
        categories.add(new BookCategory("Chemistry", "Chem."));
        categories.add(new BookCategory("Biology", "Bio."));
        categories.add(new BookCategory("History", "Hist."));
        categories.add(new BookCategory("Ecomonics", "Eco."));
        categories.add(new BookCategory("Social Science", "S.Sc."));
        return categories;
    }
}
