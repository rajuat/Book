package com.itservz.bookex.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.service.FirebaseStorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju.athokpam on 13-12-2016.
 */

public class SellItemAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books = new ArrayList<>();

    public SellItemAdapter(Context c, List<Book> books) {
        context = c;
        this.books = books;
    }

    public int getCount() {
        return books.size();
    }

    public Object getItem(int position) {
        return books.get(position);
    }

    public long getItemId(int position) {
        return books.get(position).mrp;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = books.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.sell_item, null);
        }

        final ImageView imgIcon = (ImageView) convertView.findViewById(R.id.sell_book_img);
        TextView isbn = (TextView) convertView.findViewById(R.id.sell_book_isbn);
        TextView title = (TextView) convertView.findViewById(R.id.sell_book_title);

        //image
        FirebaseStorageService.INSTANCE.getImage("books/"+book.uuid).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imgIcon.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
        /*if(book.image != null && book.image.length() > 0) {
            byte[] imageDecoded = Base64.decode(book.image, Base64.DEFAULT);
            imgIcon.setImageBitmap(BitmapFactory.decodeByteArray(imageDecoded, 0, imageDecoded.length));
        }*/
        isbn.setText(book.ISBN);
        title.setText(book.title);

        return convertView;
    }

}