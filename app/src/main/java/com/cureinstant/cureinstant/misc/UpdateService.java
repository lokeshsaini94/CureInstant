package com.cureinstant.cureinstant.misc;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.FeedItemActivity;
import com.cureinstant.cureinstant.activity.NewQuestionActivity;
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
 * Created by lokesh on 9/15/2017.
 */

public class UpdateService extends Service {

    private static final String TAG = "UpdateService";
    private Feed feed;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews remoteViews = new RemoteViews(UpdateService.this.getPackageName(),
                R.layout.widget_layout);

        RequestData requestData = new RequestData();
        requestData.execute();

        if (feed != null) {
            if (feed.getId() != null && !feed.getId().isEmpty()) {
                if (feed.getTitle() != null && !feed.getTitle().isEmpty()) {
                    remoteViews.setTextViewText(R.id.post_title, feed.getTitle());
                } else {
                    remoteViews.setViewVisibility(R.id.post_title, View.GONE);
                }
                if (feed.getContent() != null && !feed.getContent().isEmpty()) {
                    remoteViews.setTextViewText(R.id.post_desc, feed.getContent());
                } else {
                    remoteViews.setViewVisibility(R.id.post_desc, View.GONE);
                }

                Intent feedIntent = new Intent(UpdateService.this, FeedItemActivity.class);
                feedIntent.putExtra("feed_item", feed);
                PendingIntent questionPendingIntent = PendingIntent.getActivity(UpdateService.this,
                        0, feedIntent, 0);
                remoteViews.setOnClickPendingIntent(R.id.top_question_container, questionPendingIntent);
            }
        }

        // Register an onClickListener
        Intent QuestionIntent = new Intent(UpdateService.this, NewQuestionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this,
                0, QuestionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ask_question, pendingIntent);

        ComponentName theWidget = new ComponentName(this, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, remoteViews);

        return super.onStartCommand(intent, flags, startId);
    }


    // Fetches and Sets Feed data from api call
    private class RequestData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e(TAG, "doInBackground: RequestData started");
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
                JSONObject feedItem = feedData.getJSONObject(0);
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

                feed = new Feed("QUERY", actionName, actionType, id, title, content, time, likes, followings, comments, shares, liked, followed, images, links, youtubeVideos, commentsList, answer, doctorName, doctorUsername, doctorSpec, doctorPicture);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            Log.e(TAG, "doInBackground: RequestData Ended");
            return null;
        }
    }
}
