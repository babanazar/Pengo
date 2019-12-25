package com.example.pengout.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.pengout.R;

public class SplashScreenActivity2 extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds
    ImageView imageanim;
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        imageanim = findViewById(R.id.imageanim);
        animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
        animation.setOneShot(true);//If true, the animation will only run a single time

                imageanim.setImageDrawable(animation);
        animation.start();

        imageanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation = new AnimationDrawable();
                animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled1), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled2), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled3), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled4), 500);
                animation.addFrame(getResources().getDrawable(R.drawable.untitled5), 500);
                animation.setOneShot(true);//If true, the animation will only run a
                        imageanim.setImageDrawable(animation);
                animation.start();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashScreenActivity2.this, RegisterActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
    /** Called when the activity is first created. */

}