package com.example.pengout.view.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.Contacts;
import com.example.pengout.model.Event;
import com.example.pengout.model.User;
import com.example.pengout.view.activity.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class RequestsFragment extends Fragment {

    private View requestsFragmentView;
    private RecyclerView myRequestsList;

    private DatabaseReference  usersRef, contactsRef;

    private FirebaseAuth mAuth;
    private String currentUserID;
    private String receiverUserID;

    private String currentTimestamp;

    public RequestsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestsFragmentView = inflater.inflate(R.layout.fragment_requests, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts");
        currentTimestamp = String.valueOf(System.currentTimeMillis());

        myRequestsList = requestsFragmentView.findViewById(R.id.chat_requests_list);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return requestsFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(usersRef.child(currentUserID).child("received follow requests"), User.class)
                        .build();

        FirebaseRecyclerAdapter<User, RequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, RequestsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull User model) {


                        final String listUserId = getRef(position).getKey();

                        usersRef.child(currentUserID).child("received follow requests").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                        usersRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("image")) {
                                                    final String requestProfileImage = (String)dataSnapshot.child("image").getValue();
                                                    Picasso.get().load(requestProfileImage).into(holder.profileImage);

                                                }

                                                final String requestUserName = (String)dataSnapshot.child("name").getValue();
                                                final String requestUserStatus = (String)dataSnapshot.child("status").getValue();
                                                receiverUserID = (String)dataSnapshot.child("uid").getValue();
                                                holder.userName.setText(requestUserName);
                                                holder.userStatus.setText("wants to connect with you.");

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent otherProfile = new Intent(getContext(), ProfileActivity.class);
                                                        otherProfile.putExtra("visit_user_id", receiverUserID);
                                                        startActivity(otherProfile);

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        RequestsViewHolder holder = new RequestsViewHolder(view);
                        return holder;
                    }
                };
        adapter.setHasStableIds(true);
        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;

        public RequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);

        }
    }
}
