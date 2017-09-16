package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.BookDoctor;
import com.cureinstant.cureinstant.model.BookSlot;
import com.cureinstant.cureinstant.model.BookSlotDay;
import com.google.firebase.crash.FirebaseCrash;

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

public class BookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    BookDoctor bookDoctor;
    ProgressDialog progressDialog;

    ArrayList<BookSlotDay> bookSlotDaysFinal;

    ListView morningSlots;

    int selectedDay = 0;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent i = getIntent();
        bookDoctor = i.getParcelableExtra("BookDoctor");

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(bookDoctor.getName());
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.text_loading));

        spinner = (Spinner) findViewById(R.id.book_slot_spinner);

        morningSlots = (ListView) findViewById(R.id.slots_list);

        FetchSlots fetchSlots = new FetchSlots();
        fetchSlots.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<BookSlot> bookSlots = bookSlotDaysFinal.get(position).getBookSlots();
        ArrayList<String> timeSlots = new ArrayList<>();
        selectedDay = position;
        if (bookSlots == null) {
            timeSlots.add(getString(R.string.text_no_appointments_available));
        } else {
            for (int i = 0; i < bookSlots.size(); i++) {
                timeSlots.add(bookSlots.get(i).getStartTime());
            }
        }
        ArrayAdapter<String> slotsAdapter = new ArrayAdapter<>(BookActivity.this, android.R.layout.simple_list_item_1, timeSlots);
        morningSlots.setAdapter(slotsAdapter);
        if (bookSlots != null) {
            morningSlots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BookAppointment bookAppointment = new BookAppointment(position);
                    bookAppointment.execute();
                }
            });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
                    String day = slotsDay.getString("day");
                    if (slotsDay.has("slots")) {
                        JSONArray daySlotsArray = slotsDay.getJSONArray("slots");
                        for (int x = 0; x < daySlotsArray.length(); x++) {
                            JSONObject daySlotObject = daySlotsArray.getJSONObject(x);
                            String startTime = daySlotObject.getString("start_time");
                            String endTime = daySlotObject.getString("end_time");
                            String interval = daySlotObject.getString("slot_interval");
                            int availID = daySlotObject.getInt("id");
                            int workID = daySlotObject.getInt("work_id");

                            ArrayList<BookSlot> bookSlots = new ArrayList<>();
                            JSONArray slotArray = daySlotObject.getJSONArray("slots");
                            for (int z = 0; z < slotArray.length(); z++) {
                                JSONObject bookSlotObject = slotArray.getJSONObject(z);
                                int slotID = bookSlotObject.getInt("id");
                                String slotStartTime = bookSlotObject.getString("start_time");
                                String slotEndTime = bookSlotObject.getString("end_time");
                                bookSlots.add(new BookSlot(slotID, slotStartTime, slotEndTime));
                            }

                            bookSlotDays.add(new BookSlotDay(i, day, date, startTime, endTime, interval, availID, workID, bookSlots));
                        }
                    } else {
                        bookSlotDays.add(new BookSlotDay(i, day, date, null, null, null, 0, 0, null));
                    }
                }

                return bookSlotDays;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BookSlotDay> bookSlotDays) {
            super.onPostExecute(bookSlotDays);
            progressDialog.dismiss();
            bookSlotDaysFinal = bookSlotDays;

            ArrayList<String> days = new ArrayList<>();
            for (int i = 0; i < bookSlotDays.size(); i++) {
                days.add(bookSlotDays.get(i).getDay());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(BookActivity.this, android.R.layout.simple_spinner_item, days);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(BookActivity.this);
        }
    }

    // Fetches Time Slots for a doctor's appointment
    private class BookAppointment extends AsyncTask<Void, Void, Boolean> {

        private int slotPosition = 0;

        BookAppointment(int slotPosition) {
            this.slotPosition = slotPosition;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://www.cureinstant.com/api/ams/appointment/book";

            RequestBody body = new FormBody.Builder()
                    .add("slot", String.valueOf(bookSlotDaysFinal.get(selectedDay).getBookSlots().get(slotPosition).getSlotID()))
                    .add("work", String.valueOf(bookSlotDaysFinal.get(selectedDay).getWorkID()))
                    .add("availability", String.valueOf(bookSlotDaysFinal.get(selectedDay).getAvailID()))
                    .add("date", String.valueOf(bookSlotDaysFinal.get(selectedDay).getDate()))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject bookResult = new JSONObject(s);
                if (bookResult.has("success")) {
                    String result = bookResult.getString("success");
                    if (result.equals("success")) {
                        return true;
                    }
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            String result;
            if (aBoolean) {
                result = getString(R.string.text_appointment_successfully_booked);
            } else {
                result = getString(R.string.text_something_went_wrong);
            }
            Toast.makeText(BookActivity.this, result, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
}
