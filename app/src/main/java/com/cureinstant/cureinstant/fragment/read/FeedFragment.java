package com.cureinstant.cureinstant.fragment.read;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FeedAdapter;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.ConnectivityReceiver;
import com.cureinstant.cureinstant.util.MyApplication;
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
public class FeedFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    boolean isFollowing = false;

    boolean loading = true;

    private List<Feed> feedList;
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View moreProgresbar;
    private Snackbar snackbar;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Check Internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.feed_refresh);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.feed_list);
        moreProgresbar = rootView.findViewById(R.id.feed_more_progressbar);
        feedList = new ArrayList<>();

        feedAdapter = new FeedAdapter(getContext(), feedList);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feedAdapter);

        if (Utilities.checkConnection()) {
            RequestData requestData = new RequestData();
            requestData.execute();
        } else {
            snackbar.show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.checkConnection()) {
                    swipeRefreshLayout.setRefreshing(true);
                    RequestData requestData = new RequestData();
                    requestData.execute();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    snackbar.show();
                }
            }
        });

        // Requests more data when recyclerView's last item becomes visible
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems, visibleItemCount, totalItemCount;
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 2) {
                            moreProgresbar.setVisibility(View.VISIBLE);
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

    // Dummy data for feed
    private void prepareFeedData() {
        Feed feed1 = new Feed("BLOG", "", "", "igfja agfdsaf", "asdygb asgdk sdfas", "", "", "", "", "", false, false, "Lokesh", "lokeshsaini94", "adg", "1488976746_6.jpg");
        feedList.add(feed1);
        feedList.add(feed1);
        feedList.add(feed1);

        feedAdapter.notifyDataSetChanged();
    }

    // Returns Feed object from feedItem jsonObject
    private Feed fetchBlog(JSONObject feedItem) throws JSONException {

        String title = "", actionName = "", actionType = "", content, time, likes = "0", followings = "0", comments = "0", shares = "0", doctorName = null, doctorUsername = null, doctorSpec = null, doctorPicture = null;
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
            case "SHARE":
                actionName = feedItem.getString("sec_name");
                actionType = " shared this";
                break;
        }


        switch (type) {
            case "BLOG": {
                JSONObject header = feedItemContent.getJSONObject("blog_header");
                title = header.getString("title");
                content = feedItemContent.getString("content");
                time = feedItemContent.getString("created_at");
                likes = feedItemContent.getString("likes");
                comments = feedItemContent.getString("comments");
                shares = feedItemContent.getString("shares");
                liked = !feedItemContent.isNull("liked");
                JSONObject doctor = feedItemContent.getJSONObject("user");
                doctorName = doctor.getString("name");
                doctorUsername = doctor.getString("username");
                doctorSpec = doctor.getString("speciality");
                if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                    JSONObject picture = doctor.getJSONObject("profile_pic");
                    doctorPicture = picture.getString("pic_name");
                }
                feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, liked, followed, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
            }
            case "POST": {
                content = feedItemContent.getString("content");
                time = feedItemContent.getString("created_at");
                likes = feedItemContent.getString("likes");
                comments = feedItemContent.getString("comments");
                shares = feedItemContent.getString("shares");
                liked = !feedItemContent.isNull("liked");
                JSONObject doctor = feedItemContent.getJSONObject("user");
                doctorName = doctor.getString("name");
                doctorUsername = doctor.getString("username");
                doctorSpec = doctor.getString("speciality");
                if (doctor.has("profile_pic") && !doctor.isNull("profile_pic")) {
                    JSONObject picture = doctor.getJSONObject("profile_pic");
                    doctorPicture = picture.getString("pic_name");
                }
                feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, liked, followed, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
            }
            case "QUERY":
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
                followings = feedItemContent.getString("followings");
                comments = feedItemContent.getString("comments");
                followed = !feedItemContent.isNull("followed");
                feed = new Feed(type, actionName, actionType, title, content, time, likes, followings, comments, shares, liked, followed, doctorName, doctorUsername, doctorSpec, doctorPicture);
                break;
        }

        return feed;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            snackbar.show();
        } else {
            if (feedList.isEmpty()) {
                RequestData requestData = new RequestData();
                requestData.execute();
            }
            snackbar.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    // Fetches and Sets data from api call
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

    // Fetches and Sets more data from api call
    private class RequestMoreData extends AsyncTask<Void, Void, Void> {

        private int oldFeedItemCount = feedList.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            moreProgresbar.setVisibility(View.VISIBLE);
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
            feedAdapter.notifyItemInserted(oldFeedItemCount);
            moreProgresbar.setVisibility(View.GONE);
        }
    }
}
