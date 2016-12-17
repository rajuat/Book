package com.itservz.bookex.android.service;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itservz.bookex.android.DrawerActivity;
import com.itservz.bookex.android.SellActivity;

/**
 * Created by Raju on 12/6/2016.
 */

public class FirebaseStorageService {
    public static FirebaseStorageService INSTANCE = new FirebaseStorageService();
    private StorageReference storageRef;
    private byte[] image = null;

    private FirebaseStorageService() {
        storageRef = FirebaseService.getInstance().storage.getReferenceFromUrl("gs://bookexfirebaseproject.appspot.com/");
    }

    public static FirebaseStorageService getInstance() {
        return INSTANCE;
    }

    public Task<byte[]> getImage(String fileName) {
        StorageReference splashRef = storageRef.child(fileName);
        final long ONE_MEGABYTE = 1024 * 1024;
        return splashRef.getBytes(ONE_MEGABYTE);
    }

    public void setImage(String id, byte[] data, final SellActivity sellActivity){
        StorageReference ref = FirebaseService.getInstance().storage.getReference("books");
        UploadTask uploadTask = ref.child(id).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(sellActivity, "ImageNotUploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(sellActivity, "ImageUploaded: " + taskSnapshot.getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
