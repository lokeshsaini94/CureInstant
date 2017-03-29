package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;

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

public class NewQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText questionTitle;
    private EditText questionDesc;
    private Switch questionDescSwitch;
    private Button questionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(R.string.ask_question);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        questionTitle = (EditText) findViewById(R.id.question_title);
        questionDesc = (EditText) findViewById(R.id.question_desc);
        questionDescSwitch = (Switch) findViewById(R.id.question_desc_switch);
        questionButton = (Button) findViewById(R.id.question_button);

        questionButton.setOnClickListener(this);

        questionDescSwitch.setChecked(false);
        questionDescSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    questionDesc.setVisibility(View.VISIBLE);
                } else {
                    questionDesc.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_button:
                String title = questionTitle.getText().toString();
                String desc = questionDesc.getText().toString();
                ArrayList<String> images = new ArrayList<>();
                if (title.isEmpty()) {
                    questionTitle.setError(getString(R.string.error_field_required));
                } else {
                    PostQuestion postQuestion = new PostQuestion(title, desc, images);
                    postQuestion.execute();
                }
                break;
        }
    }

    private void returnIntent() {
        Intent data = new Intent();
        data.putExtra("newQuestionDone", true);
        setResult(RESULT_OK, data);
        finish();
    }

    private class PostQuestion extends AsyncTask<Void, Void, Boolean> {

        String title;
        String desc;
        ArrayList<String> images;

        public PostQuestion(String title, String desc, ArrayList<String> images) {
            this.title = title;
            this.desc = desc;
            this.images = images;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("question", title)
                    .add("question_description", desc);

            if (!images.isEmpty()) {
                for (int i=0; i<images.size(); i++) {
                    formBuilder.add("image[" + (i) +  "]", images.get(i));
                }
            }

            RequestBody body = formBuilder.build();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/question/submit")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject feedJson = new JSONObject(result);
                String status = feedJson.getString("success");
                return status.equals("success");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                returnIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
