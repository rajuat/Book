package com.itservz.bookex.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.adapter.TopBannerAdapter;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.BookCategory;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.backend.CategoryService;
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.backend.FirebaseService;
import com.itservz.bookex.android.backend.LoginDialog;
import com.itservz.bookex.android.util.ScreenSizeScaler;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseDatabaseService.SellItemListener {

    private ViewPager viewPager;
    private TextView usernameTxt;
    private String username;
    PrefManager prefManager = null;

    private void setUsername(String username) {
        Log.d("DrawerActivity", "setUsername(" + String.valueOf(username) + ")");
        if (username == null) {
            username = "James Bond";
        }
        boolean isLoggedIn = !username.equals("James Bond");
        this.username = username;
        this.usernameTxt.setText(username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        setUsername("James Bond");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sell);
        final Intent sellIntent = new Intent(this, SellActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(sellIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // top banner
        viewPager = (ViewPager) findViewById(R.id.pagerAds);
        viewPager.setAdapter(new TopBannerAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), getResources()));

        /*// grid list view
        GridView gridListView = (GridView) findViewById(R.id.sell_list);
        List<Book> books = new FirebaseDatabaseService().getSellingItems(this);
        SellItemAdapter adapter = new SellItemAdapter(this, books);
        gridListView.setAdapter(adapter);*/

        // newly added
        FirebaseDatabaseService.getInstance(prefManager.getLastFetch()).getSellingItems(this);

        //category
        int dpAsPixels = new ScreenSizeScaler(getResources()).getdpAspixel(8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        ViewGroup containerCategory = (ViewGroup) findViewById(R.id.containerCategory);

        int corderRadius = new ScreenSizeScaler(getResources()).getdpAspixel(8);
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(corderRadius);
        int stroke = new ScreenSizeScaler(getResources()).getdpAspixel(1);
        gd.setStroke(stroke, Color.BLUE);

        for (BookCategory cat : new CategoryService().getCategories()) {
            TextView v = new TextView(this, null);
            v.setText(cat.shortText);
            v.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackground(gd);
            } else {
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                v.setTextColor(getResources().getColor(R.color.colorTextIcon));

            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
                }
            });
            containerCategory.addView(v, params);
        }

        //click
        TextView textViewCat = (TextView) findViewById(R.id.text_view_category);
        textViewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
            }
        });

        //click
        TextView textViewNewlyAdded = (TextView) findViewById(R.id.textViewNewlyAdded);
        textViewNewlyAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
            }
        });

        //click
        TextView textViewNearby = (TextView) findViewById(R.id.textViewNearby);
        textViewNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
            }
        });

    }

    @Override
    public void onSellItemAdded(final Book book) {
        LinearLayout containerNewlyAdded = (LinearLayout) findViewById(R.id.containerNewlyAdded);
        LinearLayout containerNearby = (LinearLayout) findViewById(R.id.containerNearby);
        int dpAsPixels = new ScreenSizeScaler(getResources()).getdpAspixel(8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpAsPixels, dpAsPixels, 0, dpAsPixels);
        View view = new SellItemAdapter(this, null).createBookItem(null, book);
        view.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        containerNewlyAdded.addView(view, layoutParams);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book.uuid);
                context.startActivity(intent);
            }
        });

        View nearbyView = new SellItemAdapter(this, null).createBookItem(null, book);
        nearbyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book.uuid);
                context.startActivity(intent);
            }
        });
        nearbyView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        containerNearby.addView(nearbyView, layoutParams);

        prefManager.setLastFetch(book.uuid);
        Toast.makeText(this, "book added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        } else if (id == R.id.loginBtn) {
            LoginDialog.showLoginPrompt(DrawerActivity.this, FirebaseService.getInstance().getApp());
        } else if (id == R.id.logoutBtn) {
            FirebaseService.getInstance().getAuth().signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
