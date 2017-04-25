package com.itservz.bookex.android.backend;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.itservz.bookex.android.DrawerActivity;
import com.itservz.bookex.android.SellActivity;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.BookCategory;
import com.itservz.bookex.android.util.CategoryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju on 12/18/2016.
 */

public class FirebaseCategoryService {
    private static final String TAG = "FirebaseCategoryService";
    private final DatabaseReference catsRef;
    private Activity activity;

    public interface CategoryListener{
        public void onCategoryAdded(BookCategory bookCategory);
    }

    public FirebaseCategoryService(Activity activity){
        this.activity = activity;
        catsRef = FirebaseService.getInstance().getDatabase().getReference(DBRefs.categories.name());
        catsRef.keepSynced(true);
    }

    public List<BookCategory> getCategories(){
        final List<BookCategory> categories = new ArrayList<>();
        catsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                BookCategory bookCat = dataSnapshot.getValue(BookCategory.class);
                if(activity != null){
                    if(activity instanceof DrawerActivity) {
                        ((DrawerActivity) activity).onCategoryAdded(bookCat);
                    }
                    if(activity instanceof SellActivity) {
                        ((SellActivity) activity).onCategoryAdded(bookCat);
                    }
                }
                categories.add(bookCat);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return categories;
    }


}
