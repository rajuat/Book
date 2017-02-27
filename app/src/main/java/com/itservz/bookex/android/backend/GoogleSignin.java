/*
package com.itservz.bookex.android.backend;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

*/
/**
 * Created by Raju on 2/27/2017.
 *//*


public class GoogleSignin {
    private final Activity context;
    private static final String TAG = "GoogleSignin";

    public GoogleSignin(Activity context) {
        this.context = context;
    }

    public void signin(int RC_SIGN_IN){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "signin: ");
        } else {
            context.startActivityForResult(
                    AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())),
                    RC_SIGN_IN);
        }
    }
}
*/
