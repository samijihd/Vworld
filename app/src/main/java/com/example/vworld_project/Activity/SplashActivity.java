package com.example.vworld_project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.vworld_project.R;

public class SplashActivity extends AppCompatActivity {

    // boolean variable to record the state of the splash screen
    private static boolean splashLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make app waiting for a few seconds then change boolean to true
        if (!splashLoaded) {
            //bind splash screen
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 500);

            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(SplashActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
