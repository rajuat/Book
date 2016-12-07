package com.itservz.bookex.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.Transaction;

import java.util.UUID;

public class SellActivity extends AppCompatActivity implements  View.OnClickListener{

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        book = new Book();

        TextInputEditText isbn = (TextInputEditText) findViewById(R.id.isbn);
        book.ISBN = isbn.getText().toString();

        TextInputEditText title = (TextInputEditText) findViewById(R.id.book_title);
        book.title = title.getText().toString();

        ((Button) findViewById(R.id.sell_button)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        book.uuid = UUID.randomUUID().toString();
        Log.d("Selling", book.toString());
        update();
    }

    @NonNull
    public void update() {
        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get a reference to the todoItems child items it the database
        final DatabaseReference myRef = database.getReference("sells");
        // Assign a listener to detect changes to the child items of the database reference.
        myRef.addChildEventListener(new ChildEventListener() {
            // This function is called once for each child that exists when the listener is added. Then it is called
            // each time a new child is added.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Book value = dataSnapshot.getValue(Book.class);
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

            }

            // The following functions are also required in ChildEventListener implementations.
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });
        // Create a new child with a auto-generated ID.
        DatabaseReference childRef = myRef.push();
        // Set the child's data to the value passed in from the text box.
        childRef.setValue(book);
    }
}
