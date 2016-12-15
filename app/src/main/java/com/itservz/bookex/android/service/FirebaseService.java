package com.itservz.bookex.android.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by raju.athokpam on 15-12-2016.
 */

public class FirebaseService {

    public FirebaseApp app;
    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseStorage storage;
    private static FirebaseService INSTANCE = new FirebaseService();

    public FirebaseService() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
    }

    public static FirebaseService getInstance() {
        return INSTANCE;
    }
}
