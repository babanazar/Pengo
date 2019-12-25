package com.example.pengout.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.view.fragment.JoinedFragment;
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

public class SavedEventsActivity extends AppCompatActivity {

    private RecyclerView savedEventsList;
    private Toolbar savedEventsToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef, savedEventsRef;

    private String currentUserId;

    private int pos;

    FirebaseRecyclerAdapter<Event, SavedEventViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);

        savedEventsList = findViewById(R.id.saved_events_list);
        savedEventsList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        currentUserId = (String) getIntent().getExtras().get("current_user_id");

        savedEventsToolbar = findViewById(R.id.saved_events_toolbar);
        setSupportActionBar(savedEventsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Saved Events");

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(userRef.child(currentUserId).child("saved"), Event.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Event, SavedEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final SavedEventViewHolder holder, int position, @NonNull Event model) {

                final String eventId = getRef(position).getKey();

                userRef.child(currentUserId).child("saved").child(eventId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imageUrl = "";
                        String name="";
                        String place = "";
                        String date = "";
                        String desc = "";

                        final long count = (Long) dataSnapshot.child("count").getValue();
                        final ArrayList<String> loc = new ArrayList<>();

                        if (dataSnapshot.hasChild("url")) {
                            imageUrl = (String) dataSnapshot.child("url").getValue();
                            name = (String) dataSnapshot.child("name").getValue();
                            place = (String) dataSnapshot.child("place").getValue();
                            String time = (String) dataSnapshot.child("time").getValue();
                            date = (String) dataSnapshot.child("date").getValue();
                            desc = (String) dataSnapshot.child("desc").getValue();

                            for (DataSnapshot child : dataSnapshot.child("loc").getChildren()) {
                                loc.add((String) child.getValue());
                            }

                            holder.name.setText(name);
                            holder.place.setText(place);
                            holder.time.setText(time);
                            holder.date.setText(date);
                            Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.image);
                        }
                        else {
                            name = (String)dataSnapshot.child("name").getValue();
                            place = (String)dataSnapshot.child("place").getValue();
                            String time = (String)dataSnapshot.child("time").getValue();
                            date = (String)dataSnapshot.child("date").getValue();
                            desc = (String)dataSnapshot.child("desc").getValue();
//                            loc = dataSnapshot.child("loc").getValue();
                            for(DataSnapshot child : dataSnapshot.child("loc").getChildren()){
                                loc.add(child.getValue().toString());
                            }

                            holder.name.setText(name);
                            holder.place.setText(place);
                            holder.time.setText(time);
                            holder.date.setText(date);
                        }
                        final String finalImageUrl = imageUrl;
                        final String finalName = name;
                        final String finalDate = date;
                        final String finalPlace = place;
                        final String finalDesc = desc;
//                        final Event.Location finalLoc = loc;
                        for(DataSnapshot child : dataSnapshot.child("loc").getChildren()){
                            loc.add(child.getValue().toString());
                        }

                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pos = holder.getAdapterPosition();
                                Intent eventActivityIntent = new Intent(SavedEventsActivity.this, EventActivity.class);
                                eventActivityIntent.putExtra("event_id",eventId);
                                eventActivityIntent.putExtra("event_name", finalName);
                                eventActivityIntent.putExtra("event_date", finalDate);
                                eventActivityIntent.putExtra("event_place", finalPlace);
                                eventActivityIntent.putExtra("event_desc", finalDesc);
                                eventActivityIntent.putExtra("event_loc",  loc);
                                eventActivityIntent.putExtra("event_image_url", finalImageUrl);
                                eventActivityIntent.putExtra("event_count", count);
                                startActivity(eventActivityIntent,
                                        ActivityOptions.makeSceneTransitionAnimation(SavedEventsActivity.this,holder.image,"shareView").toBundle());

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
            public SavedEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                SavedEventViewHolder viewHolder = new SavedEventViewHolder(view);
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
        savedEventsList.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static class  SavedEventViewHolder extends RecyclerView.ViewHolder{
        TextView name, place, time, date;
        ImageView image;

        Button join, save;

        public SavedEventViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            place = itemView.findViewById(R.id.event_place);
            time = itemView.findViewById(R.id.event_time);
            date = itemView.findViewById(R.id.event_date);
            image = itemView.findViewById(R.id.event_image);

            join = itemView.findViewById(R.id.homeJoin);
            save = itemView.findViewById(R.id.homeSave);

            join.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        savedEventsList.smoothScrollToPosition(pos);
    }
}
