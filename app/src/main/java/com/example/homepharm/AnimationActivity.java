package com.example.homepharm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {

    private TextView textView;
    private static final long SPLASH_DURATION = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        textView = findViewById(R.id.textView);
        textView.setVisibility(TextView.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.fade_in);
                textView.startAnimation(fadeIn);
                textView.setVisibility(TextView.VISIBLE);
            }
        }, SPLASH_DURATION - 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToNextScreen();
            }
        }, SPLASH_DURATION);
    }

    private void navigateToNextScreen() {
        SharedPreferences sharedPreferences = getSharedPreferences("HomePharmPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(AnimationActivity.this, Category.class);
        } else {
            intent = new Intent(AnimationActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
