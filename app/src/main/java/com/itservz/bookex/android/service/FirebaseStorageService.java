package com.itservz.bookex.android.service;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public void setImage(String id, byte[] data){
        storageRef.child("books").child(id+".jpg");
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Log.d("ImageNotUploaded", exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.d("ImageUploaded", taskSnapshot.getDownloadUrl().toString());
            }
        });
    }


}
