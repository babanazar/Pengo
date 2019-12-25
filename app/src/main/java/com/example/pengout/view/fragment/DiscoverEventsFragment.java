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
public class DiscoverEventsFragment extends Fragment {

    private View gelecekEtkinlikView;
    private RecyclerView myGelecekEtkinlikList;

    private DatabaseReference gelecekEtkinlikRef,tableRef,usersRef,stalksRef;
    private int pos;

    private FirebaseAuth mAuth;
    private String currentUserID;

    FirebaseRecyclerAdapter<Event, GelecekEtkinlikViewHolder> adapter;

    String userName,userPhoto ;

    ArrayList<String> interestList = new ArrayList<>();
    public DiscoverEventsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gelecekEtkinlikView = inflater.inflate(R.layout.fragment_gecmis_etkinlik, container, false);

        myGelecekEtkinlikList = gelecekEtkinlikView.findViewById(R.id.gecmis_etkinlik_list);
        myGelecekEtkinlikList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        tableRef = FirebaseDatabase.getInstance().getReference().child("registered");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        stalksRef = FirebaseDatabase.getInstance().getReference().child("stalks");


        usersRef.child(currentUserID).child("interests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    interestList.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        gelecekEtkinlikRef = FirebaseDatabase.getInstance().getReference().child("newEvents"); // just add .child(currentUserID);

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
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        String cat = dataSnapshot.child("category").getValue().toString();
                        if(!isInterest(cat)){
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
                                userName = (String) dataSnapshot.child("name").getValue();
                                userPhoto = (String) dataSnapshot.child("image").getValue();
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
                            imageUrl = (String) dataSnapshot.child("url").getValue();
                            name = (String) dataSnapshot.child("name").getValue();
                            place = (String) dataSnapshot.child("place").getValue();
                            String time = (String) dataSnapshot.child("time").getValue();
                            date = (String) dataSnapshot.child("date").getValue();
                            desc = (String) dataSnapshot.child("desc").getValue();
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
                        final long cnt = (Long) dataSnapshot.child("count").getValue();

                        holder.join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!joined[0]){
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton2));
                                    joined[0] =true;
                                    Toast.makeText(getContext()," Added to your events",Toast.LENGTH_SHORT).show();
                                    Map<String,Object> eventuser = new HashMap<>();
                                    tableRef.child(eventIDs).child(currentUserID).child("timestamp").setValue(System.currentTimeMillis());
                                    gelecekEtkinlikRef.child(eventIDs).child("count").setValue(cnt+1);
                                    DatabaseReference reference = stalksRef.push();
                                    reference.child("stalkBody").setValue(userName + " has registered to " + finalName);
                                    reference.child("stalkImage").setValue(userPhoto);
                                    reference.child("stalkId").setValue(currentUserID);
                                }
                                else{
                                    holder.join.setBackground(getResources().getDrawable(R.drawable.eventbutton));
                                    joined[0] =false;
                                    Toast.makeText(getContext(),"Removed from your events",Toast.LENGTH_SHORT).show();
                                    tableRef.child(eventIDs).child(currentUserID).removeValue();
                                    gelecekEtkinlikRef.child(eventIDs).child("count").setValue(cnt-1);
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
                                eventActivityIntent.putExtra("event_count",cnt);
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

    boolean isInterest(String cat){
        for (String i : interestList){
            if(i.equals(cat))
                return true;
        }
        return false;
    }

}
