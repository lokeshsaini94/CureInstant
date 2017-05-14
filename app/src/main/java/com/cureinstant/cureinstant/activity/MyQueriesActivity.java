package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FeedAdapter;
import com.cureinstant.cureinstant.model.Answer;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class MyQueriesActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private ArrayList<Feed> feedList = new ArrayList<>();
    private FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_queries);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle("My Questions");
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_queries_list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyQueriesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        feedAdapter = new FeedAdapter(MyQueriesActivity.this, feedList, recyclerView);
        recyclerView.setAdapter(feedAdapter);

        RequestData requestData = new RequestData();
        requestData.execute();
    }

    // Fetches and Sets Feed data from api call
    private class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/asked-questions")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject feedJson = new JSONObject(s);
                JSONObject questions = feedJson.getJSONObject("questions");
                JSONArray data = questions.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject feedItem = data.getJSONObject(i);
                    String title = "", actionName = "", actionType = "", id, content, time, doctorName = null, doctorUsername = null, doctorSpec = null, doctorPicture = null;
                    int likes = 0, followings = 0, comments = 0, shares = 0;
                    ArrayList<String> images = new ArrayList<>();
                    ArrayList<String> links = new ArrayList<>();
                    ArrayList<String> youtubeVideos = new ArrayList<>();
                    ArrayList<Comment> commentsList = new ArrayList<>();
                    Answer answer = null;
                    boolean liked = false, followed = false;

                    id = feedItem.getString("id");
                    title = feedItem.getString("question");
                    content = feedItem.getString("description");
                    time = feedItem.getString("created_at");
                    followings = feedItem.optInt("followings", 0);
                    comments = feedItem.optInt("comments", 0);

                    feedList.add(new Feed("QUERY", actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            feedAdapter.notifyItemRangeInserted(0, feedList.size());
            progressDialog.dismiss();
        }
    }
}
