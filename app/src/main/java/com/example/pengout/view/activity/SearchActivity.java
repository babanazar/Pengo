package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.example.pengout.view.adapter.SearchTabsAdapter;
import com.example.pengout.view.fragment.EventResultsFragment;
import com.example.pengout.view.fragment.UserResultsFragment;
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

    Query firebaseSearchQuery;

    private ViewPager viewPager;
    SearchTabsAdapter searchTabsAdapter;
    EventResultsFragment eventResultsFragment;
    UserResultsFragment userResultsFragment;
    TabLayout tabLayout;

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
        //results = findViewById(R.id.results);
        //results.setLayoutManager(new LinearLayoutManager(this));

        viewPager = findViewById(R.id.search_tabs_pager);
        searchTabsAdapter = new SearchTabsAdapter(getSupportFragmentManager());
        eventResultsFragment = new EventResultsFragment();
        userResultsFragment = new UserResultsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_tabs_pager,eventResultsFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_tabs_pager,userResultsFragment).commit();
        viewPager.setAdapter(searchTabsAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Glide.with(getApplicationContext()).load("https://cdn2.allevents.in/thumbs/thumb5dbb7cb95ea77.jpg").override(200,200).centerCrop().into(edu);
        //Glide.with(getApplicationContext()).load(R.drawable.theatre).override(200,200).into(theatre);
        //Glide.with(getApplicationContext()).load(R.drawable.sport_logo).override(200,200).into(sport);
        //Glide.with(getApplicationContext()).load(R.drawable.music).override(200,200).into(music);


    }



    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }




}
