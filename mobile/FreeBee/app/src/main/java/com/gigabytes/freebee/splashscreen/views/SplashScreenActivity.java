package com.gigabytes.freebee.splashscreen.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gigabytes.freebee.homescreen.views.activities.HomeScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_SCREEN_ACTIVITY_DELAY = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_ACTIVITY_DELAY);
    }
}
