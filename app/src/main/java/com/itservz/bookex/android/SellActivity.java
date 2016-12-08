package com.itservz.bookex.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.Transaction;
import com.itservz.bookex.android.service.FirebaseStorageService;
import com.itservz.bookex.android.service.ImagePickerService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SellActivity extends AppCompatActivity implements  View.OnClickListener{

    private Book book;
    int PICK_IMAGE = 1;
    private ImageView bookImage;
    private InputStream image_stream;
    private final static int  MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        book = new Book();

        bookImage = (ImageView) findViewById(R.id.book_image);
        bookImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new ImagePickerService().getImageFromPhoneIntent();
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        TextInputEditText isbn = (TextInputEditText) findViewById(R.id.isbn);
        book.ISBN = isbn.getText().toString();

        TextInputEditText title = (TextInputEditText) findViewById(R.id.book_title);
        book.title = title.getText().toString();

        ((Button) findViewById(R.id.sell_button)).setOnClickListener(this);

    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            image_stream = null;
            try {
                image_stream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap= BitmapFactory.decodeStream(image_stream);
                bookImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bitmap bitmap= BitmapFactory.decodeStream(image_stream);
                    bookImage.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_LONG);
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    public void onClick(View v) {
        book.uuid = UUID.randomUUID().toString();
        Log.d("Selling", book.toString());
        update();

        // Get the data from an ImageView as bytes
        bookImage.setDrawingCacheEnabled(true);
        bookImage.buildDrawingCache();
        Bitmap bitmap = bookImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorageService.INSTANCE.setImage(book.uuid, data);

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
