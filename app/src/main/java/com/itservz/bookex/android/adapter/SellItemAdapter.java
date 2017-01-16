package com.itservz.bookex.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.backend.FirebaseStorageService;
import com.itservz.bookex.android.util.BitmapHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju.athokpam on 13-12-2016.
 */

public class SellItemAdapter extends BaseAdapter {
    private static final String TAG = "SellItemAdapter";
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
        return createBookItem(convertView, book);
    }

    @NonNull
    public View createBookItem(View convertView, final Book book) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.sell_item, null);
        }

        final ImageView imgIcon = (ImageView) convertView.findViewById(R.id.sell_book_img);
        TextView title = (TextView) convertView.findViewById(R.id.sell_book_title);
        TextView yourPrice = (TextView) convertView.findViewById(R.id.sell_book_your_price);
        TextView mrp = (TextView) convertView.findViewById(R.id.sell_book_mrp);

        //image
        Task<byte[]> image = FirebaseStorageService.getInstance().getImage("books/" + book.uuid);
        image.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG, "img "+bytes.length);
                imgIcon.setImageBitmap(BitmapHelper.decodeSampledBitmapFromBytes(context.getResources(), bytes));
                book.image = bytes;
            }
        });
        image.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Img failed "+e.getMessage());
            }
        });
        /*if(book.image != null && book.image.length() > 0) {
            byte[] imageDecoded = Base64.decode(book.image, Base64.DEFAULT);
            imgIcon.setImageBitmap(BitmapFactory.decodeByteArray(imageDecoded, 0, imageDecoded.length));
        }*/
        yourPrice.append(""+book.yourPrice);
        mrp.setText("₹ "+book.mrp);
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        title.setText(book.title);
        return convertView;
    }

}