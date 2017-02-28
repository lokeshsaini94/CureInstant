package com.cureinstant.cureinstant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static File createNewRecordFile() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + R.string.app_name + "/" + "Records" + "/";
        String fileName = "Record" + (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale
                .getDefault())).format(new Date());
        File file = new File(path + fileName + ".jpeg");
        file.getParentFile().mkdirs(); //if the folder doesn't exists it's created
        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.d("Utilities", "IOException: " + e);
        }
        return file;
    }
}
