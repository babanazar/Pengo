package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    //private ImageView music,sport,edu,theatre;
    private ImageButton searchButton;
    private EditText searchText;
    private RecyclerView results;
    private DatabaseReference mEventDatabase;
    private Toolbar mToolbar;
    private LinearLayout emptyView;
    private static final int ACTIVITY_NUM = 1;

    FirebaseRecyclerAdapter<Event, EventsViewHolder> adapter;
    Query firebaseSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Search");

        setupBottomNavigationView();

        mEventDatabase = FirebaseDatabase.getInstance().getReference("eventWithDesc");

        searchButton = findViewById(R.id.search_btn);
        searchText = findViewById(R.id.search_field);
        results = findViewById(R.id.results);
        emptyView = findViewById(R.id.emptySearch);
        results.setLayoutManager(new LinearLayoutManager(this));

        //Glide.with(getApplicationContext()).load("https://cdn2.allevents.in/thumbs/thumb5dbb7cb95ea77.jpg").override(200,200).centerCrop().into(edu);
        //Glide.with(getApplicationContext()).load(R.drawable.theatre).override(200,200).into(theatre);
        //Glide.with(getApplicationContext()).load(R.drawable.sport_logo).override(200,200).into(sport);
        //Glide.with(getApplicationContext()).load(R.drawable.music).override(200,200).into(music);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String st = searchText.getText().toString();
                firebaseEventSearch(st);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseEventSearch("");
    }

    void firebaseEventSearch(final String searchText){
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
                firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            if(data.child("name").getValue().toString().equals(model.getName())){
                                eventIDs[0] = data.getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),eventIDs[0],Toast.LENGTH_SHORT).show();
                        //Intent eventActivityIntent = new Intent(getApplicationContext(), EventActivity.class);
                        //eventActivityIntent.putExtra("event_id", eventIDs);
                        //eventActivityIntent.putExtra("event_name", model.getName());
                        //eventActivityIntent.putExtra("event_date", model.getDate());
                        //eventActivityIntent.putExtra("event_place", model.getPlace());
                        //eventActivityIntent.putExtra("event_desc", model.getDesc());
                        //eventActivityIntent.putExtra("event_loc", model.getLoc());
                        //eventActivityIntent.putExtra("event_image_url", model.getUrl());
                        //startActivity(eventActivityIntent);
                    }
                });
            }


        };

        results.setAdapter(adapter);
        adapter.startListening();
    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
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

}
