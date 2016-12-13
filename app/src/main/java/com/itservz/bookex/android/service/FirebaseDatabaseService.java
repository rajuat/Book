package com.itservz.bookex.android.service;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itservz.bookex.android.DrawerActivity;
import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju on 12/6/2016.
 */

public class FirebaseDatabaseService {

    public List<Book> getSellingItems(final DrawerActivity drawerActivity){
        final List<Book> books =  new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sellsReference = database.getReference(DBRefs.sells.name());
        sellsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book book = dataSnapshot.getValue(Book.class);
                books.add(book);
                Toast.makeText(drawerActivity, "book added", Toast.LENGTH_SHORT).show();
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

        return books;
    }

    @NonNull
    public DatabaseReference getDatabaseReference(final ArrayAdapter<String> adapter) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(DBRefs.todoItems.name());
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
