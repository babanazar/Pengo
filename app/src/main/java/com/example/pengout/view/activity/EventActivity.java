package com.example.pengout.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.pengout.R;
import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity {

    private String eventID;
    private String eventUrl;
    private final String TAG = "IN EVENT ACTIVITY";

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getExtras().get("visit_event_id").toString();
        eventUrl = getIntent().getExtras().get("event_image_url").toString();
        imageView = findViewById(R.id.eventIm);
        Picasso.get().load(eventUrl).into(imageView);



        Log.d(TAG, "onCreate: ");
    }
}
