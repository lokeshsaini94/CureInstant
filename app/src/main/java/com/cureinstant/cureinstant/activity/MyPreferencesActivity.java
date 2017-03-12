package com.cureinstant.cureinstant.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cureinstant.cureinstant.R;

/**
 * Created by lokeshsaini94 on 09-03-2017.
 */

public class MyPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        AppBarLayout appBarLayout = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.layout_settings_toolbar, root, false);
        root.addView(appBarLayout, 0); // insert at top

        Toolbar bar = (Toolbar) appBarLayout.findViewById(R.id.toolbar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
