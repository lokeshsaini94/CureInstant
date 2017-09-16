package com.cureinstant.cureinstant.fragment.read;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FeedAdapter;
import com.cureinstant.cureinstant.model.Answer;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.model.Feed;
import com.google.firebase.crash.FirebaseCrash;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private ArrayList<Feed> answeredFeedList = new ArrayList<>();
    private FeedAdapter answeredFeedAdapter;
    private RecyclerView answeredRecyclerView;
    private ArrayList<Feed> commentedFeedList = new ArrayList<>();
    private FeedAdapter commentedFeedAdapter;
    private RecyclerView commentedRecyclerView;


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);

        answeredRecyclerView = (RecyclerView) rootView.findViewById(R.id.answered_list);
        commentedRecyclerView = (RecyclerView) rootView.findViewById(R.id.commented_list);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        answeredRecyclerView.setLayoutManager(mLayoutManager);
        answeredRecyclerView.setItemAnimator(new DefaultItemAnimator());
        answeredFeedAdapter = new FeedAdapter(getContext(), answeredFeedList, answeredRecyclerView);
        answeredRecyclerView.setAdapter(answeredFeedAdapter);

        final LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentedRecyclerView.setLayoutManager(mLayoutManager2);
        commentedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commentedFeedAdapter = new FeedAdapter(getContext(), commentedFeedList, commentedRecyclerView);
        commentedRecyclerView.setAdapter(commentedFeedAdapter);

        RequestData requestData = new RequestData();
        requestData.execute();

        return rootView;
    }


    // Fetches and Sets Feed data from api call
    private class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/trending/most-answered")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject feedJson = new JSONObject(s);
                JSONArray feedData = feedJson.getJSONArray("trending");
                for (int i = 0; i < feedData.length(); i++) {
                    JSONObject feedItem = feedData.getJSONObject(i);
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

                    answeredFeedList.add(new Feed("QUERY", actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            Request request2 = new Request.Builder()
                    .url("http://www.cureinstant.com/api/trending/most-commented")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response2 = client.newCall(request2).execute();
                String s2 = response2.body().string();
                JSONObject feedJsonC = new JSONObject(s2);
                JSONArray feedDataC = feedJsonC.getJSONArray("trending");
                for (int i = 0; i < feedDataC.length(); i++) {
                    JSONObject feedItem = feedDataC.getJSONObject(i);
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

                    commentedFeedList.add(new Feed("QUERY", actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            answeredFeedAdapter.notifyItemRangeInserted(0, answeredFeedList.size());
            commentedFeedAdapter.notifyItemRangeInserted(0, commentedFeedList.size());
        }
    }

}
