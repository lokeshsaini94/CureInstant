package com.cureinstant.cureinstant.fragment.read;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FeedAdapter;
import com.cureinstant.cureinstant.misc.ConnectivityReceiver;
import com.cureinstant.cureinstant.misc.MyApplication;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.model.Answer;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.model.Feed;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {


    private ArrayList<Feed> feedList = new ArrayList<>();
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Snackbar snackbar;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        if (savedInstanceState != null) {
            feedList = savedInstanceState.getParcelableArrayList("feedList");
        }

        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Check Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.feed_refresh);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        feedAdapter = new FeedAdapter(getContext(), feedList, recyclerView);
        recyclerView.setAdapter(feedAdapter);
        feedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (Utilities.checkConnection()) {
                    RequestMoreData requestMoreData = new RequestMoreData();
                    requestMoreData.execute();
                }
            }
        });

        if (Utilities.checkConnection()) {
            if (feedList.isEmpty()) {
                refreshData();
            }
        } else {
            snackbar.show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.checkConnection()) {
                    swipeRefreshLayout.setRefreshing(true);
                    refreshData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    snackbar.show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("feedList", feedList);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            snackbar.show();
        } else {
            if (feedList.isEmpty()) {
                refreshData();
            }
            snackbar.dismiss();
        }
    }

    // Scrolls Feed back to the 1st item
    public void goBackUp() {
        recyclerView.smoothScrollToPosition(0);
    }

    // Refresh the Feed data
    public void refreshData() {
        RequestData requestData = new RequestData();
        requestData.execute();
    }

    // Dummy data for feed
    private void prepareFeedData() {
        Feed feed1 = new Feed("BLOG", "", "", "", "igfja agfdsaf", "asdygb asgdk sdfas", "", 0, 0, 0, 0, false, false, null, null, null, null, null, "Lokesh", "lokeshsaini94", "adg", "1488976746_6.jpg");
        feedList.add(feed1);
        feedList.add(feed1);
        feedList.add(feed1);

        feedAdapter.notifyDataSetChanged();
    }

    // Returns Feed object from feedItem jsonObject
    private Feed fetchBlog(JSONObject feedItem) throws JSONException {

        String title = "", actionName = "", actionType = "", id, content, time, doctorName = null, doctorUsername = null, doctorSpec = null, doctorPicture = null;
        int likes = 0, followings = 0, comments = 0, shares = 0;
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> youtubeVideos = new ArrayList<>();
        ArrayList<Comment> commentsList = new ArrayList<>();
        Answer answer = null;
        boolean liked = false, followed = false;
        Feed feed = null;

        String type = feedItem.getString("type");
        String actionTypeTitle = feedItem.getString("feed");
        JSONObject feedItemContent = feedItem.getJSONObject("content");

        switch (actionTypeTitle) {
            case "COMMENT":
                actionName = feedItem.getString("sec_name");
                actionType = " commented on this";
                break;
            case "ANSWER":
                actionName = feedItem.getString("sec_name");
                actionType = " answered this";
                break;
            case "LIKE":
                actionName = feedItem.getString("sec_name");
                actionType = " found this helpful";
                break;
            case "SHARE":
                actionName = feedItem.getString("sec_name");
                actionType = " shared this";
                break;
        }


        switch (type) {
            case "BLOG": {
                JSONObject header = feedItemContent.getJSONObject("blog_header");
                title = header.getString("title");
                id = feedItemContent.getString("id");
                content = feedItemContent.getString("content");
                time = feedItemContent.getString("created_at");
                likes = feedItemContent.optInt("likes", 0);
                comments = feedItemContent.optInt("comments", 0);
                shares = feedItemContent.optInt("shares", 0);
                liked = !feedItemContent.isNull("liked");
                JSONArray imagesArray = feedItemContent.getJSONArray("images");
                for (int i = 0, count = imagesArray.length(); i < count; i++) {
                    JSONObject jsonObject = imagesArray.getJSONObject(i);
                    images.add(jsonObject.getString("doc"));
                }
                JSONObject doctor = feedItemContent.getJSONObject("user");
                doctorName = doctor.getString("name");
                doctorUsername = doctor.getString("username");
                doctorSpec = doctor.getString("speciality");
                if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                    JSONObject picture = doctor.getJSONObject("profile_pic");
                    doctorPicture = picture.getString("pic_name");
                }
                feed = new Feed(type, actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
            }
            case "POST": {
                id = feedItemContent.getString("id");
                content = feedItemContent.getString("content");
                time = feedItemContent.getString("created_at");
                likes = feedItemContent.optInt("likes", 0);
                comments = feedItemContent.optInt("comments", 0);
                shares = feedItemContent.optInt("shares", 0);
                liked = !feedItemContent.isNull("liked");
                JSONArray imagesArray = feedItemContent.getJSONArray("images");
                for (int i = 0, count = imagesArray.length(); i < count; i++) {
                    JSONObject jsonObject = imagesArray.getJSONObject(i);
                    images.add(jsonObject.getString("doc"));
                }
                JSONObject doctor = feedItemContent.getJSONObject("user");
                doctorName = doctor.getString("name");
                doctorUsername = doctor.getString("username");
                doctorSpec = doctor.getString("speciality");
                if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                    JSONObject picture = doctor.getJSONObject("profile_pic");
                    doctorPicture = picture.getString("pic_name");
                }
                feed = new Feed(type, actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
            }
            case "QUERY":
                id = feedItemContent.getString("id");
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
                time = feedItemContent.getString("created_at");
                JSONArray imagesArray = feedItemContent.getJSONArray("images");
                for (int i = 0, count = imagesArray.length(); i < count; i++) {
                    JSONObject jsonObject = imagesArray.getJSONObject(i);
                    images.add(jsonObject.getString("doc"));
                }
                followings = feedItemContent.optInt("followings", 0);
                comments = feedItemContent.optInt("comments", 0);
                followed = !feedItemContent.isNull("followed");
                feed = new Feed(type, actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
        }

        return feed;
    }

    // Fetches and Sets Feed data from api call
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
            feedAdapter.notifyItemRangeChanged(0, feedList.size());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    // Fetches and Sets more Feed data from api call
    private class RequestMoreData extends AsyncTask<Void, Void, Void> {

        private int oldFeedItemCount = feedList.size() - 1;
        private int addedFeedItemCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //add null , so the adapter will check view_type and show progress bar at bottom
            feedList.add(null);
            feedAdapter.notifyItemInserted(feedList.size() - 1);
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

                feedList.remove(feedList.size() - 1); // Remove progressView

                JSONObject feedJson = new JSONObject(s);
                JSONObject feedData = feedJson.getJSONObject("data");
                Utilities.pageData = feedData.getJSONObject("pageData").toString();
                JSONArray feeds = feedData.getJSONArray("feeds");

                addedFeedItemCount = feeds.length();
                if (feeds.length() > 0) {
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject feedItem = feeds.getJSONObject(i);
                        feedList.add(fetchBlog(feedItem));
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (feedList.size() > oldFeedItemCount) {
                feedAdapter.notifyItemRangeChanged((oldFeedItemCount+1), feedList.size());
                feedAdapter.setLoaded();
            } else {
                feedAdapter.notifyItemRemoved(feedList.size() - 1);
            }
        }
    }
}
