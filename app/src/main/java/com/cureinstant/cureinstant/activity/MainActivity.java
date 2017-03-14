package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.fragment.AppointmentFragment;
import com.cureinstant.cureinstant.fragment.MoreFragment;
import com.cureinstant.cureinstant.fragment.NotificationFragment;
import com.cureinstant.cureinstant.fragment.ReadFragment;
import com.cureinstant.cureinstant.fragment.RecordsFragment;
import com.cureinstant.cureinstant.helper.BottomNavigationViewHelper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenKey;
import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;
import static com.cureinstant.cureinstant.util.Utilities.refreshTokenKey;
import static com.cureinstant.cureinstant.util.Utilities.refreshTokenValue;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private int bottomNavSelectedItem;

    private Fragment readFragment, appointmentFragment, recordFragment, moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        accessTokenValue = preferences.getString(accessTokenKey, "");
        refreshTokenValue = preferences.getString(refreshTokenKey, "");
        RequestData requestData = new RequestData();
        requestData.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(toolbar);

        setupNavigationView();
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
            case R.id.action_notification:
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                NotificationFragment frag = new NotificationFragment();
                frag.show(fragmentManager, "notif_frag");
                return true;
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
                if (readFragment == null) {
                    readFragment = new ReadFragment();
                }
                pushFragment(readFragment);
                break;
            case R.id.action_appointments:
                // Action to perform when Bag Menu item is selected.
                if (appointmentFragment == null) {
                    appointmentFragment = new AppointmentFragment();
                }
                pushFragment(appointmentFragment);
                break;
            case R.id.action_records:
                // Action to perform when Bag Menu item is selected.
                if (recordFragment == null) {
                    recordFragment = new RecordsFragment();
                }
                pushFragment(recordFragment);
                break;
            case R.id.action_more:
                // Action to perform when Account Menu item is selected.
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                }
                pushFragment(moreFragment);
                break;
        }
    }

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(android.support.v4.app.Fragment fragment) {
        if (fragment == null)
            return;

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

    private class RequestData extends AsyncTask<Void, Void, String> {

        View progress = findViewById(R.id.progress_container);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/get-user-dashboard")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                Log.e("SplashScreen", "doInBackground: " + accessTokenValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
        }
    }
}
