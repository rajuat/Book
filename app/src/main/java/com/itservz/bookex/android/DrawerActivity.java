package com.itservz.bookex.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itservz.bookex.android.adapter.FirebaseSearchListAdapter;
import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.backend.FirebaseCategoryService;
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.BookCategory;
import com.itservz.bookex.android.model.SortBy;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.util.CategoryBuilder;
import com.itservz.bookex.android.util.ScreenSizeScaler;
import com.itservz.bookex.android.view.FlowLayout;

import java.util.Arrays;

public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseDatabaseService.SellItemListener, FirebaseCategoryService.CategoryListener {
    private static final String TAG = "DrawerActivity";
    private ViewPager viewPager;
    private TextView usernameTxt;
    private String username;
    PrefManager prefManager = null;
    private FirebaseSearchListAdapter searchAdapter;
    private ListView searchList;
    private FlowLayout categoriesFL;
    private CategoryBuilder categoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getIntent().getBooleanExtra("landingPage", false)) {
            login();//user are ask to login
        }

        prefManager = new PrefManager(this);
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
        /*viewPager = (ViewPager) findViewById(R.id.pagerAds);        viewPager.setAdapter(new TopBannerAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), getResources()));*/

        /*// grid list view
        GridView gridListView = (GridView) findViewById(R.id.sell_list);        List<Book> books = new FirebaseDatabaseService().getSellingItems(this);        SellItemAdapter adapter = new SellItemAdapter(this, books);        gridListView.setAdapter(adapter);*/

        // newly added
        FirebaseDatabaseService.getInstance("").getSellingItems(this, SortBy.recent.name());
        FirebaseDatabaseService.getInstance("").getSellingItems(this, SortBy.price.name());

        //NEW CATEGORY
        categoriesFL = (FlowLayout) findViewById(R.id.flowLayout);
        categoriesFL.setEnabled(true);
        categoriesFL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "touch for expansion");
                int x = (int) event.getX();
                int y = (int) event.getY();
                int width = categoriesFL.getLayoutParams().width;
                int height = categoriesFL.getLayoutParams().height;


                if ((x - width <= 20 && x - width > 0) || (width - x <= 20 && width - x > 0)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d(TAG, "width:" + width + " height:" + height + " x:" + x + " y:" + y);
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
        categoryBuilder = new CategoryBuilder(this);
        new FirebaseCategoryService(this).getCategories();

        // show all less category
        /*final boolean[] expand = {true};
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
        });*/

        //click newly added
        TextView textViewNewlyAdded = (TextView) findViewById(R.id.textViewNewlyAdded);
        textViewNewlyAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerActivity.this, BookListActivity.class);
                intent.putExtra("sortBy", SortBy.recent.name());
                startActivity(intent);
            }
        });


        //click nearby
        TextView textViewNearby = (TextView) findViewById(R.id.textViewNearby);
        textViewNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerActivity.this, BookListActivity.class);
                intent.putExtra("sortBy", SortBy.price.name());
                startActivity(intent);
            }
        });

        //user
        setLoginInfo();
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    void displayAddressOutput() {

    }

    @Override
    void updateUIWidgets() {

    }

    @Override
    public void onCategoryAdded(BookCategory bookCategory) {
        categoryBuilder.addCategories(categoriesFL, bookCategory.longText);
    }

    private void setLoginInfo() {
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            ((TextView) headerView.findViewById(R.id.loginNameTxt)).setText(currentUser.getDisplayName());
            ((TextView) headerView.findViewById(R.id.loginEmailTxt)).setText(currentUser.getEmail());
        } else {
            ((TextView) headerView.findViewById(R.id.loginNameTxt)).setText("Guest");
            ((TextView) headerView.findViewById(R.id.loginEmailTxt)).setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public void onSellItemAdded(final Book book, final String sortBy) {
        //newly added
        LinearLayout containerNewlyAdded = (LinearLayout) findViewById(R.id.containerNewlyAdded);
        LinearLayout containerNearby = (LinearLayout) findViewById(R.id.containerNearby);
        int dpAsPixels = new ScreenSizeScaler(getResources()).getdpAspixel(8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpAsPixels, dpAsPixels, 0, dpAsPixels);

        if (SortBy.recent.name().equals(sortBy)) {
            View view = new SellItemAdapter(this, null).createBookItem(null, book);
            view.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            containerNewlyAdded.addView(view, layoutParams);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book);
                    context.startActivity(intent);
                }
            });
        } else if (SortBy.price.name().equals(sortBy)) {
            View priceView = new SellItemAdapter(this, null).createBookItem(null, book);
            priceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, book);
                    context.startActivity(intent);
                }
            });
            priceView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
            containerNearby.addView(priceView, layoutParams);
        }

        prefManager.setLastFetch(book.getUuid());
        //Toast.makeText(this, "book added", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), 1);
            return true;
        } else*/
        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.loginBtn) {
            if (isLogin()) {
                AuthUI.getInstance().signOut(this);
                View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                ((TextView) headerView.findViewById(R.id.loginNameTxt)).setText("Guest");
                ((TextView) headerView.findViewById(R.id.loginEmailTxt)).setText("");
                Snackbar.make(findViewById(R.id.drawer_layout), "Signout", Snackbar.LENGTH_LONG).show();
                item.setTitle("Login");
            } else {
                login();
                item.setTitle("Logout");
            }
        } else if(id == R.id.sell_book){
            Intent sellIntent = new Intent(this, SellActivity.class);
            startActivity(sellIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                Snackbar.make(findViewById(R.id.drawer_layout), "Signin", Snackbar.LENGTH_LONG).show();
                setLoginInfo();
                /*Intent in = IdpResponse.getIntent(response);
                in.setClass(context, SignedInActivity.class);
                startActivity(SignedInActivity.createIntent(this, response));
                finish();*/
                return;
            } else {
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(findViewById(R.id.drawer_layout), errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }


}
