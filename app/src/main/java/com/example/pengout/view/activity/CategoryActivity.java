package com.example.pengout.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {
    int imageName;
    private int pos;

    ImageView categoryIm;
    String categoryName,title;
    CollapsingToolbarLayout ctl;
    DatabaseReference eventsRef,usersRef,tableRef;
    RecyclerView categoryList;
    String currentUserID;
    FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Event, EtkinlikViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        imageName = getIntent().getExtras().getInt("image");
        categoryName = getIntent().getExtras().getString("name");
        title = getIntent().getExtras().getString("n");
        categoryIm = findViewById(R.id.categoryImage);
        categoryIm.setBackgroundResource(imageName);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getUid();
        categoryList = findViewById(R.id.categoryList);
        categoryList.setLayoutManager(new LinearLayoutManager(this));
        ctl = findViewById(R.id.collapsing_toolbar_layout);

        ctl.setTitle(title);
        ctl.setExpandedTitleColor(Color.argb(0,0,0,0));
        ctl.setCollapsedTitleTextColor(Color.rgb(0,0,0));
        ctl.setCollapsedTitleGravity(Gravity.FILL_HORIZONTAL);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        eventsRef = FirebaseDatabase.getInstance().getReference().child("eventWithDesc");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        tableRef = FirebaseDatabase.getInstance().getReference().child("registered");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(eventsRef, Event.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<Event, EtkinlikViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final EtkinlikViewHolder holder, final int position, @NonNull Event model) {

                final String eventIDs = getRef(position).getKey();
                eventsRef.child(eventIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.child("category").getValue().equals(categoryName)){
                            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                            params.height = 0;
                            holder.itemView.setLayoutParams(params);
                            return;
                        }

                        String imageUrl = "";
                        String name="";
                        String place = "";
                        String date = "";
                        String desc = "";
                        final boolean[] joined = {false};
                        final boolean[] saved = {false};

                        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("saved").hasChild(eventIDs)){
                                    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
                                    saved[0] = true;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        tableRef.child(eventIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(currentUserID)){
                                    joined[0] = true;
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton2));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        final ArrayList<String> loc = new ArrayList<>();



                        if (dataSnapshot.hasChild("url")) {
                            imageUrl = (String)dataSnapshot.child("url").getValue();

                        }
                        else{
                            imageUrl = "";
                        }
                        name = (String) dataSnapshot.child("name").getValue();
                        place = (String)dataSnapshot.child("place").getValue();
                        String time = (String)dataSnapshot.child("time").getValue();
                        date = (String)dataSnapshot.child("date").getValue();
                        desc = (String)dataSnapshot.child("desc").getValue();
                        for(DataSnapshot child : dataSnapshot.child("loc").getChildren()){
                            loc.add((String)child.getValue());
                        }

                        holder.name.setText(name);
                        holder.place.setText(place);
                        holder.time.setText(time);
                        holder.date.setText(date);
                        Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.image);

                        final String finalImageUrl = imageUrl;
                        final String finalName = name;
                        final String finalDate = date;
                        final String finalPlace = place;
                        final String finalDesc = desc;

                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!joined[0]){
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton2));
                                    joined[0] =true;
                                    Toast.makeText(getApplicationContext()," Added to your events",Toast.LENGTH_SHORT).show();
                                    Map<String,Object> eventuser = new HashMap<>();
                                    tableRef.child(eventIDs).child(currentUserID).child("timestamp").setValue(System.currentTimeMillis());
                                }
                                else{
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton));
                                    joined[0] =false;
                                    Toast.makeText(getApplicationContext(),"Removed from your events",Toast.LENGTH_SHORT).show();
                                    tableRef.child(eventIDs).child(currentUserID).removeValue();
                                }
                            }
                        });

                        holder.save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!saved[0]){
                                    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
                                    saved[0] =true;
                                    Toast.makeText(getApplicationContext(),"Added to bookmarks",Toast.LENGTH_SHORT).show();
                                    usersRef.child(currentUserID).child("saved").child(eventIDs).child("timestamp").setValue(System.currentTimeMillis());
                                }
                                else{
                                    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                                    saved[0] =false;
                                    Toast.makeText(getApplicationContext(),"Removed from bookmarks",Toast.LENGTH_SHORT).show();
                                    usersRef.child(currentUserID).child("saved").child(eventIDs).removeValue();
                                }
                            }
                        });
                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pos = holder.getAdapterPosition();
                                Intent eventActivityIntent = new Intent(getApplicationContext(), EventActivity.class);
                                eventActivityIntent.putExtra("event_id",eventIDs);
                                eventActivityIntent.putExtra("event_name", finalName);
                                eventActivityIntent.putExtra("event_date", finalDate);
                                eventActivityIntent.putExtra("event_place", finalPlace);
                                eventActivityIntent.putExtra("event_desc", finalDesc);
                                eventActivityIntent.putExtra("event_loc", loc);
                                eventActivityIntent.putExtra("event_image_url", finalImageUrl);
                                startActivity(eventActivityIntent);

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public EtkinlikViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                EtkinlikViewHolder viewHolder = new EtkinlikViewHolder(view);
                return viewHolder;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        adapter.setHasStableIds(true);
        categoryList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class EtkinlikViewHolder extends RecyclerView.ViewHolder {

        TextView name, place, time, date;
        ImageView image;
        Button join,save;

        public EtkinlikViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            place = itemView.findViewById(R.id.event_place);
            time = itemView.findViewById(R.id.event_time);
            date = itemView.findViewById(R.id.event_date);
            image = itemView.findViewById(R.id.event_image);
            join = itemView.findViewById(R.id.homeJoin);
            save = itemView.findViewById(R.id.homeSave);


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        categoryList.smoothScrollToPosition(pos);
    }


}
