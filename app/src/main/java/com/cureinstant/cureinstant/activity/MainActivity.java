package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.fragment.AppointmentFragment;
import com.cureinstant.cureinstant.fragment.MoreFragment;
import com.cureinstant.cureinstant.fragment.ReadFragment;
import com.cureinstant.cureinstant.helper.BottomNavigationViewHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenKey;
import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;
import static com.cureinstant.cureinstant.util.Utilities.refreshTokenKey;
import static com.cureinstant.cureinstant.util.Utilities.refreshTokenValue;
import static com.cureinstant.cureinstant.util.Utilities.trimCache;

public class MainActivity extends AppCompatActivity {

    private static int currentVisibleFrag = 1;

    private BottomNavigationView bottomNavigationView;
    private int bottomNavSelectedItem;

    private ReadFragment readFragment;
    private AppointmentFragment appointmentFragment;
    private MoreFragment moreFragment;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Restore fragments if its instance already exists
        if (savedInstanceState != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                if (readFragment == null) {
                    readFragment = (ReadFragment) fragmentManager.findFragmentByTag("readFragment");
                }
                if (appointmentFragment == null) {
                    appointmentFragment = (AppointmentFragment) fragmentManager.findFragmentByTag("appointmentFragment");
                }
                if (moreFragment == null) {
                    moreFragment = (MoreFragment) fragmentManager.findFragmentByTag("moreFragment");
                }
            }
        }

        // Fetches accessToken from sharedPreference
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        accessTokenValue = preferences.getString(accessTokenKey, "");
        refreshTokenValue = preferences.getString(refreshTokenKey, "");
        Log.e("MainActivity", "onCreate: accessTokenValue " + accessTokenValue);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(toolbar);

        setupFragments();

        setupNavigationView();
    }

    //Fires after the OnStop() state
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bottomNavigationView.getMenu().getItem(0);
        if (bottomNavSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
            return;
        } else {
            super.onBackPressed();
        }

        super.onBackPressed();
    }

    /**
     * Setup Bottom Navigation View and set item selected listener to it
     */
    private void setupNavigationView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            selectFragment(item);
                            return false;
                        }
                    });
        }
    }

    /**
     * Setup Fragments and attach them to their containers
     */
    private void setupFragments() {
        if (readFragment == null) {
            readFragment = new ReadFragment();
        }
        if (appointmentFragment == null) {
            appointmentFragment = new AppointmentFragment();
        }
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.read_fragment_container, readFragment, "readFragment");
                ft.replace(R.id.appointment_fragment_container, appointmentFragment, "appointmentFragment");
                ft.replace(R.id.more_fragment_container, moreFragment, "moreFragment");
                ft.commit();
            }
        }
    }

    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);
        bottomNavSelectedItem = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_read:
                // Action to perform when Home Menu item is selected.
                if (currentVisibleFrag == 1) {
                    readFragment.refreshFeedFrag();
                }
                setFragmentsVisibility(1);
                break;
            case R.id.action_appointments:
                // Action to perform when Bag Menu item is selected.
                setFragmentsVisibility(2);
                break;
            case R.id.action_more:
                // Action to perform when Account Menu item is selected.
                setFragmentsVisibility(3);
                break;
        }
        Bundle params = new Bundle();
        params.putString("fragment_selected", String.valueOf(item.getItemId()));
        mFirebaseAnalytics.logEvent("main_navigation", params);
    }

    // Show and animate fragment that is selected and hide other fragments
    private void setFragmentsVisibility(int i) {
        View readFragmentContainer = findViewById(R.id.read_fragment_container);
        View appointmentFragmentContainer = findViewById(R.id.appointment_fragment_container);
        View moreFragmentContainer = findViewById(R.id.more_fragment_container);

        if (i == 0) {
            readFragmentContainer.setVisibility(View.GONE);
            appointmentFragmentContainer.setVisibility(View.GONE);
            moreFragmentContainer.setVisibility(View.GONE);
        } else if (i == 1) {
            readFragmentContainer.setVisibility(View.VISIBLE);
            appointmentFragmentContainer.setVisibility(View.GONE);
            moreFragmentContainer.setVisibility(View.GONE);
            readFragmentContainer
                    .animate()
                    .alpha(1.0f)
                    .setDuration(300);
            currentVisibleFrag = 1;
        } else if (i == 2) {
            readFragmentContainer.setVisibility(View.GONE);
            appointmentFragmentContainer.setVisibility(View.VISIBLE);
            moreFragmentContainer.setVisibility(View.GONE);
            appointmentFragmentContainer
                    .animate()
                    .alpha(1.0f)
                    .setDuration(300);
            currentVisibleFrag = 2;
        } else if (i == 3) {
            readFragmentContainer.setVisibility(View.GONE);
            appointmentFragmentContainer.setVisibility(View.GONE);
            moreFragmentContainer.setVisibility(View.VISIBLE);
            moreFragmentContainer
                    .animate()
                    .alpha(1.0f)
                    .setDuration(300);
            currentVisibleFrag = 3;
        }
    }
}
