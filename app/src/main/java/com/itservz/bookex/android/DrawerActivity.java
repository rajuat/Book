package com.itservz.bookex.android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.itservz.bookex.android.adapter.FirebaseSearchListAdapter;
import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.adapter.TopBannerAdapter;
import com.itservz.bookex.android.backend.CategoryService;
import com.itservz.bookex.android.backend.DBRefs;
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.backend.FirebaseService;
import com.itservz.bookex.android.backend.LoginDialog;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.BookCategory;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.util.CategoryBuilder;
import com.itservz.bookex.android.util.ScreenSizeScaler;
import com.itservz.bookex.android.view.FlowLayout;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseDatabaseService.SellItemListener {
    private static final String TAG = "DrawerActivity";
    private ViewPager viewPager;
    private TextView usernameTxt;
    private String username;
    PrefManager prefManager = null;
    private FirebaseSearchListAdapter searchAdapter;
    private ListView searchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        /*usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        setUsername("James Bond");*/

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
        FirebaseDatabaseService.getInstance("").getSellingItems(this);

        //NEW CATEGORY
        final FlowLayout categoriesFL = (FlowLayout) findViewById(R.id.flowLayout);
        categoriesFL.setEnabled(true);
        categoriesFL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "touch for expansion");
                int x=(int)event.getX();
                int y=(int)event.getY();
                int width= categoriesFL.getLayoutParams().width;
                int height = categoriesFL.getLayoutParams().height;


                if((x - width <= 20 && x - width > 0) ||(width - x <= 20 && width - x > 0)){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d(TAG,"width:"+width+" height:"+height+" x:"+x+" y:"+y);
                            categoriesFL.getLayoutParams().width = x;
                            categoriesFL.getLayoutParams().height = y;
                            categoriesFL.requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
                return false;
            }
        });
        final CategoryBuilder categoryBuilder = new CategoryBuilder(this);
        for (BookCategory cat : new CategoryService().getCategories()) {
            categoryBuilder.addCategories(categoriesFL, cat.longText);
        }
        //click
        final boolean[] expand = {true};
        final TextView textViewCat = (TextView) findViewById(R.id.text_view_category);
        textViewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expand[0]){
                    categoryBuilder.expandOrCollapseCategories(categoriesFL, "collapse");
                    textViewCat.setText("Show all categories");
                    expand[0] = false;
                } else {
                    categoryBuilder.expandOrCollapseCategories(categoriesFL, "expand");
                    textViewCat.setText("Show less categories");
                    expand[0] = true;
                }
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
        //search
        Query query = FirebaseService.getInstance().getDatabase().getReference(DBRefs.sells.name());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        searchAdapter = new FirebaseSearchListAdapter(query, Book.class, R.layout.basic_search, this);
        searchList = (ListView) findViewById(R.id.search_list);
    }

    @Override
    public void onSellItemAdded(final Book book) {
        //newly added
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
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book.getUuid());
                context.startActivity(intent);
            }
        });

        View nearbyView = new SellItemAdapter(this, null).createBookItem(null, book);
        nearbyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book.getUuid());
                context.startActivity(intent);
            }
        });
        nearbyView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        containerNearby.addView(nearbyView, layoutParams);

        prefManager.setLastFetch(book.getUuid());
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;  // Return true to collapse action view
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;  // Return true to expand action view
            }
        };
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Assign the listener to that action item
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search close button clicked");
                EditText editText = (EditText) findViewById(R.id.search_src_text);
                //Clear the text from EditText view
                editText.setText("");

                //Clear query
                searchView.setQuery("", false);
               /* searchAdapter = new SearchAdapter(BookListActivity.this, query);
                mListView.setAdapter(searchAdapter);*/
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

    /*private void setUsername(String username) {
        Log.d("DrawerActivity", "setUsername("+String.valueOf(username)+")");
        if (username == null) {
            username = "James Bond";
        }
        boolean isLoggedIn = !username.equals("James Bond");
        this.username = username;
        this.usernameTxt.setText(username);
    }*/
}
