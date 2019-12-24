package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.pengout.R;

import java.util.Calendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String eventName, eventUrl, eventDate, eventPlace, eventDesc;
    private final String TAG = "IN EVENT ACTIVITY";
    private boolean clicked = false;
    private boolean saved = false;
    private String eventId;

    ImageView imageView;
    TextView title, description, date, place;
    CollapsingToolbarLayout ctl;
    ArrayList<String> eventLoc;

    private DatabaseReference tableRef,usersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventId = (String) getIntent().getExtras().get("event_id");
        eventName = (String)getIntent().getExtras().get("event_name");
        eventUrl =(String) getIntent().getExtras().get("event_image_url");
        eventPlace = (String)getIntent().getExtras().get("event_place");
        eventDate = (String)getIntent().getExtras().get("event_date");
        eventDesc = (String)getIntent().getExtras().get("event_desc");
        eventLoc = (ArrayList<String>) getIntent().getExtras().get("event_loc");
//        Toast.makeText(this, (String)eventLoc.toString(), Toast.LENGTH_SHORT).show();
        imageView = findViewById(R.id.eventIm);
        date = findViewById(R.id.date);
        date.setText(eventDate);
        place = findViewById(R.id.place);
        place.setText(eventPlace);
        ctl = findViewById(R.id.collapsing_toolbar_layout);
        description = findViewById(R.id.description);
        description.setText(eventDesc);
        Picasso.get().load(eventUrl).into(imageView);
        Log.d(TAG, "onCreate: ");


        Toolbar toolbar = findViewById(R.id.toolbar);
        ctl.setTitle(eventName);
        ctl.setExpandedTitleColor(Color.argb(0,0,0,0));
        ctl.setCollapsedTitleTextColor(Color.rgb(0,0,0));
        //ctl.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded);
        //ctl.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        ctl.setCollapsedTitleGravity(Gravity.FILL_HORIZONTAL);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tableRef = FirebaseDatabase.getInstance().getReference().child("registered");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("saved").hasChild(eventId))
                    saved = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tableRef.child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUserID)){
                    clicked = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mybutton) {
            if(!clicked){
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.eventbutton2));
                clicked =true;
                Toast.makeText(this,eventId + " Added to your events",Toast.LENGTH_SHORT).show();
                Map<String,Object> eventuser = new HashMap<>();
                tableRef.child(eventId).child(currentUserID).child("timestamp").setValue(System.currentTimeMillis());
                //Map<String,Object> reg = new HashMap<>();
                //reg.put(eventId+"/" +currentUserID,"registered");
            }
            else{
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.eventbutton));
                clicked=false;
                Toast.makeText(this,"Removed from your events",Toast.LENGTH_SHORT).show();
                tableRef.child(eventId).child(currentUserID).removeValue();
            }

        }
        else if(id == R.id.bookmark){
            if(!saved){
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
                saved =true;
                Toast.makeText(this,"Added to bookmarks",Toast.LENGTH_SHORT).show();
                usersRef.child(currentUserID).child("saved").child(eventId).child("timestamp").setValue(System.currentTimeMillis());
            }
            else{
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                saved=false;
                Toast.makeText(this,"Removed from bookmarks",Toast.LENGTH_SHORT).show();
                usersRef.child(currentUserID).child("saved").child(eventId).removeValue();
            }
        }
        else
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location;
        try{
            location = new LatLng(Double.valueOf(eventLoc.get(0)),Double.valueOf(eventLoc.get(1)));
        }catch(Exception e){
            location = new LatLng(39.874611, 32.747378);
        }
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(eventPlace));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location,15);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        this.menu = menu;
        if(clicked)
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.eventbutton2));
        else
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.eventbutton));
        if(saved)
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
        else
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    public void setNotification(Calendar date) {

        NotifyMe notifyMe = new NotifyMe.Builder(this)
                .title(eventName)
                .content(eventName + " is approaching! It's on ")  //+ eventTime+ " tomorrow.")
                .color(0, 255, 255, 255)
                .led_color(255, 255, 255, 255)
                .time(date)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Snooze", false)
                .key("text")
                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Details", true, false)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }
}
