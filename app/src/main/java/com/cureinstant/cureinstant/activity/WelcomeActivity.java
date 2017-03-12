package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    BubblePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        picker = (BubblePicker) findViewById(R.id.picker);

        final String[] titles = getResources().getStringArray(R.array.bodyParts);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker.setItems(new ArrayList<PickerItem>() {{
            for (int i = 0; i < titles.length; ++i) {
                add(new PickerItem(titles[i], colors.getColor((i * 2) % 8, 0),
                        ContextCompat.getColor(WelcomeActivity.this, android.R.color.white),
                        ContextCompat.getDrawable(WelcomeActivity.this, images.getResourceId(i, 0))));
            }
        }});

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
        if (Utilities.isLoggedIn(getApplicationContext())) {
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_button:
                Intent login = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.signup_button:
                Intent signup = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(signup);
                break;
        }

    }
}
