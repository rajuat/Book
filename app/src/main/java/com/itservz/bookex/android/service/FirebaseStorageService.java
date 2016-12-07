package com.itservz.bookex.android.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Raju on 12/6/2016.
 */

public class FirebaseStorageService {
    public static FirebaseStorageService INSTANCE = new FirebaseStorageService();
    private StorageReference storageRef;
    private byte[] image = null;

    private FirebaseStorageService() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://bookexfirebaseproject.appspot.com/");
    }

    public FirebaseStorageService getInstance() {
        return INSTANCE;
    }

    public Task<byte[]> getImage(String fileName) {
        StorageReference splashRef = storageRef.child(fileName);
        final long ONE_MEGABYTE = 1024 * 1024;
        return splashRef.getBytes(ONE_MEGABYTE);
    }
}
