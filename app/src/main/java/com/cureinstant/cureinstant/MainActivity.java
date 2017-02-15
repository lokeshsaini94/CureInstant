package com.cureinstant.cureinstant;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cureinstant.cureinstant.fragments.AppointmentFragment;
import com.cureinstant.cureinstant.fragments.NotificationFragment;
import com.cureinstant.cureinstant.fragments.ProfileFragment;
import com.cureinstant.cureinstant.fragments.ReadFragment;
import com.cureinstant.cureinstant.helper.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private BottomNavigationView bottomNavigationView;
    private int bottomNavSelectedItem;

    private Fragment readFragment, appointmentFragment, notificationFragment, profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        } else {
            MenuItem homeItem = bottomNavigationView.getMenu().getItem(0);
            if (bottomNavSelectedItem != homeItem.getItemId()) {
                // select home item
                selectFragment(homeItem);
                return;
            } else {
                super.onBackPressed();
            }
        }

        super.onBackPressed();
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar_layout);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear));

            isSearchOpened = true;
        }
    }

    private void doSearch() {
        edtSeach.setText("");
        isSearchOpened = true;
        handleMenuSearch();
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
            case R.id.action_notification:
                // Action to perform when Bag Menu item is selected.
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                }
                pushFragment(notificationFragment);
                break;
            case R.id.action_profile:
                // Action to perform when Account Menu item is selected.
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                }
                pushFragment(profileFragment);
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
}
