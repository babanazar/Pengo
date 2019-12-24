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
import android.widget.TextView;

import com.example.pengout.R;
import com.example.pengout.model.Contacts;
import com.example.pengout.model.User;
import com.example.pengout.view.activity.ProfileActivity;
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
public class ContactsFragment extends Fragment {

    private View contactsView;
    private RecyclerView myContactsList;

    private DatabaseReference contactsRef, usersRef, friendsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public ContactsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactsView = inflater.inflate(R.layout.fragment_contacts, container, false);

        myContactsList = contactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(currentUserID);
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        friendsRef = usersRef.child(currentUserID).child("friends");

        return contactsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(friendsRef, User.class)
                        .build();

        FirebaseRecyclerAdapter<User, ContactsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, ContactsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull User model) {

                        final String userIDs = getRef(position).getKey();



                        friendsRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                usersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String userImage = "https://github.com/selimyagci/Pengo/blob/master/app/src/main/res/drawable-xhdpi/ic_user.png";
                                        String profileName;
                                        String profileAddress;

                                        if (dataSnapshot.hasChild("image")) {
                                            userImage = (String) dataSnapshot.child("image").getValue();
                                            profileName = (String) dataSnapshot.child("name").getValue();
                                            profileAddress = (String) dataSnapshot.child("address").getValue();

                                            holder.userName.setText(profileName);
                                            holder.userStatus.setText(profileAddress);
                                            Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                        } else {
                                            profileName = "unknown";
                                            if (dataSnapshot.hasChild("name"))
                                                profileName = (String)dataSnapshot.child("name").getValue();

                                            profileAddress = "no address";
                                            if (dataSnapshot.hasChild("name"))
                                                profileAddress = (String)dataSnapshot.child("address").getValue();

                                            holder.userName.setText(profileName);
                                            holder.userStatus.setText(profileAddress);
                                        }

                                        final String finalImage = userImage;
                                        final String finalName = profileName;
                                        String finalAdresse = profileAddress;


                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent otherProfile = new Intent(getContext(), ProfileActivity.class);
                                                otherProfile.putExtra("visit_user_id", userIDs);
                                                otherProfile.putExtra("visit_user_name", finalName);
                                                otherProfile.putExtra("visit_user_image", finalImage);
                                                startActivity(otherProfile);
                                            }
                                        });

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
                    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                        return viewHolder;
                    }

                };
        adapter.setHasStableIds(true);
        myContactsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
