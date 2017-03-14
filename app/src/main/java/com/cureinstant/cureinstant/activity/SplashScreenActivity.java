package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 2000;
    public Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        Utilities.loggedInBool(this, false);

        if (Utilities.isLoggedIn(this)) {
            i = new Intent(SplashScreenActivity.this, MainActivity.class);
        } else {
            i = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
