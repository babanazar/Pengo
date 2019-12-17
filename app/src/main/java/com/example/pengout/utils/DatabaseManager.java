package com.example.pengout.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootReference;

    public DatabaseManager(String currentUserId){
        this.currentUserId = currentUserId;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.rootReference = FirebaseDatabase.getInstance().getReference();

    }
    public void saveClickedEvent(String eventId){

    }
}
