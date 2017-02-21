package com.itservz.bookex.android;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.Query;
import com.itservz.bookex.android.adapter.FirebaseSearchListAdapter;
import com.itservz.bookex.android.backend.DBRefs;
import com.itservz.bookex.android.backend.FirebaseService;
import com.itservz.bookex.android.model.Book;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private FirebaseSearchListAdapter searchAdapter;
    private ListView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //search
        Query query = FirebaseService.getInstance().getDatabase().getReference(DBRefs.sells.name());
        searchAdapter = new FirebaseSearchListAdapter(query, Book.class, R.layout.basic_search, this);
        searchList = (ListView) findViewById(R.id.search_list);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = searchAdapter.getItem(i);
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book.getUuid());
                context.startActivity(intent);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        };
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.search_src_text);
                editText.setText("");

                //Clear query
                searchView.setQuery("", false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                Log.d(TAG, "Search :: " + search);
                searchAdapter.filter(search);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        searchList.setAdapter(searchAdapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
