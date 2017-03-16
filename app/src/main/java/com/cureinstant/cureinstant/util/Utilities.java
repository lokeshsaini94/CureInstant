package com.cureinstant.cureinstant.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lokeshsaini94 on 20-02-2017.
 */

public class Utilities {

    public static final String prefName = "prefName";
    public static final String loginBoolKey = "loginBoolKey";
    public static final String accessTokenKey = "accessTokenKey";
    public static final String refreshTokenKey = "refreshTokenKey";
    public static final String profilePicBaseUrl = "http://www.cureinstant.com/profile_pics/";
    public static final String profilePicSmallBaseUrl = "http://www.cureinstant.com/profile_pics_avatar/";
    public static String accessTokenValue;
    public static String refreshTokenValue;
    public static String pageData = "";

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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
