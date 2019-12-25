package com.example.pengout.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserID, currentState, senderOrCurrentUserID, profileName, profileImage;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userAddress;
    private Button sendFollowRequestButton;

    private DatabaseReference userRef,  notificationRef;
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    private String currentTimestamp;

    private RelativeLayout profileJoinedRel, profilePostedRel;

    ArrayList<String> friendList = new ArrayList<>();

    private TextView otherJoinedCount, otherPostedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = findViewById(R.id.other_profile_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentTimestamp = String.valueOf(System.currentTimeMillis());

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        notificationRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        profileJoinedRel = findViewById(R.id.other_joinedCount);
        profilePostedRel = findViewById(R.id.other_postedCount);

        receiverUserID = (String)getIntent().getExtras().get("visit_user_id");
        profileName = (String)getIntent().getExtras().get("visit_user_name");
        profileImage = (String)getIntent().getExtras().get("visit_user_image");

        senderOrCurrentUserID = mAuth.getCurrentUser().getUid();



        userProfileImage = findViewById(R.id.other_profile_image);
        userProfileName = findViewById(R.id.other_tv_name);
        userAddress = findViewById(R.id.other_tv_address);
        sendFollowRequestButton = findViewById(R.id.other_follow);

        profileJoinedRel = findViewById(R.id.other_joinedCount);
        profilePostedRel = findViewById(R.id.other_postedCount);

        otherPostedCount = findViewById(R.id.other_post_count);
        otherJoinedCount = findViewById(R.id.other_join_count);

        userRef.child(senderOrCurrentUserID).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                for (DataSnapshot data : dataSnapshot1.getChildren()){
                    friendList.add(data.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        retreiveUserInfo();








//        userRef.child(senderOrCurrentUserID).child("friends").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild(receiverUserID)) {
//                    profilePostedRel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(ProfileActivity.this, "Receiver Id " + receiverUserID, Toast.LENGTH_SHORT).show();
//                            Intent otherProfileEvents = new Intent(ProfileActivity.this, PostedAndJoinedActivity.class);
//                            otherProfileEvents.putExtra("position", 1);
//                            otherProfileEvents.putExtra("profile_id", receiverUserID);
//                        }
//                    });
//
//                    profileJoinedRel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(ProfileActivity.this, "Receiver Id " + receiverUserID, Toast.LENGTH_SHORT).show();
//                            Intent otherProfileEvents = new Intent(ProfileActivity.this, PostedAndJoinedActivity.class);
//                            otherProfileEvents.putExtra("position", 0);
//                            otherProfileEvents.putExtra("profile_id", receiverUserID);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    boolean isFriend(String id){
        for(String i : friendList){
            if(id.equals(i))
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        retreiveUserInfo();





        userRef.child(receiverUserID).child("joined").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherJoinedCount.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child(receiverUserID).child("posted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherPostedCount.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(isFriend(receiverUserID)){
            profilePostedRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent otherProfileEvents = new Intent(ProfileActivity.this, PostedAndJoinedActivity.class);
                    otherProfileEvents.putExtra("position", 1);
                    otherProfileEvents.putExtra("profile_id", receiverUserID);
                }
            });

            profileJoinedRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfileActivity.this, "Receiver Id " + receiverUserID, Toast.LENGTH_SHORT).show();
                    Intent otherProfileEvents = new Intent(ProfileActivity.this, PostedAndJoinedActivity.class);
                    otherProfileEvents.putExtra("position", 0);
                    otherProfileEvents.putExtra("profile_id", receiverUserID);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_to_chat_button){
            // do something here
//            Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
//            startActivity(chatIntent );
            Intent privateChatIntent = new Intent(ProfileActivity.this, PrivateChatActivity.class);

            privateChatIntent.putExtra("visit_user_id", receiverUserID);
            privateChatIntent.putExtra("visit_user_name", profileName);
            privateChatIntent.putExtra("visit_image", profileImage);

            startActivity(privateChatIntent);

        }
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }



    private void retreiveUserInfo() {
        userRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && (dataSnapshot.hasChild("image"))) {
                    String userImage = (String) dataSnapshot.child("image").getValue();
                    String userName = (String) dataSnapshot.child("name").getValue();
                    String userStatus = (String) dataSnapshot.child("address").getValue();
                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);

                    userProfileName.setText(userName);
                    userAddress.setText(userStatus);
                    currentState = "new";
                } else {
                    String userName = (String) dataSnapshot.child("name").getValue();
                    String userAddressString = (String) dataSnapshot.child("address").getValue();

                    currentState = "new";

                    userProfileName.setText(userName);
                    userAddress.setText(userAddressString);

                }
                manageFollowRequests();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void manageFollowRequests() {
        userRef.child(senderOrCurrentUserID).child("received follow requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(receiverUserID)) {
                    sendFollowRequestButton.setText("Accept Follow Request");
                    currentState = "request_received";
                }

                else {
                    userRef.child(senderOrCurrentUserID).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiverUserID)) {
                                currentState = "friends";
                                sendFollowRequestButton.setText("Unfollow");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!senderOrCurrentUserID.equals(receiverUserID)) {
            sendFollowRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFollowRequestButton.setEnabled(false);
                    if (currentState.equals("new")) {
                        sendFollowRequest();
                    }
                    if (currentState.equals("request_sent")) {
                        cancelFollowRequest();
                    }
                    if (currentState.equals("request_received")) {
                        acceptFollowRequest();
                    }
                    if (currentState.equals("friends")) {
                        removeSpecificContact();
                    }
                }
            });
        } else {
            sendFollowRequestButton.setVisibility(View.INVISIBLE);
        }

    }


    private void removeSpecificContact() {
        userRef.child(senderOrCurrentUserID).child("friends").child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child(receiverUserID).child("friends").child(senderOrCurrentUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                sendFollowRequestButton.setEnabled(true);
                                                currentState = "new";
                                                sendFollowRequestButton.setText("Follow");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void acceptFollowRequest() {
        userRef.child(senderOrCurrentUserID).child("friends").child(receiverUserID).child("timestamp")
                .setValue(currentTimestamp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            userRef.child(receiverUserID).child("friends").child(senderOrCurrentUserID).child("timestamp")
                                .setValue(currentTimestamp)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                userRef.child(senderOrCurrentUserID).child("received follow requests").child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    userRef.child(receiverUserID).child("sent follow requests").child(senderOrCurrentUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    sendFollowRequestButton.setEnabled(true);
                                                                                    currentState = "friends";
                                                                                    sendFollowRequestButton.setText("Remove Contact");

                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelFollowRequest() {
        userRef.child(senderOrCurrentUserID).child("sent follow requests").child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child(receiverUserID).child("received follow requests").child(senderOrCurrentUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                sendFollowRequestButton.setEnabled(true);
                                                currentState = "new";
                                                sendFollowRequestButton.setText("Follow");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendFollowRequest() {
        userRef.child(senderOrCurrentUserID).child("sent follow requests").child(receiverUserID).child("timestamp")
                .setValue(currentTimestamp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userRef.child(receiverUserID).child("received follow requests").child(senderOrCurrentUserID).child("timestamp")
                                    .setValue(currentTimestamp)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                HashMap<String, String> chatNotificationMap = new HashMap<>();
                                                chatNotificationMap.put("from", senderOrCurrentUserID);
                                                chatNotificationMap.put("type", "request");

                                                notificationRef.child(receiverUserID).push()
                                                        .setValue(chatNotificationMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    sendFollowRequestButton.setEnabled(true);
                                                                    currentState = "request_sent";
                                                                    sendFollowRequestButton.setText("Cancel Follow Request");
                                                                }
                                                            }
                                                        });


                                            }
                                        }
                                    });
                        }
                    }
                });
    }

}
