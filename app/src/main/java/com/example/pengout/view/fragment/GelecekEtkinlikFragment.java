package com.example.pengout.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GelecekEtkinlikFragment extends Fragment {

    private View gelecekEtkinlikView;
    private RecyclerView myGelecekEtkinlikList;

    private DatabaseReference gelecekEtkinlikRef;

    private FirebaseAuth mAuth;
    private String currentUserID;

    public GelecekEtkinlikFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gelecekEtkinlikView = inflater.inflate(R.layout.fragment_gelecek_etkinlik, container, false);

        myGelecekEtkinlikList = gelecekEtkinlikView.findViewById(R.id.gelecek_etkinlik_list);
        myGelecekEtkinlikList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        gelecekEtkinlikRef = FirebaseDatabase.getInstance().getReference().child("EventsWithCategory").child("art"); // just add .child(currentUserID);

        return gelecekEtkinlikView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(gelecekEtkinlikRef, Event.class)
                        .build();


        FirebaseRecyclerAdapter<Event, GelecekEtkinlikViewHolder> adapter
                = new FirebaseRecyclerAdapter<Event, GelecekEtkinlikViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final GelecekEtkinlikViewHolder holder, int position, @NonNull Event model) {

                final String eventIDs = getRef(position).getKey();
                gelecekEtkinlikRef.child(eventIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("url")) {
                            String imageUrl = dataSnapshot.child("url").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String place = dataSnapshot.child("place").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();
                            String date = dataSnapshot.child("date").getValue().toString();

                            holder.name.setText(name);
                            holder.place.setText(place);
                            holder.time.setText(time);
                            holder.date.setText(date);
                            Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.image);

                        }else {
                            String name = dataSnapshot.child("name").getValue().toString();
                            String place = dataSnapshot.child("place").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();
                            String date = dataSnapshot.child("date").getValue().toString();

                            holder.name.setText(name);
                            holder.place.setText(place);
                            holder.time.setText(time);
                            holder.date.setText(date);
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent eventActivityIntent = new Intent(getContext(), EventActivity.class);

                                eventActivityIntent.putExtra("visit_event_id", eventIDs);
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
            public GelecekEtkinlikViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                GelecekEtkinlikViewHolder viewHolder = new GelecekEtkinlikViewHolder(view);
                return viewHolder;
            }
        };
        myGelecekEtkinlikList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class GelecekEtkinlikViewHolder extends RecyclerView.ViewHolder {

        TextView name, place, time, date;
        ImageView image;

        public GelecekEtkinlikViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            place = itemView.findViewById(R.id.event_place);
            time = itemView.findViewById(R.id.event_time);
            date = itemView.findViewById(R.id.event_date);
            image = itemView.findViewById(R.id.event_image);
        }
    }
}
