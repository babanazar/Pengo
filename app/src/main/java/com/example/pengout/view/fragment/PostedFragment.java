package com.example.pengout.view.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.view.activity.EventActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PostedFragment extends Fragment {

    private View postedView;
    private RecyclerView myPostedEventsList;

    private DatabaseReference postedEventsRef, usersRef;
    private int pos;

    private FirebaseAuth mAuth;
    private String currentUserId;

    FirebaseRecyclerAdapter<Event, PostedEventViewHolder> adapter;


    public PostedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        postedView = inflater.inflate(R.layout.fragment_posted, container, false);

        myPostedEventsList = postedView.findViewById(R.id.posted_events_list);
        myPostedEventsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        postedEventsRef = FirebaseDatabase.getInstance().getReference().child("createdEvents").child(currentUserId);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        return postedView;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(postedEventsRef, Event.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Event, PostedEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostedEventViewHolder holder, int position, @NonNull Event model) {
                final String eventId = getRef(position).getKey();
                postedEventsRef.child(eventId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imageUrl = "";
                        String name="";
                        String place = "";
                        String date = "";
                        String desc = "";

                        final ArrayList<String> loc = new ArrayList<>();

                        if (dataSnapshot.hasChild("url")) {
                            imageUrl = (String)dataSnapshot.child("url").getValue();
                            name = (String)dataSnapshot.child("name").getValue();
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

                        }else {
                            name = (String)dataSnapshot.child("name").getValue();
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
                        }
                        final String finalImageUrl = imageUrl;
                        final String finalName = name;
                        final String finalDate = date;
                        final String finalPlace = place;
                        final String finalDesc = desc;
                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pos = holder.getAdapterPosition();
                                Intent eventActivityIntent = new Intent(getContext(), EventActivity.class);
                                eventActivityIntent.putExtra("event_id",eventId);
                                eventActivityIntent.putExtra("event_name", finalName);
                                eventActivityIntent.putExtra("event_date", finalDate);
                                eventActivityIntent.putExtra("event_place", finalPlace);
                                eventActivityIntent.putExtra("event_desc", finalDesc);
                                eventActivityIntent.putExtra("event_loc", loc);
                                eventActivityIntent.putExtra("event_image_url", finalImageUrl);
                                startActivity(eventActivityIntent,
                                        ActivityOptions.makeSceneTransitionAnimation((Activity)getContext(),holder.image,"shareView").toBundle());

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
            public PostedEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                PostedEventViewHolder viewHolder = new PostedEventViewHolder(view);
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
        myPostedEventsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PostedEventViewHolder extends RecyclerView.ViewHolder{

        TextView name, place, time, date;
        ImageView image;

        Button join, save;

        public PostedEventViewHolder(@NonNull View itemView) {
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
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
        myPostedEventsList.smoothScrollToPosition(pos);
    }


}
