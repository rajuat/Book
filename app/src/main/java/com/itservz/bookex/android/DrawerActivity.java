package com.itservz.bookex.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.GridView;
import android.widget.TextView;

import com.itservz.bookex.android.adapter.SellItemAdapter;
import com.itservz.bookex.android.adapter.TopBannerAdapter;
import com.itservz.bookex.android.model.Book;
import com.itservz.bookex.android.service.FirebaseDatabaseService;
import com.itservz.bookex.android.service.FirebaseService;
import com.itservz.bookex.android.service.LoginDialog;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private View loginBtn;
    private View logoutBtn;
    private TextView usernameTxt;
    private String username;

    private void setUsername(String username) {
        Log.d("DrawerActivity", "setUsername("+String.valueOf(username)+")");
        if (username == null) {
            username = "Android";
        }
        boolean isLoggedIn = !username.equals("Android");
        this.username = username;
        this.usernameTxt.setText(username);
        this.logoutBtn.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        this.loginBtn .setVisibility(isLoggedIn ? View.GONE    : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //login
        loginBtn = findViewById(R.id.loginBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        usernameTxt = (TextView) findViewById(R.id.usernameTxt);
        setUsername("Android");
        // Show a popup when the user asks to sign in
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginDialog.showLoginPrompt(DrawerActivity.this, FirebaseService.getInstance().app);
            }
        });
        // Allow the user to sign out
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseService.getInstance().auth.signOut();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sell);
        final Intent sellIntent = new Intent(this, SellActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.equals("Android"))
                LoginDialog.showLoginPrompt(DrawerActivity.this, FirebaseService.getInstance().app);
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
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TopBannerAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), getResources()));

        // grid list view
        GridView gridListView = (GridView) findViewById(R.id.sell_list);
        List<Book> books = new FirebaseDatabaseService().getSellingItems(this);
        SellItemAdapter adapter = new SellItemAdapter(this, books);
        gridListView.setAdapter(adapter);

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
