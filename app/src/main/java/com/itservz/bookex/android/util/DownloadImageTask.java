package com.itservz.bookex.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Raju on 2/5/2017.
 */


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Bitmap imageFromUrl = null;
    private ImageView view;
    public DownloadImageTask(Bitmap imageFromUrl) {
        this.imageFromUrl = imageFromUrl;
    }

    public DownloadImageTask(ImageView view) {
        this.view = view;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        view.setImageBitmap(result);
        imageFromUrl = result;
    }
}