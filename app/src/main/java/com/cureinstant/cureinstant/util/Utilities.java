package com.cureinstant.cureinstant.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lokeshsaini94 on 20-02-2017.
 */

public class Utilities {

    public static final String prefName = "prefName";
    public static final String loginBoolKey = "loginBoolKey";
    public static final String accessTokenKey = "accessTokenKey";
    public static final String refreshTokenKey = "refreshTokenKey";
    public static String accessTokenValue;
    public static String refreshTokenValue;

    public static final String profilePicBaseUrl = "http://cureinstant.com/profile_pics/";
    public static final String profilePicSmallBaseUrl = "http://cureinstant.com/profile_pics_avatar/";

    public static void loggedInBool(Context context, Boolean b) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(loginBoolKey, b);
        editor.commit();
    }

    public static Boolean isLoggedIn(Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE).getBoolean(loginBoolKey, false);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}
