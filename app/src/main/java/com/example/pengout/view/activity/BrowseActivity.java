package com.example.pengout.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.BottomNavigationViewHelper;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BrowseActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private static final int ACTIVITY_NUM = 1;
    Button search,art,entertainment,business,health,sport,workshop,other,trip,education;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Browse");
        search = findViewById(R.id.searchbutton);
        art = findViewById(R.id.art2);
        entertainment = findViewById(R.id.entertainment2);
        business = findViewById(R.id.business2);
        health = findViewById(R.id.health2);
        sport = findViewById(R.id.sport2);
        workshop = findViewById(R.id.workshop2);
        other = findViewById(R.id.other);
        trip = findViewById(R.id.trip2);
        education = findViewById(R.id.education2);


        setClickListeners();

        setupBottomNavigationView();

    }



    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav_view_ex);
        BottomNavigationViewHelper.setupBottonNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(BrowseActivity.this, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    void setClickListeners(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("image",R.drawable.art2);
                intent.putExtra("n","Art");
                intent.putExtra("name","art");
                startActivity(intent);
            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Education");
                intent.putExtra("name","education");
                intent.putExtra("image",R.drawable.education2);
                startActivity(intent);
            }
        });
        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Entertainment");
                intent.putExtra("name","entertainment");
                intent.putExtra("image",R.drawable.entertainment2);
                startActivity(intent);
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Health");
                intent.putExtra("name","health-wellness");
                intent.putExtra("image",R.drawable.health2);
                startActivity(intent);
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Business");
                intent.putExtra("name","business");
                intent.putExtra("image",R.drawable.business2);
                startActivity(intent);
            }
        });
        workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Workshop");
                intent.putExtra("name","workshops");
                intent.putExtra("image",R.drawable.workshop2);
                startActivity(intent);
            }
        });
        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Trip");
                intent.putExtra("name","trip-adventures");
                intent.putExtra("image",R.drawable.trip2);
                startActivity(intent);
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Sports");
                intent.putExtra("name","sports");
                intent.putExtra("image",R.drawable.sport2);
                startActivity(intent);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowseActivity.this,CategoryActivity.class);
                intent.putExtra("n","Others");
                intent.putExtra("name","other");
                intent.putExtra("image",R.drawable.penguin);
                startActivity(intent);
            }
        });

    }
}
