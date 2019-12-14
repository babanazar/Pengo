package com.example.pengout.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.example.pengout.R;
import com.example.pengout.utils.FragmentChangeListener;
import com.example.pengout.view.fragment.InfoFragment1;
import com.example.pengout.view.fragment.InfoFragment2;
import com.example.pengout.view.fragment.InfoFragment3;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InformationActivity extends FragmentActivity implements FragmentChangeListener {


    //our database reference object
    DatabaseReference databaseUsers;

    InfoFragment1 fragment1;
    FragmentManager manager;
    FragmentTransaction transaction;

    public ArrayList<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //getting the reference of artists node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        fragment1 = new InfoFragment1();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.container_info,fragment1,"Fragment first").commit();
    }


    @Override
    public void goFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_info, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
