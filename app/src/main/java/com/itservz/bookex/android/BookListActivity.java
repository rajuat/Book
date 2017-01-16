package com.itservz.bookex.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itservz.bookex.android.adapter.BookListAdapter;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.backend.GoogleBooksAPIService;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity implements FirebaseDatabaseService.SellItemListener{

    private static final String TAG = "BookListActivity";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.books_alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookListActivity.this, "alert", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.books_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookListActivity.this, "filter", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.books_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookListActivity.this, "sorrt", Toast.LENGTH_SHORT).show();
            }
        });

        /*ActionMenuView toolbar2 = (ActionMenuView) findViewById(R.id.toolbar2);
        Menu menu = toolbar2.getMenu();
        getMenuInflater().inflate(R.menu.menu_books_action, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }*/
        /*Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.inflateMenu(R.menu.menu_books_action);//changed
        toolbar2.setTitle(null);
        //toolbar2 menu items CallBack listener
        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.books_filter){
                    Toast.makeText(BookListActivity.this, "filter", Toast.LENGTH_LONG).show();
                } else if(arg0.getItemId() == R.id.books_alert){
                    Toast.makeText(BookListActivity.this, "alert", Toast.LENGTH_LONG).show();
                } else if(arg0.getItemId() == R.id.books_sort){
                    Toast.makeText(BookListActivity.this, "sort", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    //create the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners...

        return true;
    }

    //create the action on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, DrawerActivity.class));
            return true;
        } else if(id == R.id.books_filter){
            Toast.makeText(BookListActivity.this, "filter", Toast.LENGTH_LONG).show();
        } else if(id == R.id.books_alert){
            Toast.makeText(BookListActivity.this, "alert", Toast.LENGTH_LONG).show();
        } else if(id == R.id.books_sort){
            Toast.makeText(BookListActivity.this, "sort", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        String lastFetch = new PrefManager(this).getLastFetch();
        FirebaseDatabaseService.getInstance(lastFetch).getSellingItems(this);

        ArrayList<Book> books = new ArrayList<>(FirebaseDatabaseService.getInstance(lastFetch).getBooks().values());
        bookListAdapter = new BookListAdapter(this);
        bookListAdapter.addAll(books);
        Log.d(TAG, books.toString());

        recyclerView.setAdapter(bookListAdapter);
    }

    @Override
    public void onSellItemAdded(Book book) {
        Log.d(TAG, book.toString());
        bookListAdapter.add(book);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Book> mValues;

        public SimpleItemRecyclerViewAdapter(List<Book> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Book book = mValues.get(position);
            holder.mItem = book;
            byte[] img = book.image;
            if(img != null)
            holder.mImageView.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            holder.mTitleView.setText(book.title);
            holder.mCategories.setText("physics / class 11 / science");
            //holder.mCategories.setText(book.getCategoriesAsString());
            holder.mYourPriceView.setText("₹ " + book.yourPrice);
            holder.mMRPView.setText("₹ " + book.mrp);
            if(book.mrp == 0){
                new GoogleBooksAPIService().getBookByISBN(holder);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(BookDetailFragment.ARG_ITEM_ID, holder.mItem.uuid);
                        BookDetailFragment fragment = new BookDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BookDetailActivity.class);
                        intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem.uuid);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTitleView;
            public final TextView mCategories;
            public final TextView mYourPriceView;
            public final TextView mMRPView;
            public Book mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.bookImage);
                mTitleView = (TextView) view.findViewById(R.id.book_list_title);
                mCategories = (TextView) view.findViewById(R.id.book_list_cat);
                mYourPriceView = (TextView) view.findViewById(R.id.booklist_yprice);
                mMRPView = (TextView) view.findViewById(R.id.book_list_mrp);
                mMRPView.setPaintFlags(mMRPView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitleView.getText() + "'";
            }
        }
    }
}
