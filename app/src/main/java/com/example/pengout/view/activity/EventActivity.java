package com.example.pengout.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.pengout.R;

public class EventActivity extends AppCompatActivity {

    private String eventID;
    private final String TAG = "IN EVENT ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getExtras().get("visit_event_id").toString();
        Log.d(TAG, "onCreate: ");

    }
}
