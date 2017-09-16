package com.cureinstant.cureinstant.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cureinstant.cureinstant.model.User;

/**
 * Created by lokesh on 9/11/2017.
 */

public class UserDbHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "userDatabase";
    // Provision data table names
    public static final String TABLE_USER = "userTable";
    // Provision data Table Columns names
    public static final String ID = "_id";
    public static final String COLUMN_ACCESS_TOKEN = "accessToken";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_PICTURE = "picture";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Table Create Statements
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + ID + " integer primary key autoincrement, "
            + COLUMN_ACCESS_TOKEN + " TEXT PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_SEX + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_NUMBER + " TEXT,"
            + COLUMN_DOB + " TEXT,"
            + COLUMN_PICTURE + " TEXT" + ")";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // Adding new User
    public void addUser(User user, String accessToken) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCESS_TOKEN, accessToken);
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_SEX, user.getSex());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_NUMBER, user.getNumber());
        values.put(COLUMN_DOB, user.getDob());
        values.put(COLUMN_PICTURE, user.getPicture());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    // Getting single User
    public User getUser(String accessToken) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + COLUMN_ACCESS_TOKEN + " = " + accessToken;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // return user
        return new User(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7));
    }

    // Updating User
    public int updateUser(User user, String accessToken) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCESS_TOKEN, accessToken);
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_SEX, user.getSex());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_NUMBER, user.getNumber());
        values.put(COLUMN_DOB, user.getDob());
        values.put(COLUMN_PICTURE, user.getPicture());

        // updating row
        return db.update(TABLE_USER, values, COLUMN_ACCESS_TOKEN + " = ?",
                new String[]{accessToken});
    }

    // Getting User Count
    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
