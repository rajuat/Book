package com.itservz.bookex.android.adapter;

/**
 * Created by Raju on 2/7/2017.
 */

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.itservz.bookex.android.R;
import com.itservz.bookex.android.model.Book;

import java.util.ArrayList;
import java.util.List;

public class FirebaseSearchListAdapter extends FirebaseListAdapter {

    private final static String TAG = "FBSearchListAdapter";
    private Query mRef;
    private Class<Book> mModelClass;
    private int mLayout;
    private LayoutInflater mInflater;
    private List<Book> books = new ArrayList<>();
    private List<Book> booksCopy = new ArrayList<>();
    private List<String> mKeys;
    private ChildEventListener mListener;


    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public FirebaseSearchListAdapter(Query mRef, Class<Book> mModelClass, int mLayout, Activity activity) {
        super(activity, mModelClass, mLayout, mRef);
        this.mRef = mRef;
        this.mModelClass = mModelClass;
        this.mLayout = mLayout;
        mInflater = activity.getLayoutInflater();
        books = new ArrayList<Book>();
        mKeys = new ArrayList<String>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Book model = dataSnapshot.getValue(FirebaseSearchListAdapter.this.mModelClass);
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    books.add(0, model);
                    booksCopy.add(0, model);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == books.size()) {
                        books.add(model);
                        booksCopy.add(model);
                        mKeys.add(key);
                    } else {
                        books.add(nextIndex, model);
                        booksCopy.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the books changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                Book newModel = dataSnapshot.getValue(FirebaseSearchListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                books.set(index, newModel);
                booksCopy.set(index, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                books.remove(index);
                booksCopy.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                Book newModel = dataSnapshot.getValue(FirebaseSearchListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                books.remove(index);
                booksCopy.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    books.add(0, newModel);
                    booksCopy.add(0, newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == books.size()) {
                        books.add(newModel);
                        booksCopy.add(newModel);
                        mKeys.add(key);
                    } else {
                        books.add(nextIndex, newModel);
                        booksCopy.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the books
        mRef.removeEventListener(mListener);
        books.clear();
        booksCopy.clear();
        mKeys.clear();
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(mLayout, viewGroup, false);
        }

        Book model = books.get(i);
        // Call out to subclass to marshall this model into the provided view
        populateView(view, model, i);
        return view;
    }

    @Override
    protected void populateView(View v, Object model, int position) {
        Log.d(TAG, "populateView: " + v + " : " + model + " : " + position);
        TextView textView = (TextView) v.findViewById(R.id.search_title);
        textView.setText(((Book) model).getTitle() );
    }

    public void filter(String text) {
        books.clear();
        if (text.isEmpty()) {
            //books.addAll(booksCopy);
            return;
        } else {
            text = text.toLowerCase();
            for (Book book : booksCopy) {
                if (book.getTitle().toLowerCase().contains(text)) {
                    books.add(book);
                }
            }
        }
        notifyDataSetChanged();
    }
}