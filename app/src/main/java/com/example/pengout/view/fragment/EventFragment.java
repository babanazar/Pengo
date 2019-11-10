package com.example.pengout.view.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pengout.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EventFragment extends Fragment implements View.OnClickListener {

    private View root;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference eventsRef;


    private ImageView eventImage,eventShare,eventFavorite,eventSave;
    private TextView eventTitle, eventSubTitle;

    private AlphaAnimation alphaAnimation, alphaAnimationShowIcon;

    private boolean isSaved, isFavorited;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        root = inflater.inflate(R.layout.fragment_event,container, false);

        eventImage = root.findViewById(R.id.event_image);
        eventTitle = root.findViewById(R.id.event_title);
        eventSubTitle = root.findViewById(R.id.event_subtitle);
        eventShare = root.findViewById(R.id.event_share);
        eventFavorite = root.findViewById(R.id.event_favorite);
        eventSave = root.findViewById(R.id.event_save);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference().child("futureEvents");


        Glide.with(getContext()).load(R.drawable.login_photo).apply(new RequestOptions().fitCenter()).into(eventImage);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventSave.setOnClickListener(this);
        eventFavorite.setOnClickListener(this);
        eventShare.setOnClickListener(this);
        eventImage.setOnClickListener(this);

        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(700);
        eventImage.startAnimation(alphaAnimation);

        alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimationShowIcon.setDuration(500);
    }

    @Override
    public void onStart(){
        super.onStart();

/*
        eventsRef.child("-LqrTF2-n_wzJSmKX7v6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String eventName = dataSnapshot.child("date").getValue().toString();
                String eventDate= dataSnapshot.child("name").getValue().toString();
                String eventPlace= dataSnapshot.child("place").getValue().toString();
                String eventTime= dataSnapshot.child("time").getValue().toString();

                eventTitle.setText(eventName);
                eventSubTitle.setText(eventPlace + "\t" + eventDate + "\t" + eventTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.event_save:
                if(!isSaved){
                    eventSave.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    eventSave.startAnimation(alphaAnimationShowIcon);
                    isSaved = true;
                }else {
                    eventSave.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    eventSave.startAnimation(alphaAnimationShowIcon);
                    isSaved = false;
                }
                break;

            case R.id.event_favorite:
                if (!isFavorited) {
                    eventFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    eventFavorite.startAnimation(alphaAnimationShowIcon);
                    eventFavorite.startAnimation(alphaAnimationShowIcon);
                    isFavorited = true;
                } else {
                    eventFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    eventFavorite.startAnimation(alphaAnimationShowIcon);
                    eventFavorite.startAnimation(alphaAnimationShowIcon);
                    isFavorited = false;
                }
                break;

            case R.id.event_share:
                break;

            case R.id.event_image:
                eventImage.startAnimation(alphaAnimation);
                // TO DO //
                //Event Info fragmanına gider shared element olarak event image ile
                break;

        }

    }



}
