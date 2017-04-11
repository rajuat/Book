package com.itservz.bookex.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.itservz.bookex.android.backend.MessagingService;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.Location;
import com.itservz.bookex.android.util.BitmapHelper;

/**
 * An activity representing a single Book detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 */
public class BookDetailActivity extends BaseActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        book = (Book) getIntent().getSerializableExtra(BookDetailFragment.ARG_ITEM_ID);

        Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromBytes(getResources(), book.getImage());
        ((ImageView)findViewById(R.id.backdrop)).setImageDrawable(new BitmapDrawable(getResources(), bitmap));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(BookDetailActivity.this, ChatActivity.class);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, getIntent().getSerializableExtra(BookDetailFragment.ARG_ITEM_ID));
                startActivity(intent);*/
                startActivity(MessagingService.getMessagingIntent());
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putSerializable(BookDetailFragment.ARG_ITEM_ID, book);
            arguments.putParcelable(Location.LOCATION_KEY, mLastLocation);
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    void displayAddressOutput() {

    }

    @Override
    void updateUIWidgets() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
