package com.itservz.bookex.android.backend;

import android.content.Intent;

/**
 * Created by raju.athokpam on 08-12-2016.
 */

public class ImagePickerService {

    public Intent getImageFromPhoneIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        return chooserIntent;
    }


}
