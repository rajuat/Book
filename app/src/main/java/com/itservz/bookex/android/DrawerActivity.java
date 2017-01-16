package com.itservz.bookex.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.adapter.TopBannerAdapter;
import com.itservz.bookex.android.backend.CategoryService;
import com.itservz.bookex.android.backend.FirebaseDatabaseService;
import com.itservz.bookex.android.backend.FirebaseService;
import com.itservz.bookex.android.backend.LoginDialog;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.model.BookCategory;
import com.itservz.bookex.android.preference.PrefManager;
import com.itservz.bookex.android.util.ScreenSizeScaler;
import com.itservz.bookex.android.view.FlowLayout;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseDatabaseService.SellItemListener {

    private ViewPager viewPager;
    private TextView usernameTxt;
    private String username;
    PrefManager prefManager = null;

    private void setUsername(String username) {
        Log.d("DrawerActivity", "setUsername("+String.valueOf(username)+")");
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
        FirebaseDatabaseService.getInstance("").getSellingItems(this);

        //NEW CATEGORY
        final FlowLayout categoriesFL = (FlowLayout) findViewById(R.id.flowLayout);

        for (BookCategory cat : new CategoryService().getCategories()) {
            addCategories(categoriesFL, cat.longText);
        }

        final boolean[] expand = {false};
        //click
        final TextView textViewCat = (TextView) findViewById(R.id.text_view_category);
        textViewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expand[0]){
                    expandOrCollapseCategories(categoriesFL, "collapse");
                    textViewCat.setText("Show all categories");
                    expand[0] = false;
                } else {
                    expandOrCollapseCategories(categoriesFL, "expand");
                    textViewCat.setText("Show less categories");
                    expand[0] = true;
                }
                //startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
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

    private void addCategories(FlowLayout flowLayout, String text) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layout);
        ScreenSizeScaler screenSizeScaler = new ScreenSizeScaler(getResources());
        int px = screenSizeScaler.getdpAspixel(8);
        linearLayout.setPadding(px, px, px, px);

        final TextView textView = new TextView(this);
        textView.setLayoutParams(layout);
        textView.setBackground(getResources().getDrawable(R.drawable.rounded_border));
        textView.setPadding(px, px, px, px);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerActivity.this, BookListActivity.class));
            }
        });

        linearLayout.addView(textView);
        flowLayout.addView(linearLayout);
    }

    public void expandOrCollapseCategories(final View v, String exp_or_colpse) {
        TranslateAnimation anim = null;
        if ("expand".equals(exp_or_colpse)) {
            anim = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        } else {
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -(v.getHeight() - 96f) );
            Animation.AnimationListener collapselistener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };
            anim.setAnimationListener(collapselistener);
        }
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
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
