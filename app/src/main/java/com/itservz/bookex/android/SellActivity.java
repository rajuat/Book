package com.itservz.bookex.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.service.FirebaseDatabaseService;
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
    PrefManager prefManager = null;
    private final static int  MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        prefManager = new PrefManager(this);

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
        isbn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                prefManager.setISBN(((TextInputEditText)v).getText().toString());
                //book.ISBN = ((TextInputEditText)v).getText().toString();
            }
        });

        TextInputEditText title = (TextInputEditText) findViewById(R.id.book_title);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                prefManager.setTitle(((TextInputEditText)v).getText().toString());
                //book.title = ((TextInputEditText)v).getText().toString();
            }
        });

        ((Button) findViewById(R.id.sell_button)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //book.uuid = UUID.randomUUID().toString();
        bookImage.setDrawingCacheEnabled(true);
        bookImage.buildDrawingCache();
        Bitmap bitmap = bookImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        //String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        //book.image = imageEncoded;
        book.ISBN = prefManager.getISBN();
        book.title = prefManager.getTitle();
        String uId = FirebaseDatabaseService.INSTANCE.addSellingItem(book);
        FirebaseStorageService.getInstance().setImage(uId, baos.toByteArray(), this);
        Snackbar.make(v, "Ad posted", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        //Toast.makeText(this, "Ad posted", Toast.LENGTH_LONG).show();
        finish();
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
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bitmap bitmap= BitmapFactory.decodeStream(image_stream);
                    bookImage.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }
}