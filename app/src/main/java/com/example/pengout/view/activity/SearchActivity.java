package com.example.pengout.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.pengout.R;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.example.pengout.view.adapter.SearchTabsAdapter;
import com.example.pengout.view.fragment.EventResultsFragment;
import com.example.pengout.view.fragment.UserResultsFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class SearchActivity extends AppCompatActivity {

    //private ImageView music,sport,edu,theatre;
    private ImageButton searchButton;
    private EditText searchText;
    private RecyclerView results;
    private DatabaseReference mEventDatabase;
    private Toolbar mToolbar;
    private LinearLayout emptyView;

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


        setupBottomNavigationView();

        mEventDatabase = FirebaseDatabase.getInstance().getReference("eventWithDesc");

        searchButton = findViewById(R.id.search_btn);
        searchText = findViewById(R.id.search_field);

        viewPager = findViewById(R.id.search_tabs_pager);
        searchTabsAdapter = new SearchTabsAdapter(getSupportFragmentManager());
        eventResultsFragment = new EventResultsFragment();
        userResultsFragment = new UserResultsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_tabs_pager,eventResultsFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.search_tabs_pager,userResultsFragment).commit();
        viewPager.setAdapter(searchTabsAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Search");


    }



    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
