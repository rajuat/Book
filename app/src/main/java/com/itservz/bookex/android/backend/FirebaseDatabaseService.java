package com.itservz.bookex.android.backend;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.SortBy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raju on 12/6/2016.
 */

public class FirebaseDatabaseService {
    private static final String TAG = "FirebaseDatabaseService";
    private DatabaseReference sellsReference = null;
    private Query sellsQuery = null;
    private static FirebaseDatabaseService INSTANCE = null;

    private FirebaseDatabaseService(String lastPosted){
        sellsReference = FirebaseService.getInstance().getDatabase().getReference(DBRefs.sells.name());
        sellsReference.keepSynced(true);

    }

    public static FirebaseDatabaseService getInstance(String lastPosted){
        Log.d(TAG, "Last posted " + lastPosted);
        return new FirebaseDatabaseService(lastPosted);
    }

    private Map<String, Book> books =  new HashMap<>();
    public Map<String, Book> getBooks(){
        return books;
    }

    public Collection<Book> getSellingItems(final SellItemListener sellItemListener, final String sortBy){
        //final SellItemListener sellItemListener = null;
        if(SortBy.price.name().equals(sortBy)){
            sellsQuery = sellsReference.orderByChild("yourPrice");
        } else if(SortBy.recent.name().equals(sortBy)) {
            sellsQuery = sellsReference.orderByChild("uploadTime");
        } else {
            sellsQuery = sellsReference.orderByKey();//default
        }
        sellsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book book = dataSnapshot.getValue(Book.class);
                Log.d(TAG, book.getTitle());
                books.put(book.getUuid(), book);
                if(sellItemListener != null){
                    sellItemListener.onSellItemAdded(book, sortBy);
                    Log.d(TAG, book.getUuid());
                }
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });
        return books.values();
    }

    public String addSellingItem(Book book) {
        DatabaseReference childRef = sellsReference.push();
        String uId = childRef.getKey();
        book.setUuid(uId);
        Log.d("Selling", book.toString());
        childRef.setValue(book);
        return uId;
    }

    public interface SellItemListener{
        public void onSellItemAdded(Book book, String sortBy);
    }

    @NonNull
    public DatabaseReference getDatabaseReference(final ArrayAdapter<String> adapter) {
        final DatabaseReference myRef = FirebaseService.getInstance().getDatabase().getReference(DBRefs.todoItems.name());
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = dataSnapshot.getValue(String.class);
                adapter.add(value);
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                adapter.remove(value);
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });
        return myRef;
    }
}
