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
import com.cureinstant.cureinstant.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements View.OnClickListener {

    boolean isFollowing = false;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;

    private List<Feed> feedList;
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.feed_refresh);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);
        feedList = new ArrayList<>();

        feedAdapter = new FeedAdapter(getContext(), feedList);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feedAdapter);

        RequestData requestData = new RequestData();
        requestData.execute();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                RequestData requestData = new RequestData();
                requestData.execute();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            RequestMoreData requestMoreData = new RequestMoreData();
                            requestMoreData.execute();
                            feedAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    loading = true;
                }
            }
        });

        return rootView;
    }

    private void prepareFeedData() {
        Feed feed1 = new Feed("BLOG", "", "", "igfja agfdsaf", "asdygb asgdk sdfas", "", "", "", "", "", "Lokesh", "lokeshsaini94", "adg", "1488976746_6.jpg");
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

    private Feed fetchBlog(JSONObject feedItem) throws JSONException {

        String title = "", actionName = "", actionType = "", content, time, likes = "0", followings = "0", comments = "0", shares = "0", doctorName = null, doctorUsername = null, doctorSpec = null, doctorPicture = null;

        Feed feed = null;

        String type = feedItem.getString("type");
        String actionTypeTitle = feedItem.getString("feed");
        JSONObject feedItemContent = feedItem.getJSONObject("content");

        if (actionTypeTitle.equals("COMMENT")) {
            actionName = feedItem.getString("sec_name");
            actionType = " commented on this";
        } else if (actionTypeTitle.equals("ANSWER")) {
            actionName = feedItem.getString("sec_name");
            actionType = " answered this";
        } else if (actionTypeTitle.equals("SHARE")) {
            actionName = feedItem.getString("sec_name");
            actionType = " shared this";
        }


        if (type.equals("BLOG")) {
            JSONObject header = feedItemContent.getJSONObject("blog_header");
            title = header.getString("title");
            content = feedItemContent.getString("content");
            time = feedItemContent.getString("updated_at");
            likes = feedItemContent.getString("likes");
            comments = feedItemContent.getString("comments");
            shares = feedItemContent.getString("shares");
            JSONObject doctor = feedItemContent.getJSONObject("user");
            doctorName = doctor.getString("name");
            doctorUsername = doctor.getString("username");
            doctorSpec = doctor.getString("speciality");
            if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                JSONObject picture = doctor.getJSONObject("profile_pic");
                doctorPicture = picture.getString("pic_name");
            }
            feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, doctorName, doctorUsername, doctorSpec, doctorPicture);
        } else if (type.equals("POST")) {
            content = feedItemContent.getString("content");
            time = feedItemContent.getString("updated_at");
            likes = feedItemContent.getString("likes");
            comments = feedItemContent.getString("comments");
            shares = feedItemContent.getString("shares");
            JSONObject doctor = feedItemContent.getJSONObject("user");
            doctorName = doctor.getString("name");
            doctorUsername = doctor.getString("username");
            doctorSpec = doctor.getString("speciality");
            if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                JSONObject picture = doctor.getJSONObject("profile_pic");
                doctorPicture = picture.getString("pic_name");
            }
            feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, doctorName, doctorUsername, doctorSpec, doctorPicture);
        } else if (type.equals("QUERY")) {
            title = feedItemContent.getString("question");
            if (actionTypeTitle.equals("ANSWER")) {
                JSONObject answerObject = feedItemContent.getJSONObject("answer");
                content = answerObject.getString("content");
                JSONObject doctor = feedItemContent.getJSONObject("user");
                doctorName = doctor.getString("name");
                doctorUsername = doctor.getString("username");
                doctorSpec = doctor.getString("speciality");
                if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                    JSONObject picture = doctor.getJSONObject("profile_pic");
                    doctorPicture = picture.getString("pic_name");
                }
            } else {
                content = feedItemContent.getString("description");
            }
            time = feedItemContent.getString("updated_at");
            followings = feedItemContent.getString("followings");
            comments = feedItemContent.getString("comments");
            feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, doctorName, doctorUsername, doctorSpec, doctorPicture);
        }

        return feed;
    }

    private class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
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
                Log.e("FeedFragment", "doInBackground: accessTokenValue " + accessTokenValue);
                String s = response.body().string();
                JSONObject feedJson = new JSONObject(s);
                JSONObject feedData = feedJson.getJSONObject("data");
                Utilities.pageData = feedData.getJSONObject("pageData").toString();
                JSONArray feeds = feedData.getJSONArray("feeds");

                feedList.clear();

                for (int i = 0; i < feeds.length(); i++) {
                    JSONObject feedItem = feeds.getJSONObject(i);

                    feedList.add(fetchBlog(feedItem));
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
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class RequestMoreData extends AsyncTask<Void, Void, Void> {

        private int oldFeedItemCount = feedList.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("pageData", Utilities.pageData)
                    .build();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/load-more-dashboard")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject feedJson = new JSONObject(s);
                JSONObject feedData = feedJson.getJSONObject("data");
                Utilities.pageData = feedData.getJSONObject("pageData").toString();
                JSONArray feeds = feedData.getJSONArray("feeds");

                for (int i = 0; i < feeds.length(); i++) {
                    JSONObject feedItem = feeds.getJSONObject(i);

                    feedList.add(fetchBlog(feedItem));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            feedAdapter = new FeedAdapter(getContext(), feedList);
//            recyclerView.setAdapter(feedAdapter);
//            feedAdapter.notifyDataSetChanged();
            feedAdapter.notifyItemInserted(oldFeedItemCount);

        }
    }
}
