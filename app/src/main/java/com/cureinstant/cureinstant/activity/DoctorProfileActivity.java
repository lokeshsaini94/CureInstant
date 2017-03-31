package com.cureinstant.cureinstant.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Doctor;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class DoctorProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String username = null; // or other values
        if(b != null)
            username = b.getString("username");

        if (username != null) {
            RequestUserData requestUserData = new RequestUserData(username);
            requestUserData.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            finish();
        }

        setContentView(R.layout.activity_doctor_profile);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(R.string.doctor_name);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class RequestUserData extends AsyncTask<Void, Void, Doctor> {

        String username;

        public RequestUserData(String username) {
            this.username = username;
        }

        @Override
        protected Doctor doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .build();
//            RequestBody body = RequestBody.create(null, new byte[]{});

            String url = "http://www.cureinstant.com/api/profile/";
            url += username;
            url += "/fetch";

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                Log.e("TAG", "doInBackground: s " + s );

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
