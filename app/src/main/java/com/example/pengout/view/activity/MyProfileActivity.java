package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private CircleImageView myProfileImage;

    private TextView name;
    private TextView address;
    public TextView joinCountTextView;
    public TextView postCountTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef, userRef, createdEventsRef, registeredEventsRef;

    private RelativeLayout postedCount, joinedCount;

//    public final int postedNumber, joinedNumber;

    private String currentUserId;

    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MyProfileActivity.this;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        mToolbar = findViewById(R.id.my_profile_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pengout");

        setupBottomNavigationView();
        myProfileImage = findViewById(R.id.my_profile_image);
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        joinCountTextView = findViewById(R.id.join_count);
        postCountTextView = findViewById(R.id.post_count);

        postedCount = findViewById(R.id.postedCount);

        postedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postedTabIntent = new Intent(MyProfileActivity.this, PostedAndJoinedActivity.class);
                postedTabIntent.putExtra("position", 1);
                postedTabIntent.putExtra("profile_id", currentUserId);
                startActivity(postedTabIntent);
            }
        });


        joinedCount = findViewById(R.id.joinedCount);


        joinedCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinedTabIntent = new Intent(MyProfileActivity.this, PostedAndJoinedActivity.class);
                joinedTabIntent.putExtra("position", 0);
                joinedTabIntent.putExtra("profile_id", currentUserId);
                startActivity(joinedTabIntent);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        currentUserId = mAuth.getCurrentUser().getUid();
        createdEventsRef = FirebaseDatabase.getInstance().getReference().child("createdEvents").child(currentUserId);
        registeredEventsRef = userRef.child(currentUserId).child("joined");


        retreiveUserInfo();
        createdEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postCountTextView.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        registeredEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                joinCountTextView.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void retreiveUserInfo() {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && (dataSnapshot.hasChild("image"))) {
                    String userImage = (String) dataSnapshot.child("image").getValue();
                    String nameFromDb = (String) dataSnapshot.child("name").getValue();
                    String addressFromDb = (String) dataSnapshot.child("address").getValue();

                    name.setText(nameFromDb);
                    address.setText(addressFromDb);
                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(myProfileImage);


                } else {
                    String nameFromDb = (String) dataSnapshot.child("name").getValue();
                    String addressFromDb = (String) dataSnapshot.child("address").getValue();

                    name.setText(nameFromDb);
                    address.setText(addressFromDb);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.edit_my_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_profile_button) {
            // do something here
            Intent settingsIntent = new Intent(MyProfileActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

        }

        else if(id == R.id.saved_events_button){
            Intent savedEvents = new Intent(MyProfileActivity.this, SavedEventsActivity.class);
            savedEvents.putExtra("current_user_id", currentUserId);
            startActivity(savedEvents);
        }
        return super.onOptionsItemSelected(item);
    }
}
