package com.itservz.bookex.android.backend;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by raju.athokpam on 11-04-2017.
 */

public class SMSService {

    public static Intent getCallIntent(String phoneNumber) {
        // Note that this ACTION_CALL requires permission
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        return callIntent;

    }

    public static Intent getSMSIntent(String phoneNumber) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // Set the message to be sent
        smsIntent.putExtra("sms_body", "I like to buy your posted book.");
        return smsIntent;
    }
}
