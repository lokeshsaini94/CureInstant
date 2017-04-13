package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.BookDoctor;
import com.cureinstant.cureinstant.model.BookSlot;
import com.cureinstant.cureinstant.model.BookSlotDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class BookActivity extends AppCompatActivity {

    BookDoctor bookDoctor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent i = getIntent();
        bookDoctor = i.getParcelableExtra("BookDoctor");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");

        FetchSlots fetchSlots = new FetchSlots();
        fetchSlots.execute();
    }

    // Fetches Time Slots for a doctor's appointment
    private class FetchSlots extends AsyncTask<Void, Void, ArrayList<BookSlotDay>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<BookSlotDay> doInBackground(Void... params) {
            ArrayList<BookSlotDay> bookSlotDays = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            String url = "http://www.cureinstant.com/api/ams/appointment/show-slots";

            RequestBody body = new FormBody.Builder()
                    .add("doctor", String.valueOf(bookDoctor.getUserID()))
                    .add("work", String.valueOf(bookDoctor.getWorkID()))
                    .add("timezone", String.valueOf(5.5))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();

                JSONObject slotsObject = new JSONObject(s);
                JSONObject slotsDays = slotsObject.getJSONObject("slots");
                for (int i = 0; i < 7; i++) {
                    JSONObject slotsDay = slotsDays.getJSONObject(String.valueOf(i + 1));
                    String date = slotsDay.getString("date");
                    Log.e("TAG", "doInBackground: " + i);
                    if (slotsDay.has("slots")) {
                        Log.e("TAG", "doInBackground: has slot");
                        JSONArray daySlotsArray = slotsDay.getJSONArray("slots");
                        for (int x = 0; x < daySlotsArray.length(); x++) {
                            JSONObject daySlotObject = daySlotsArray.getJSONObject(x);
                            String day = daySlotObject.getString("day");
                            String startTime = daySlotObject.getString("start_time");
                            String endTime = daySlotObject.getString("end_time");
                            String interval = daySlotObject.getString("slot_interval");
                            int availID = daySlotObject.getInt("id");
                            Log.e("TAG", "doInBackground: day slot fetching done");

                            ArrayList<BookSlot> bookSlots = new ArrayList<>();
                            JSONArray slotArray = daySlotObject.getJSONArray("slots");
                            Log.e("TAG", "doInBackground: slotArray.length() " + slotArray.length());
                            for (int z = 0; z < slotArray.length(); z++) {
                                Log.e("TAG", "doInBackground: slotArray loop start " + (z + 1));
                                // TODO: 11-04-2017 Fix the error here
                                JSONObject bookSlotObject = slotArray.getJSONObject(i);
                                Log.e("TAG", "doInBackground: bookSlotObject done ");
                                int slotID = bookSlotObject.getInt("id");
                                Log.e("TAG", "doInBackground: slotID " + slotID);
                                String slotStartTime = bookSlotObject.getString("start_time");
                                Log.e("TAG", "doInBackground: slotStartTime " + slotStartTime);
                                String slotEndTime = bookSlotObject.getString("end_time");
                                Log.e("TAG", "doInBackground: slotEndTime " + slotEndTime);
                                bookSlots.add(new BookSlot(slotID, slotStartTime, slotEndTime));
                                Log.e("TAG", "doInBackground: slot " + (z + 1) + " done");
                            }

                            bookSlotDays.add(new BookSlotDay(i, day, date, startTime, endTime, interval, availID, bookSlots));
                            Log.e("TAG", "doInBackground: All done");
                        }
                    }
                }

                return bookSlotDays;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BookSlotDay> bookSlotDays) {
            super.onPostExecute(bookSlotDays);
            progressDialog.dismiss();
            Log.e("TAG", "onPostExecute: bookSlotDays.size() " + bookSlotDays.size());
        }
    }
}
