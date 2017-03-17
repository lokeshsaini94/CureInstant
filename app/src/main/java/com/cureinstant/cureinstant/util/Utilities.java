package com.cureinstant.cureinstant.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

    // Returns days, hours, minutes, seconds from a give date string
    public static long[] getDateDifference(String postDate) throws ParseException {

        // Makes date object with UTC
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Makes date object with current timezone
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        // Makes a date object with current date and time
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        Date currentDate = dateFormatter.parse(c.get(Calendar.YEAR) + "-" +
                month + "-" +
                c.get(Calendar.DAY_OF_MONTH) + " " +
                c.get(Calendar.HOUR) + ":" +
                c.get(Calendar.MINUTE) + ":" +
                c.get(Calendar.SECOND));
        Date postdate = (Date)dateFormatter.parse(dt);

        // Gives difference b/w two dates in milliseconds
        long different = currentDate.getTime() - postdate.getTime();

        // Converts difference to days, hours, minutes, seconds
        long diffInDays = TimeUnit.MILLISECONDS.toDays(different);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(different);
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(different);
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(different);

        return new long[]{diffInDays, diffInHours, diffInMin, diffInSec};
    }
}
