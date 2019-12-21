package com.example.pengout.view.fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.FragmentChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InfoFragment1 extends Fragment {

    Button nextButton;
    EditText userName, userAge;
    Spinner userGender, userCity;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info1,container,false);
        nextButton = view.findViewById(R.id.nextButton);
        userAge = view.findViewById(R.id.editTextAge);
        userName = view.findViewById(R.id.editTextUserName);
        userCity = view.findViewById(R.id.spinnerCity);
        userGender = view.findViewById(R.id.spinnerGender);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
                nextFragment();
            }
        });
        return view;
    }

    void nextFragment(){
        InfoFragment2 fragment2 = new InfoFragment2();
        FragmentChangeListener fcl = (FragmentChangeListener)getActivity();
        fcl.goFragment(fragment2);
    }

    private void updateSettings() {

        String currentUserID = mAuth.getUid();

        String setName = userName.getText().toString();
        String setAge = userAge.getText().toString();
        String setGender = userGender.getSelectedItem().toString();
        String setCity = userCity.getSelectedItem().toString();
        if (TextUtils.isEmpty(setName)) {
            Toast.makeText(getContext(), "Please enter your name first...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setAge)) {
            Toast.makeText(getContext(), "Please enter your email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setCity)) {
            Toast.makeText(getContext(), "Please enter your address...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setGender)) {
            Toast.makeText(getContext(), "Please select your gender...", Toast.LENGTH_SHORT).show();
        }

        else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setName);
            profileMap.put("age", setAge);
            profileMap.put("address", setCity);
            profileMap.put("gender", setGender);

            usersRef.child(currentUserID).updateChildren(profileMap);

        }


    }
}

