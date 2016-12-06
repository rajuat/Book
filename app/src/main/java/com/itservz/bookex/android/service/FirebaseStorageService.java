package com.itservz.bookex.android.service;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itservz.bookex.android.R;

/**
 * Created by Raju on 12/6/2016.
 */

public class FirebaseStorageService {

    private FirebaseStorageService() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://bookexfirebaseproject.appspot.com/");
    }

    public static FirebaseStorageService INSTANCE = new FirebaseStorageService();
    private byte[] image = null;
    private StorageReference storageRef;

    public FirebaseStorageService getInstance() {
        return INSTANCE;
    }

    public Task<byte[]> getImage(String fileName) {

        StorageReference splashRef = storageRef.child(fileName);
        final long ONE_MEGABYTE = 1024 * 1024;
        return splashRef.getBytes(ONE_MEGABYTE);
    }


}
