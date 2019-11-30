package com.example.pengout.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pengout.R;

public class CategoryActivity extends AppCompatActivity {

    private ImageView music,sport,edu,theatre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cards);
        music = findViewById(R.id.music);
        music.setAlpha(127);
        sport = findViewById(R.id.sport);
        sport.setAlpha(127);
        edu = findViewById(R.id.edu);
        edu.setAlpha(127);
        theatre = findViewById(R.id.the);
        theatre.setAlpha(127);

        Glide.with(getApplicationContext()).load("https://cdn2.allevents.in/thumbs/thumb5dbb7cb95ea77.jpg").override(200,200).centerCrop().into(edu);
        Glide.with(getApplicationContext()).load(R.drawable.theatre).override(200,200).into(theatre);
        Glide.with(getApplicationContext()).load(R.drawable.sport_logo).override(200,200).into(sport);
        Glide.with(getApplicationContext()).load(R.drawable.music).override(200,200).into(music);
    }

    void setOnClickListeners(){
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
