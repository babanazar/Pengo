package com.example.pengout.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pengout.R;
import com.example.pengout.model.Match;
import com.example.pengout.view.activity.ChatActivity;
import com.example.pengout.view.activity.HomeActivity;
import com.example.pengout.view.activity.PrivateChatActivity;
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

public class GroupsFragment extends Fragment {

    private View root;
    private RecyclerView matchList;

    private DatabaseReference matchRef, usersRef;

    private FirebaseAuth mAuth;
    String currentUserId;
    FirebaseRecyclerAdapter<Match, MatchViewHolder> adapter;

    public GroupsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_match,container,false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        matchRef = FirebaseDatabase.getInstance().getReference().child("matches");
        matchList = root.findViewById(R.id.match_list);
        matchList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Match> options =
                new FirebaseRecyclerOptions.Builder<Match>()
                    .setQuery(matchRef.child(currentUserId), Match.class)
                    .build();
        adapter = new FirebaseRecyclerAdapter<Match, MatchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MatchViewHolder holder, int position, @NonNull Match model) {
                final String matchUserId = getRef(position).getKey();
                matchRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        if(matchUserId == null){
                            return;
                        }
                        final String imageUrl = (String) dataSnapshot.child(matchUserId).child("image").getValue();
                        String eventName = (String) dataSnapshot.child(matchUserId).child("eventName").getValue();
                        final String userName = (String) dataSnapshot.child(matchUserId).child("userName").getValue();
                        if(!(dataSnapshot.child(matchUserId).hasChild("status")))
                            return;
                        if(dataSnapshot.child(matchUserId).child("status").getValue().equals("accepted")){
                            holder.eventName.setText(eventName);
                            holder.userName.setText(userName);
                            holder.itemView.findViewById(R.id.match_accept_btn).setVisibility(View.VISIBLE);
                            holder.itemView.findViewById(R.id.match_cancel_btn).setVisibility(View.VISIBLE);
                            if(!imageUrl.equals(""))
                                Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.profileImage);
                            holder.cancelButton.setText("Delete");
                            holder.acceptButton.setText("WAITING");
                            holder.acceptButton.setBackgroundColor(Color.BLUE);
                            holder.acceptButton.setClickable(false);
                            return;
                        }

                        if( dataSnapshot.child(matchUserId).child("status").getValue().equals("matched")){
                            holder.eventName.setText(eventName);
                            holder.userName.setText(userName);
                            if(!imageUrl.equals(""))
                                Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.profileImage);
                            holder.itemView.findViewById(R.id.match_cancel_btn).setVisibility(View.GONE);
                            holder.itemView.findViewById(R.id.match_accept_btn).setVisibility(View.VISIBLE);
                            holder.acceptButton.setText("MATCHED");
                            holder.acceptButton.setBackgroundColor(Color.GRAY);
                            holder.acceptButton.setClickable(false);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent privateChatIntent = new Intent(getContext(), PrivateChatActivity.class);
                                    privateChatIntent.putExtra("visit_user_id", matchUserId);
                                    privateChatIntent.putExtra("visit_user_name", userName);
                                    privateChatIntent.putExtra("visit_image", imageUrl);
                                    startActivity(privateChatIntent);
                                }
                            });
                            return;
                        }


                        holder.eventName.setText(eventName);
                        holder.userName.setText(userName);
                        holder.itemView.findViewById(R.id.match_accept_btn).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.match_cancel_btn).setVisibility(View.VISIBLE);
                        if(!imageUrl.equals(""))
                            Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(holder.profileImage);

                        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                matchRef.child(currentUserId).child(matchUserId).child("status").setValue("accepted");
                                matchRef.child(matchUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(currentUserId)){
                                            if( dataSnapshot.child(currentUserId).child("status").getValue().equals("accepted")){
                                                matchRef.child(currentUserId).child(matchUserId).child("status").setValue("matched");
                                                matchRef.child(matchUserId).child(currentUserId).child("status").setValue("matched");
                                                //usersRef.child(currentUserId).child("friends").child(matchUserId).setValue(System.currentTimeMillis());
                                                //usersRef.child(currentUserId).child("friends").child(matchUserId).setValue(System.currentTimeMillis());

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                            }
                        });
                        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                matchRef.child(currentUserId).child(matchUserId).removeValue();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

            }

            @NonNull
            @Override
            public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_item,viewGroup,false);
                return new MatchViewHolder(view);
            }
        };

        matchList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder{

        TextView eventName, userName;
        CircleImageView profileImage;
        Button acceptButton, cancelButton;
        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            userName = itemView.findViewById(R.id.user_name);
            profileImage = itemView.findViewById(R.id.profile_image);
            acceptButton = itemView.findViewById(R.id.match_accept_btn);
            cancelButton = itemView.findViewById(R.id.match_cancel_btn);
        }
    }
}
