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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Event;
import com.example.pengout.model.User;
import com.example.pengout.view.activity.EventActivity;
import com.example.pengout.view.activity.ProfileActivity;
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

public class UserResultsFragment extends Fragment {

    FirebaseRecyclerAdapter<User, UsersViewHolder> adapter;
    Query firebaseSearchQuery;

    private RecyclerView results;
    private ImageButton searchButton;
    private EditText searchText;
    private DatabaseReference mEventDatabase;

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_userres, container, false);
        results = root.findViewById(R.id.user_results);
        results.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventDatabase = FirebaseDatabase.getInstance().getReference("users");

        searchButton = root.findViewById(R.id.search_btn);
        searchText = root.findViewById(R.id.search_field);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String st = searchText.getText().toString();
                firebaseUserSearch(st);
            }
        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseUserSearch("");
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView n,c;
        ImageView imageView;
        public UsersViewHolder(View itemView){
            super(itemView);
            n = itemView.findViewById(R.id.user_name);
            c = itemView.findViewById(R.id.city);
            imageView = itemView.findViewById(R.id.users_search_image);

        }
    }


    public void firebaseUserSearch(final String searchText){
        firebaseSearchQuery = mEventDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearchQuery,User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(options) {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_display,viewGroup,false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull final User model) {
                holder.n.setText(model.getName());
                holder.c.setText(model.getAddress());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.imageView);
                final String[] userIDs = new String[1];
                firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            ArrayList<String> loc = new ArrayList<>();
                            if(data.child("name").getValue().equals(model.getName())){
                                userIDs[0] = data.getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(),userIDs[0],Toast.LENGTH_SHORT).show();
                        Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", userIDs[0]);
                        startActivity(profileIntent);
                    }
                });
            }


        };

        results.setAdapter(adapter);
        adapter.startListening();
    }
}
