package com.cureinstant.cureinstant.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FollowAdapter;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.misc.SimpleDividerItemDecoration;
import com.cureinstant.cureinstant.model.Follow;
import com.cureinstant.cureinstant.util.Utilities;

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

public class FollowActivity extends AppCompatActivity {

    RecyclerView followList;
    FollowAdapter followAdapter;
    private String type;
    private ArrayList<Follow> follows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getString("type");
            follows = b.getParcelableArrayList("follows");
        }

        if (type == null) {
            Toast.makeText(FollowActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(type);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        followList = (RecyclerView) findViewById(R.id.follow_recycler_view);
        followList.setLayoutManager(new LinearLayoutManager(FollowActivity.this));
        followAdapter = new FollowAdapter(FollowActivity.this, type, follows, followList);
        followList.setAdapter(followAdapter);
        followList.setItemAnimator(new DefaultItemAnimator());
        followList.addItemDecoration(new SimpleDividerItemDecoration(this));
        followAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (Utilities.checkConnection()) {
                    RequestMoreFollowData requestMoreFollowData = new RequestMoreFollowData(type, follows.get(follows.size() - 1).getFollowID());
                    requestMoreFollowData.execute();
                }
            }
        });
    }

    private class RequestMoreFollowData extends AsyncTask<Void, Void, ArrayList<Follow>> {

        ArrayList<Follow> newFollows = new ArrayList<>();
        private int oldFeedItemCount = follows.size() - 1;
        private int addedFeedItemCount = 0;
        private String type;
        private int lastFollowID;

        public RequestMoreFollowData(String type, int lastFollowID) {
            this.type = type;
            this.lastFollowID = lastFollowID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newFollows.clear();
            newFollows.addAll(follows);
            //add null , so the adapter will check view_type and show progress bar at bottom
            follows.add(null);
            followAdapter.notifyItemInserted(follows.size() - 1);
        }

        @Override
        protected ArrayList<Follow> doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            int followID = 0;
            int userID = 0;
            String name = "";
            String username = "";
            String speciality = "";
            String picture = "";
            boolean isFollowing = false;

            String url = "http://www.cureinstant.com/api/";
            if (type.equals("followers")) {
                url += "followers/load-more";
            } else {
                url += "followings/load-more";
            }

            RequestBody body = new FormBody.Builder()
                    .add("id", String.valueOf(lastFollowID))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();

                follows.remove(follows.size() - 1); // Remove progressView

                JSONObject followJson = new JSONObject(s);
                JSONArray followArray;
                if (type.equals("followers")) {
                    followArray = followJson.getJSONArray("followers");
                } else {
                    followArray = followJson.getJSONArray("followings");
                }

                for (int i = 0; i < followArray.length(); i++) {
                    followID = 0;
                    userID = 0;
                    isFollowing = false;
                    name = "";
                    username = "";
                    speciality = "";
                    picture = "";

                    JSONObject followObject = followArray.getJSONObject(i);
                    switch (type) {
                        case "followers":
                            followID = followObject.getInt("id");
                            userID = followObject.getInt("f_id");
                            isFollowing = followObject.getBoolean("following");
                            JSONObject followerObject = followObject.getJSONObject("follower");
                            name = followerObject.getString("name");
                            username = followerObject.getString("username");
                            if (!followerObject.isNull("speciality")) {
                                speciality = followerObject.getString("speciality");
                            }
                            if (!followerObject.isNull("profile_pic")) {
                                JSONObject pictureObject = followerObject.getJSONObject("profile_pic");
                                picture = pictureObject.getString("pic_name");
                            }
                            break;
                        default:
                            followID = followObject.getInt("id");
                            userID = followObject.getInt("f_id");
                            JSONObject followingObject = followObject.getJSONObject("following");
                            name = followingObject.getString("name");
                            username = followingObject.getString("username");
                            if (!followingObject.isNull("speciality")) {
                                speciality = followingObject.getString("speciality");
                            }
                            if (!followingObject.isNull("profile_pic")) {
                                JSONObject pictureObject = followingObject.getJSONObject("profile_pic");
                                picture = pictureObject.getString("pic_name");
                            }
                            break;
                    }

                    newFollows.add(new Follow(followID, userID, name, username, speciality, picture, isFollowing));
                }

                return newFollows;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Follow> newFollows) {
            super.onPostExecute(newFollows);
            if (newFollows != null && !newFollows.isEmpty()) {
                if (newFollows.size() > oldFeedItemCount) {
                    follows.clear();
                    follows.addAll(newFollows);
                    followAdapter.setLoaded();
                } else {
                    followAdapter.notifyItemRemoved(follows.size() - 1);
                }
            }
        }
    }

}
