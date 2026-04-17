package com.example.sunrise;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView sun = findViewById(R.id.sun);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.sun_rise);
        sun.startAnimation(animation);

        ImageView hourImageView = findViewById(R.id.hour_hand);

        Animation hourAnimation =
                AnimationUtils.loadAnimation(this, R.anim.hour_turn);

        hourImageView.startAnimation(hourAnimation);
    }
}