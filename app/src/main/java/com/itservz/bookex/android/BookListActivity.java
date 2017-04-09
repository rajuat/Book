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
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.backend.GoogleBooksAPIService;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.util.BundleKeys;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements FirebaseDatabaseService.SellItemListener{

    private static final String TAG = "BookListActivity";
    private boolean mTwoPane;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        String title = getIntent().getStringExtra(BundleKeys.CATEGORY.name());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((title == null || title.trim().length() < 1) ? getTitle() : title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.book_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, DrawerActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        String lastFetch = new PrefManager(this).getLastFetch();
        Intent intent = getIntent();
        String sortBy = intent.getStringExtra("sortBy");
        Log.d(TAG, "setupRecyclerView: " + sortBy);
        FirebaseDatabaseService.getInstance(lastFetch).getSellingItems(this, sortBy);

        //ArrayList<Book> books = new ArrayList<>(FirebaseDatabaseService.getInstance(lastFetch).getBooks().values());
        bookListAdapter = new BookListAdapter(this, sortBy);
        //bookListAdapter.addAll(books);
        //Log.d(TAG, books.toString());

        recyclerView.setAdapter(bookListAdapter);
    }

    @Override
    public void onSellItemAdded(Book book, String sortBy) {
        Log.d(TAG, book.toString());
        bookListAdapter.add(book);
    }

   /* public class SimpleItemRecyclerViewAdapter
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
            byte[] img = book.getImage();
            if(img != null)
            holder.mImageView.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            holder.mTitleView.setText(book.getTitle());
            holder.mCategories.setText("physics / class 11 / science");
            //holder.mCategories.setText(book.getCategoriesAsString());
            holder.mYourPriceView.setText("₹ " + book.getYourPrice());
            holder.mMRPView.setText("₹ " + book.getMrp());
            if(book.getMrp() == 0){
                new GoogleBooksAPIService().getBookByISBN(holder);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(BookDetailFragment.ARG_ITEM_ID, holder.mItem.getUuid());
                        BookDetailFragment fragment = new BookDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BookDetailActivity.class);
                        intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem.getUuid());

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
    }*/
}

/*
        sorting and all

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
        */
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
