package com.itservz.bookex.android.backend;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by raju.athokpam on 11-04-2017.
 */

public class MessagingService {

    public static Intent getSMSIntent(String phoneNumber) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // Set the message to be sent
        smsIntent.putExtra("sms_body", "I like to buy your posted book.");
        return smsIntent;
    }

    public static Intent getMessagingIntent() {
        String shareBody = "I like to buy your posted book.";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bookex");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        return Intent.createChooser(sharingIntent, "Contact seller using ");
    }

}
