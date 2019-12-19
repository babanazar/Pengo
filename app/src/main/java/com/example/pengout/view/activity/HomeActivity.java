package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.model.User;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.example.pengout.view.adapter.EtkinlikTabsAccessorAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private EtkinlikTabsAccessorAdapter myEtkinlikTabsAccessorAdapter;

    DatabaseReference databaseUsers;
    User currentUser;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String email;

    private Context mContext = HomeActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pengout");

        setupBottomNavigationView();

        myViewPager = findViewById(R.id.home_tabs_pager);
        myEtkinlikTabsAccessorAdapter = new EtkinlikTabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myEtkinlikTabsAccessorAdapter);
//        myTabsAccessorAdapter = new HomeTabsAccessorAdapter(getSupportFragmentManager());
//        myViewPager.setAdapter(myTabsAccessorAdapter);


        myTabLayout = findViewById(R.id.home_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email = firebaseUser.getEmail();
        Log.d("emeyil", email);


        //getting the reference of artists node
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");


    }

    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.go_to_chat_button) {
            // do something here
            Intent chatIntent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(chatIntent );

            Toast.makeText(mContext, "Wanna edit?", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
