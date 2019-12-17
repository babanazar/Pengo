package com.example.pengout.view.fragment;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GelecekEtkinlikFragment extends Fragment {

    private View gelecekEtkinlikView;
    private RecyclerView myGelecekEtkinlikList;

    private DatabaseReference gelecekEtkinlikRef,tableRef,usersRef;
    private int pos;

    private FirebaseAuth mAuth;
    private String currentUserID;

    FirebaseRecyclerAdapter<Event, GelecekEtkinlikViewHolder> adapter;


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


        tableRef = FirebaseDatabase.getInstance().getReference().child("registered");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");




        gelecekEtkinlikRef = FirebaseDatabase.getInstance().getReference().child("eventWithDesc"); // just add .child(currentUserID);

        return gelecekEtkinlikView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(gelecekEtkinlikRef, Event.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<Event, GelecekEtkinlikViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final GelecekEtkinlikViewHolder holder, int position, @NonNull Event model) {

                final String eventIDs = getRef(position).getKey();
                gelecekEtkinlikRef.child(eventIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                        //if(joined[0])
                        //    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton2));
                        //else
                        //    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton));
                        //if(saved[0])
                        //    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_black_24dp));
                        //else
                        //    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                        final ArrayList<String> loc = new ArrayList<>();
                        if (dataSnapshot.hasChild("url")) {
                            imageUrl = dataSnapshot.child("url").getValue().toString();
                            name = dataSnapshot.child("name").getValue().toString();
                            place = dataSnapshot.child("place").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();
                            date = dataSnapshot.child("date").getValue().toString();
                            desc = dataSnapshot.child("desc").getValue().toString();
                            for(DataSnapshot child : dataSnapshot.child("loc").getChildren()){
                                loc.add(child.getValue().toString());
                            }

                            holder.name.setText(name);
                            holder.place.setText(place);
                            holder.time.setText(time);
                            holder.date.setText(date);
                            Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.image);

                        }else {
                            name = dataSnapshot.child("name").getValue().toString();
                            place = dataSnapshot.child("place").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();
                            date = dataSnapshot.child("date").getValue().toString();
                            desc = dataSnapshot.child("desc").getValue().toString();
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

                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!joined[0]){
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton2));
                                    joined[0] =true;
                                    Toast.makeText(getContext()," Added to your events",Toast.LENGTH_SHORT).show();
                                    Map<String,Object> eventuser = new HashMap<>();
                                    tableRef.child(eventIDs).child(currentUserID).child("timestamp").setValue(System.currentTimeMillis());
                                }
                                else{
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton));
                                    joined[0] =false;
                                    Toast.makeText(getContext(),"Removed from your events",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext(),"Added to bookmarks",Toast.LENGTH_SHORT).show();
                                    usersRef.child(currentUserID).child("saved").child(eventIDs).child("timestamp").setValue(System.currentTimeMillis());
                                }
                                else{
                                    holder.save.setBackground(getResources().getDrawable(R.drawable.ic_bookmark_border_black_24dp));
                                    saved[0] =false;
                                    Toast.makeText(getContext(),"Removed from bookmarks",Toast.LENGTH_SHORT).show();
                                    usersRef.child(currentUserID).child("saved").child(eventIDs).removeValue();
                                }
                            }
                        });

                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    pos = holder.getAdapterPosition();
                                    Intent eventActivityIntent = new Intent(getContext(), EventActivity.class);
                                    eventActivityIntent.putExtra("event_id",eventIDs);
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
            public GelecekEtkinlikViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_display_layout, viewGroup, false);
                GelecekEtkinlikViewHolder viewHolder = new GelecekEtkinlikViewHolder(view);
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
        myGelecekEtkinlikList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class GelecekEtkinlikViewHolder extends RecyclerView.ViewHolder {

        TextView name, place, time, date;
        ImageView image;
        Button join,save;

        public GelecekEtkinlikViewHolder(@NonNull View itemView) {
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
        myGelecekEtkinlikList.smoothScrollToPosition(pos);
    }

}
