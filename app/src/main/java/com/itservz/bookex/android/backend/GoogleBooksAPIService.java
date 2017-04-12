package com.itservz.bookex.android.backend;

import android.util.Log;

import com.itservz.bookex.android.BookListActivity;
import com.itservz.bookex.android.model.Book;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by raju.athokpam on 19-12-2016.
 */
//http://joerichard.net/android/android-google-books-api-example/
//https://www.googleapis.com/books/v1/volumes?q=isbn:8184750110&fields=kind,totalItems,items(volumeInfo/title,volumeInfo/authors,volumeInfo/description,volumeInfo/industryIdentifiers,volumeInfo/imageLinks/smallThumbnail,saleInfo/retailPrice)
public class GoogleBooksAPIService {

    private final static String TAG = "GoogleBooksAPIService";

    public void getBook(String isbn, final Book book){
        final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&fields=kind,totalItems,items(volumeInfo/title,volumeInfo/authors,volumeInfo/description,volumeInfo/industryIdentifiers,volumeInfo/imageLinks/smallThumbnail,saleInfo/retailPrice)";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray array = object.getJSONArray("items");
                    JSONObject item = array.getJSONObject(0);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                    book.setTitle(volumeInfo.getString("title"));
                    book.setAuthor(volumeInfo.getJSONArray("authors").getString(0));
                    book.setImageUrl(volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));
                    book.setDescription(volumeInfo.getString("description"));

                    JSONObject saleInfo = item.getJSONObject("saleInfo");
                    book.setMrp(saleInfo.getJSONObject("listPrice").getInt("amount"));
                    Log.d(TAG, "onSuccess: " +book.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("GoogleBooksAPIService", "Book not found: " + error.getMessage());
            }
        });
        //return book;
    }

    /*public void getBookByISBN(final BookListActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder) {
        if (holder.mItem.getISBN().length() > 0) {
            final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + holder.mItem.getISBN() + "&fields=kind,totalItems,items(volumeInfo/title,volumeInfo/description,volumeInfo/industryIdentifiers,saleInfo/listPrice)";

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                *//**
                 * Fired when a request returns successfully, override to handle in your own code
                 *
                 * @param statusCode   the status code of the response
                 * @param headers      return headers, if any
                 * @param responseBody the body of the HTTP response from the server
                 *//*
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String json = new String(responseBody);
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("items");

                        //for (int i = 0; i < array.length(); i++) {

                        JSONObject item = array.getJSONObject(0);

                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                        if(holder.mItem.getTitle() == null || holder.mItem.getTitle().length() == 0) holder.mItem.setTitle(volumeInfo.getString("title"));
                        if(holder.mItem.getDescription() == null || holder.mItem.getDescription().length() == 0) holder.mItem.setDescription(volumeInfo.getString("description"));

                        JSONObject saleInfo = item.getJSONObject("saleInfo");
                        JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                        if(holder.mItem.getMrp() == 0) holder.mItem.setMrp(listPrice.getInt("amount"));
                        Log.d("Url is book:", url.toString());
                        //}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                *//**
                 * Fired when a request fails to complete, override to handle in your own code
                 *
                 * @param statusCode   return HTTP status code
                 * @param headers      return headers, if any
                 * @param responseBody the response body, if any
                 * @param error        the underlying cause of the failure
                 *//*
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("GoogleBooksAPIService", "Book not found: " + error.getMessage());
                }
            });
        }

    }*/
}
