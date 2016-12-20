package com.itservz.bookex.android.service;

import android.util.Log;
import android.widget.Toast;

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
//https://www.googleapis.com/books/v1/volumes?q=isbn:8184750110&fields=kind,totalItems,items(volumeInfo/title,volumeInfo/industryIdentifiers)
//http://joerichard.net/android/android-google-books-api-example/
public class GoogleBooksAPIService {

    public void getBookByISBN(final BookListActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder) {
        if (holder.mItem.ISBN.length() > 0) {
            final String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + holder.mItem.ISBN + "&fields=kind,totalItems,items(volumeInfo/title,volumeInfo/description,volumeInfo/industryIdentifiers,saleInfo/listPrice)";

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                /**
                 * Fired when a request returns successfully, override to handle in your own code
                 *
                 * @param statusCode   the status code of the response
                 * @param headers      return headers, if any
                 * @param responseBody the body of the HTTP response from the server
                 */
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String json = new String(responseBody);
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("items");

                        //for (int i = 0; i < array.length(); i++) {

                        JSONObject item = array.getJSONObject(0);

                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                        if(holder.mItem.title == null || holder.mItem.title.length() == 0) holder.mItem.title = volumeInfo.getString("title");
                        if(holder.mItem.description == null || holder.mItem.description.length() == 0) holder.mItem.description = volumeInfo.getString("description");

                        JSONObject saleInfo = item.getJSONObject("saleInfo");
                        JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                        if(holder.mItem.mrp == 0) holder.mItem.mrp = listPrice.getInt("amount");
                        Log.d("Url is book:", url.toString());
                        //}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                /**
                 * Fired when a request fails to complete, override to handle in your own code
                 *
                 * @param statusCode   return HTTP status code
                 * @param headers      return headers, if any
                 * @param responseBody the response body, if any
                 * @param error        the underlying cause of the failure
                 */
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("GoogleBooksAPIService", "Book not found: " + error.getMessage());
                }
            });
        }

    }
}
