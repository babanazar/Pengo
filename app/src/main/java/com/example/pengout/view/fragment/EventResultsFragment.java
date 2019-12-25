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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.view.activity.EventActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventResultsFragment extends Fragment {

    FirebaseRecyclerAdapter<Event, EventsViewHolder> adapter;
    Query firebaseSearchQuery;

    private RecyclerView results;
    private DatabaseReference mEventDatabase;
    private ImageButton searchButton;
    private EditText searchText;
    long cnt;

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_eventres, container, false);
        results = root.findViewById(R.id.event_results);
        results.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventDatabase = FirebaseDatabase.getInstance().getReference("newEvents");

        searchButton = root.findViewById(R.id.search_btn);
        searchText = root.findViewById(R.id.search_field);


        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String st = searchText.getText().toString();
                    firebaseEventSearch(st);
            }
        });


        return root;
    }


    public static class EventsViewHolder extends RecyclerView.ViewHolder{

        TextView n,d,p;
        ImageView imageView;
        public EventsViewHolder(View itemView){
            super(itemView);
            n = itemView.findViewById(R.id.name_text);
            d = itemView.findViewById(R.id.date_text);
            p = itemView.findViewById(R.id.place_text);
            imageView = itemView.findViewById(R.id.search_im);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseEventSearch("");
    }

    public void firebaseEventSearch(final String searchText){
        firebaseSearchQuery = mEventDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Event>().setQuery(firebaseSearchQuery,Event.class).build();
        adapter = new FirebaseRecyclerAdapter<Event, EventsViewHolder>(options) {
            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchlist_layout,viewGroup,false);
                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull final Event model) {
                holder.n.setText(model.getName());
                holder.d.setText(model.getDate());
                holder.p.setText(model.getPlace());
                Picasso.get().load(model.getUrl()).placeholder(R.drawable.profile_image).into(holder.imageView);
                final String[] eventIDs = new String[1];
                final String[] lat = new String[1];
                final String[] lon = new String[1];
                firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            cnt = (Long) data.child("count").getValue();
                            ArrayList<String> loc = new ArrayList<>();
                            if(data.child("name").getValue().equals(model.getName())){
                                eventIDs[0] = data.getKey();
                                if(data.child("loc").hasChild("lat")){
                                    lat[0] = (String) data.child("loc").child("lat").getValue();
                                    lon[0] = (String) data.child("loc").child("long").getValue();
                                }
                                else{
                                    lat[0] = "";
                                    lon[0] = "";
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final ArrayList<String> location = new ArrayList<>();
                location.add(lat[0]);
                location.add(lon[0]);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),eventIDs[0],Toast.LENGTH_SHORT).show();
                        Intent eventActivityIntent = new Intent(getContext(), EventActivity.class);
                        eventActivityIntent.putExtra("event_id", eventIDs[0]);
                        eventActivityIntent.putExtra("event_name", model.getName());
                        eventActivityIntent.putExtra("event_date", model.getDate());
                        eventActivityIntent.putExtra("event_place", model.getPlace());
                        eventActivityIntent.putExtra("event_desc", model.getDesc());
                        eventActivityIntent.putExtra("event_loc", location);
                        eventActivityIntent.putExtra("event_image_url", model.getUrl());
                        eventActivityIntent.putExtra("event_count",cnt);
                        startActivity(eventActivityIntent);
                    }
                });
            }


        };

        results.setAdapter(adapter);
        adapter.startListening();
    }

}
