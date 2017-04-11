package com.itservz.bookex.android.backend;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by raju.athokpam on 11-04-2017.
 */

public class CallService {

    public static Intent getCallIntent(String phoneNumber) {
        // Note that this ACTION_CALL requires permission
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        return callIntent;

    }
}
