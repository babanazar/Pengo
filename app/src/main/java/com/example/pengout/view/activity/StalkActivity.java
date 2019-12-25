package com.example.pengout.view.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.model.Stalk;
import com.example.pengout.model.User;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class StalkActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 3;
    Toolbar mToolbar;
    RecyclerView stalks;
    Query firebaseSearchQuery;
    private DatabaseReference friendsRef,stalksRef;
    DatabaseReference usersRef;
    RecyclerView.Adapter<StalksViewHolder> adapter;
    String currentUserId;
    FirebaseAuth mAuth;
    ArrayList<String> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stalk);

        mToolbar = findViewById(R.id.main_page_toolbar);
        stalks = findViewById(R.id.stalks);
        stalks.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Stalk");


        usersRef = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        friendsRef = usersRef.child(currentUserId).child("friends");
        stalksRef = FirebaseDatabase.getInstance().getReference().child("stalks");



        setupBottomNavigationView();

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

        if (id == R.id.go_to_chat_button) {
            // do something here
            Intent chatIntent = new Intent(StalkActivity.this, ChatActivity.class);
            startActivity(chatIntent );

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBottomNavigationView(){
        final BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(StalkActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    public static class StalksViewHolder extends RecyclerView.ViewHolder{

        TextView stalkBody,time;
        ImageView imageView;
        public StalksViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.stalk_image);
            stalkBody = itemView.findViewById(R.id.stalk_text);
            time = itemView.findViewById(R.id.stalk_time);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        friendsRef.addValueEventListener(new ValueEventListener() {
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

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Stalk>()
                .setQuery(stalksRef, Stalk.class)
                .build();

        FirebaseRecyclerAdapter<Stalk, StalksViewHolder> adapter =
                new FirebaseRecyclerAdapter<Stalk, StalksViewHolder>(options){
                    @Override
                    protected void onBindViewHolder(@NonNull final StalksViewHolder holder, int position, @NonNull Stalk model) {
                        String stalkIds = getRef(position).getKey();
                        stalksRef.child(stalkIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                String id = (String) dataSnapshot.child("stalkId").getValue();
                                if(!isFriend(id)){
                                    holder.itemView.setVisibility(View.GONE);
                                    return;
                                }
                                String s =(String)dataSnapshot.child("stalkBody").getValue();
                                String im = (String) dataSnapshot.child("stalkImage").getValue();
                                Picasso.get().load(im).placeholder(R.drawable.profile_image).into(holder.imageView);
                                holder.stalkBody.setText(s);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public StalksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stalk_display, viewGroup, false);
                        StalksViewHolder viewHolder = new StalksViewHolder(view);
                        return viewHolder;
                    }

                };

        stalks.setAdapter(adapter);
        adapter.startListening();
    }

    boolean isFriend(String id){
        for(String i : friendList){
            if(id.equals(i))
                return true;
        }
        return false;
    }

}


/*
friendsRef.child(userIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usersRef.child(userIds).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String profileName = (String) dataSnapshot.child("name").getValue();
                                        holder.friendName.setText(profileName);
                                        if(dataSnapshot.hasChild("image")){
                                            String userImage = (String) dataSnapshot.child("image").getValue();
                                            Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.imageView);
                                        }
                                        else{
                                            Picasso.get().load("").placeholder(R.drawable.profile_image).into(holder.imageView);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                usersRef.child(userIds).child("joined").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot data: dataSnapshot.getChildren()){
                                            String eventname = (String) data.child("name").getValue();
                                            holder.eventName.setText(eventname);
                                            holder.word.setText(" has joined to ");
                                            long timestamp = Long.valueOf(data.child("timestamp").getValue().toString());
                                            long t = System.currentTimeMillis();
                                            long diff = t - timestamp;
                                            long res = diff / 3600000;
                                            if(res > 1 && res <= 24){
                                                holder.time.setText(" " + res + "h");
                                            }
                                            else if(res > 24 && res <=168){
                                                res = res/24;

                                                holder.time.setText(" " + Long.toString(res) + "d");
                                            }
                                            else if(res > 168){
                                                res = res%7;
                                                holder.time.setText(" "+ Long.toString(res) + "w");
                                            }
                                            else
                                                holder.time.setText(" ");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
 */