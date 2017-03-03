package com.cureinstant.cureinstant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lokeshsaini94 on 20-02-2017.
 */

public class Utilities {

    public static final String PREFS_NAME = "LOGIN_PREFS";
    public static final String PREFS_KEY = "LOGIN_PREFS_String";

    public static void loggedInBool(Context context, Boolean b) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(PREFS_KEY, b);
        editor.commit();
    }

    public static Boolean isLoggedIn(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(PREFS_KEY, false);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}
