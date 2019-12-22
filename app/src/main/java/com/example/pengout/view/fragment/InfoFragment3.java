package com.example.pengout.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.view.activity.HomeActivity;
import com.example.pengout.view.activity.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoFragment3 extends Fragment {

    Button ready;
    Button art, business, education, entertainment, health, sport, trip, workshop;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private boolean isPressedArt, isPressedBusiness, isPressedEducation, isPressedEntertainment,
            isPressedHealth, isPressedSport, isPressedTrip, isPressedWorkshop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info3, container, false);
        ready = view.findViewById(R.id.button_ready);
        art = view.findViewById(R.id.art2);
        business = view.findViewById(R.id.business2);
        education = view.findViewById(R.id.education2);
        entertainment = view.findViewById(R.id.entertainment2);
        health = view.findViewById(R.id.health2);
        sport = view.findViewById(R.id.sport2);
        trip = view.findViewById(R.id.trip2);
        workshop = view.findViewById(R.id.workshop2);
        isPressedArt = false;
        isPressedBusiness = false;
        isPressedEducation = false;
        isPressedEntertainment = false;
        isPressedHealth = false;
        isPressedSport = false;
        isPressedTrip = false;
        isPressedWorkshop = false;

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                art.setBackgroundResource(isPressedArt ? R.drawable.art2 : R.drawable.check);
                isPressedArt = !isPressedArt;
            }
        });


        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                business.setBackgroundResource(isPressedBusiness ? R.drawable.business2 : R.drawable.check);
                isPressedBusiness = !isPressedBusiness;
            }

        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education.setBackgroundResource(isPressedEducation ? R.drawable.education2 : R.drawable.check);
                isPressedEducation = !isPressedEducation;

            }
        });
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entertainment.setBackgroundResource(isPressedEntertainment ? R.drawable.entertainment2 : R.drawable.check);
                isPressedEntertainment = !isPressedEntertainment;

            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                health.setBackgroundResource(isPressedHealth ? R.drawable.health2 : R.drawable.check);
                isPressedHealth = !isPressedHealth;

            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sport.setBackgroundResource(isPressedSport ? R.drawable.sport2 : R.drawable.check);
                isPressedSport = !isPressedSport;

            }
        });
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trip.setBackgroundResource(isPressedTrip ? R.drawable.trip2 : R.drawable.check);
                isPressedTrip = !isPressedTrip;

            }
        });
        workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workshop.setBackgroundResource(isPressedWorkshop ? R.drawable.workshop2 : R.drawable.check);
                isPressedWorkshop = !isPressedWorkshop;

            }
        });
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getUserInterests() {

        HashMap interests = new HashMap();
        if (isPressedArt) {
            interests.put("Art", true);
        }
        if (isPressedBusiness) {
            interests.put("Business", true);
        }
        if (isPressedEducation) {
            interests.put("Education", true);
        }
        if (isPressedEntertainment) {
            interests.put("Entertainment", true);
        }
        if (isPressedHealth) {
            interests.put("Health", true);
        }
        if (isPressedSport) {
            interests.put("Sport", true);
        }
        if (isPressedTrip) {
            interests.put("Trip", true);
        }
        if (isPressedWorkshop) {
            interests.put("Workshop", true);
        }


        rootRef.child("users").child(currentUserID).child("interests").updateChildren(interests)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                                sendUserToMyProfileActivity();
                            Toast.makeText(getContext(), "Your Interests are saved...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(getContext(), "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}
