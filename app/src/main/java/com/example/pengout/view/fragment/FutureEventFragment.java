package com.example.pengout.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FutureEventFragment extends Fragment {

    private View eventsView;
    private RecyclerView eventsList;

    private DatabaseReference eventsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    private String retImage = "default_image";

    public FutureEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventsView = inflater.inflate(R.layout.fragment_future_event, container, false);

        eventsList = eventsView.findViewById(R.id.future_events_list);
        eventsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        eventsRef = FirebaseDatabase.getInstance().getReference().child("futureEvents");
//        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        return eventsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(eventsRef, Event.class)
                        .build();

        FirebaseRecyclerAdapter<Event, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull Event model) {


//                        holder.name.setText(model.getName());
//                        holder.time.setText(model.getName());
//                        holder.date.setText(model.getName());
//                        holder.place.setText(model.getName());
//                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.eventImage);
                        final String eventIDs = getRef(position).getKey();

                        eventsRef.child(eventIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild("image")) {
                                    String userImage = dataSnapshot.child("image").getValue().toString();
                                    String profileName = dataSnapshot.child("name").getValue().toString();
                                    String profileStatus = dataSnapshot.child("status").getValue().toString();

                                    holder.name.setText(profileName);
                                    holder.place.setText(profileName);
                                    holder.date.setText(profileName);
                                    holder.time.setText(profileName);
//                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.eventImage);
                                } else {
                                    String eventDate = dataSnapshot.child("date").getValue().toString();
                                    String eventName = dataSnapshot.child("name").getValue().toString();
                                    String eventPlace = dataSnapshot.child("place").getValue().toString();
                                    String eventTime = dataSnapshot.child("time").getValue().toString();

                                    holder.name.setText(eventName);
                                    holder.place.setText(eventPlace);
                                    holder.date.setText(eventDate);
                                    holder.time.setText(eventTime);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                        return new EventViewHolder(view);
                    }

                };
        eventsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView name, date, place, time;
//        CircleImageView eventImage;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            date = itemView.findViewById(R.id.event_date);
            place = itemView.findViewById(R.id.event_place);
            time = itemView.findViewById(R.id.event_time);
//            eventImage = itemView.findViewById(R.id.event_image);
        }
    }
}
