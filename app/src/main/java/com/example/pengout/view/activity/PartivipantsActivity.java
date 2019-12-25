package com.example.pengout.view.activity;

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
import com.example.pengout.model.User;
import com.example.pengout.view.fragment.UserResultsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PartivipantsActivity extends AppCompatActivity {

    private RecyclerView countList;
    private Toolbar countToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef, registeredRef;

    private String currentEventId;

    private int pos;

    FirebaseRecyclerAdapter<User, CountAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partivipants);

        countList = findViewById(R.id.count_events_list);
        countList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        currentEventId = (String) getIntent().getExtras().get("event_id");

        countToolbar = findViewById(R.id.count_events_toolbar);
        setSupportActionBar(countToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Participants");

        registeredRef = FirebaseDatabase.getInstance().getReference().child("registered");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(registeredRef.child(currentEventId), User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, CountAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CountAdapter holder, int position, @NonNull User model) {
                final String userId = getRef(position).getKey();
//                userRef.child(userId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String image_url = "";
//                        String name_ = "";
//                        String city_ = "";
//
//                        if (dataSnapshot.hasChild("image")){
//                            image_url = (String) dataSnapshot.child("image").getValue();
//                            name_ = (String) dataSnapshot.child("name").getValue();
//                            city_ = (String) dataSnapshot.child("address").getValue();
//                            Picasso.get().load(image_url).placeholder(R.drawable.profile_image).into(holder.imageView);
//                        }
//                        else {
//                            name_ = (String) dataSnapshot.child("name").getValue();
//                            city_ = (String) dataSnapshot.child("address").getValue();
//                        }
//
//                        holder.name.setText(name_);
//                        holder.city.setText(city_);
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

                registeredRef.child(currentEventId).child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String[] imageUrl = new String[1];
                        final String[] name = new String[1];
                        final String[] city = new String[1];

                        userRef.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("image")){
                                    imageUrl[0] = (String) dataSnapshot.child("image").getValue();
                                    name[0] = (String) dataSnapshot.child("name").getValue();
                                    city[0] = (String) dataSnapshot.child("address").getValue();
                                    Picasso.get().load(imageUrl[0]).placeholder(R.drawable.profile_image).into(holder.imageView);
                                }
                                else {
                                    name[0] = (String) dataSnapshot.child("name").getValue();
                                    city[0] = (String) dataSnapshot.child("address").getValue();
                                }

                                holder.name.setText(name[0]);
                                holder.city.setText(city[0]);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public CountAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_display, viewGroup, false);
                CountAdapter viewHolder = new CountAdapter(view);
                return  viewHolder;
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
        countList.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static class CountAdapter extends RecyclerView.ViewHolder{
        TextView name, city;
        ImageView imageView;


        public CountAdapter(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.user_name);
            city = itemView.findViewById(R.id.city);
            imageView = itemView.findViewById(R.id.users_search_image);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        countList.smoothScrollToPosition(pos);
    }
}
