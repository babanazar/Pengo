package com.example.pengout.view.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pengout.R;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.example.pengout.view.adapter.PostedAndJoinedTabsAccessorAdapter;
import com.example.pengout.view.fragment.JoinedFragment;
import com.example.pengout.view.fragment.PostedFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class PostedAndJoinedActivity extends AppCompatActivity {

    private Context mContext = PostedAndJoinedActivity.this;

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTablayout;

    private PostedAndJoinedTabsAccessorAdapter postedAndJoinedTabsAccessorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_and_joined);

        mToolbar = findViewById(R.id.posted_joined_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = Integer.parseInt(String.valueOf(getIntent().getExtras().get("position")));

        myViewPager = findViewById(R.id.post_join_tabs_pager);
        postedAndJoinedTabsAccessorAdapter = new PostedAndJoinedTabsAccessorAdapter(getSupportFragmentManager());

        getSupportFragmentManager().beginTransaction().replace(R.id.post_join_tabs_pager, new PostedFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.post_join_tabs_pager,new JoinedFragment()).commit();
        myViewPager.setAdapter(postedAndJoinedTabsAccessorAdapter);
        myViewPager.setCurrentItem(position);

        myTablayout = findViewById(R.id.posted_and_joined_tabs);
        myTablayout.setupWithViewPager(myViewPager);

    }


}
