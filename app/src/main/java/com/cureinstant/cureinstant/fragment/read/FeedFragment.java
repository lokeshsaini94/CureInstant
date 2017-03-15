package com.cureinstant.cureinstant.fragment.read;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FeedAdapter;
import com.cureinstant.cureinstant.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements View.OnClickListener {

    boolean isFollowing = false;

    private List<Feed> feedList = new ArrayList<>();
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);

        feedAdapter = new FeedAdapter(getContext(), feedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feedAdapter);

        prepareFeedData();

        return rootView;
    }

    private void prepareFeedData() {
        Feed feed1 = new Feed("BLOG", "igfja agfdsaf", "asdygb asgdk sdfas", "", "", "", "", "", "Lokesh", "lokeshsaini94", "adg", "1488976746_6.jpg");
        feedList.add(feed1);
        feedList.add(feed1);
        feedList.add(feed1);

        feedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_follow_button:
                Button button = (Button) view;
                View parentView = (View) button.getParent();
                TextView count = (TextView) parentView.findViewById(R.id.post_follow_count);
                changeFollowingViews(isFollowing, button, count);
                break;
            case R.id.post_menu_overflow:
                ImageButton menuButton = (ImageButton) view;
                PopupMenu popupMenu = new PopupMenu(getContext(), menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
                break;
        }
    }

    private void changeFollowingViews(boolean b, Button button, TextView count) {
        if (b) {
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setText("Follow");
            count.setText("0");
            isFollowing = false;
        } else {
            button.setTextColor(getResources().getColor(R.color.primary_text));
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            button.setText("Unfollow");
            count.setText("1");
            isFollowing = true;
        }
    }

    // TODO: 15-03-2017 make it fetch data properly without errors for each feed type
    private class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/get-user-dashboard")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.e("SplashScreen", "doInBackground: " + accessTokenValue);
                String s = response.body().string();
                JSONObject feedJson = new JSONObject(s);
                JSONObject feedData = feedJson.getJSONObject("data");
                JSONArray feeds = feedData.getJSONArray("feeds");

                feedList = new ArrayList<>(feeds.length());

                String type, title = null, content, time, likes = null, followings = null, comments, shares, doctorName, doctorUsername, doctorSpec, doctorPicture = null;
                for (int i = 0; i < feeds.length(); i++) {
                    JSONObject feedItem = feeds.getJSONObject(1);
                    type = feedItem.getString("type");
                    Log.e(TAG, "doInBackground: type " + type);
                    JSONObject feedItemContent = feedItem.getJSONObject("content");
                    if (type.equals("BLOG")) {
                        JSONObject header = feedItemContent.getJSONObject("blog_header");
                        title = header.getString("title");
                    } else if (type.equals("QUERY")) {
                        title = feedItemContent.getString("question");
                    }
                    Log.e(TAG, "doInBackground: title " + title);
                    content = feedItemContent.getString("content");
                    Log.e(TAG, "doInBackground: content " + content);
                    time = feedItemContent.getString("updated_at");
                    Log.e(TAG, "doInBackground: time " + time);
                    if (!type.equals("QUERY")) {
                        likes = feedItemContent.getString("likes");
                        Log.e(TAG, "doInBackground: likes " + likes);
                    } else {
                        followings = feedItemContent.getString("followings");
                        Log.e(TAG, "doInBackground: followings " + followings);
                    }
                    comments = feedItemContent.getString("comments");
                    Log.e(TAG, "doInBackground: comments " + comments);
                    shares = feedItemContent.getString("shares");
                    Log.e(TAG, "doInBackground: shares " + shares);

                    JSONObject doctor = feedItemContent.getJSONObject("user");
                    doctorName = doctor.getString("name");
                    doctorUsername = doctor.getString("username");
                    doctorSpec = doctor.getString("speciality");
                    Log.e(TAG, "doInBackground: doctorName " + doctorName);

                    if (doctor.has("pic_name") && !doctor.isNull("pic_name")) {
                        JSONObject picture = doctor.getJSONObject("profile_pic");
                        doctorPicture = picture.getString("pic_name");
                        Log.e(TAG, "doInBackground: doctorPicture " + doctorPicture);
                    }

                    Feed feed = new Feed(
                            type, title, content, time, likes, followings, comments, shares, doctorName, doctorUsername, doctorSpec, doctorPicture
                    );

                    feedList.add(feed);
                    Log.e(TAG, "doInBackground: feed title: " + feed.getTitle());
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            feedAdapter.notifyDataSetChanged();
        }
    }
}
