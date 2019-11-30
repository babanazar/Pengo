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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutureEventFragment extends Fragment {

    private View eventsView;
    private RecyclerView eventsList;

    private DatabaseReference eventsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

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
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, final int position, @NonNull Event model) {

                        holder.eventName.setText(model.getEventName());
                        holder.eventTime.setText(model.getEventName());
                        holder.eventDate.setText(model.getEventName());
                        holder.eventPlace.setText(model.getEventName());
//                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.eventImage);
                        /*String userIDs = getRef(position).getKey();

                        eventsRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild("image")) {
                                    String userImage = dataSnapshot.child("image").getValue().toString();
                                    String profileName = dataSnapshot.child("name").getValue().toString();
                                    String profileStatus = dataSnapshot.child("status").getValue().toString();

                                    holder.eventName.setText(profileName);
                                    holder.eventPlace.setText(profileName);
                                    holder.eventDate.setText(profileName);
                                    holder.eventTime.setText(profileName);
                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.eventImage);
                                } else {
                                    String eventDate = dataSnapshot.child("date").getValue().toString();
                                    String eventName = dataSnapshot.child("name").getValue().toString();
                                    String eventPlace = dataSnapshot.child("place").getValue().toString();
                                    String eventTime = dataSnapshot.child("time").getValue().toString();

                                    holder.eventName.setText(eventName);
                                    holder.eventPlace.setText(eventPlace);
                                    holder.eventDate.setText(eventDate);
                                    holder.eventTime.setText(eventTime);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        */
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                        EventViewHolder viewHolder = new EventViewHolder(view);
                        return viewHolder;
                    }

                };
        eventsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName, eventDate, eventPlace, eventTime;
        CircleImageView eventImage;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            eventPlace = itemView.findViewById(R.id.event_place);
            eventTime = itemView.findViewById(R.id.event_time);
            eventImage = itemView.findViewById(R.id.event_image);
        }
    }
}
