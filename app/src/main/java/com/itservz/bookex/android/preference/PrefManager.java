package com.itservz.bookex.android.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Raju on 12/6/2016.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "bp";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String ISBN = "isbn";
    private static final String TITLE = "title";
    private static final String LAST_FETCH = "lastFetch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setISBN(String isbn){
        editor.putString(ISBN, isbn);
        editor.commit();
    }

    public String getISBN(){
        return pref.getString(ISBN, "");
    }

    public void setTitle(String title){
        editor.putString(TITLE, title);
        editor.commit();
    }

    public String getTitle(){
        return pref.getString(TITLE, "");
    }

    public void setLastFetch(String lastFetch){
        editor.putString(LAST_FETCH, lastFetch);
        editor.commit();
    }

    public String getLastFetch(){
        return pref.getString(LAST_FETCH, "");
    }

}