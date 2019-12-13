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

//    Button buttonGoChat;

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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.find_friends:
                sendUserToFindFriendsActivity();
                return true;

            case R.id.create_event:
                sendUserToCreateEventActivity();
//                finish();
                return true;

            case R.id.settings:
                sendUserToSettingsActivity();
                return true;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                finish();
                return true;
        }
        return false;
    }

    private void sendUserToCreateEventActivity() {
        Intent createEventIntent = new Intent(HomeActivity.this, CreateEventActivity.class);
        startActivity(createEventIntent);
//        finish();
    }

    private void sendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(HomeActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
//        finish();
    }

    private void sendUserToSettingsActivity() {
        Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
//        finish();
    }

}
