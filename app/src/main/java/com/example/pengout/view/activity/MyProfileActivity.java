package com.example.pengout.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pengout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private CircleImageView myProfileImage;

    private TextView name;
    private TextView address;
    private TextView joinCount;
    private TextView postCount;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef, userRef;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        myProfileImage = findViewById(R.id.my_profile_image);
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        joinCount = findViewById(R.id.join_count);
        postCount = findViewById(R.id.post_count);

        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        currentUserId = mAuth.getCurrentUser().getUid();

        retreiveUserInfo();

    }


    private void retreiveUserInfo() {
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && (dataSnapshot.hasChild("image"))) {
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

//                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
//                    userProfileName.setText(userName);
//                    userProfileStatus.setText(userStatus);


                } else {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

//                    userProfileName.setText(userName);
//                    userProfileStatus.setText(userStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
