package com.cureinstant.cureinstant.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.SplashScreenActivity;
import com.cureinstant.cureinstant.fragment.RepliesBottomSheetFragment;
import com.cureinstant.cureinstant.misc.ConnectivityReceiver;
import com.cureinstant.cureinstant.misc.MyApplication;
import com.cureinstant.cureinstant.model.Answer;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.model.Feed;
import com.google.firebase.crash.FirebaseCrash;
import com.jakewharton.processphoenix.ProcessPhoenix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lokeshsaini94 on 20-02-2017.
 */

public class Utilities {

    public static final String prefName = "prefName";
    public static final String loginBoolKey = "loginBoolKey";
    public static final String accessTokenKey = "accessTokenKey";
    public static final String refreshTokenKey = "refreshTokenKey";
    public static final String profilePicBaseUrl = "http://www.cureinstant.com/profile_pics/";
    public static final String profilePicSmallBaseUrl = "http://www.cureinstant.com/profile_pics_avatar/";
    public static final String postImageBaseUrl = "http://www.cureinstant.com/posts/images/";
    public static final String blogImageBaseUrl = "http://www.cureinstant.com/blog/images/";
    public static final String questionImageBaseUrl = "http://www.cureinstant.com/query/images/";
    public static final String doctorAlbumBaseUrl = "http://www.cureinstant.com/album/images/";
    public static final String doctorAchievementBaseUrl = "http://www.cureinstant.com/achievements_docs/images/";
    public static String accessTokenValue;
    public static String refreshTokenValue;
    public static String pageData = "";

    // Stores logged in boolean in SharedPreference
    public static void loggedInBool(Context context, Boolean b) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putBoolean(loginBoolKey, b);
        editor.commit();
    }

    // Returns boolean for logged in status
    public static Boolean isLoggedIn(Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE).getBoolean(loginBoolKey, false);
    }

    // Logout use by clearing app data and SharedPreferences
    public static void logout(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();

        MyApplication.getInstance().clearApplicationData();
        trimCache(context);

        Intent mStartActivity = new Intent(context, SplashScreenActivity.class);
        ComponentName cn = mStartActivity.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        ProcessPhoenix.triggerRebirth(context, mainIntent);
        System.exit(0);
    }

    // Deletes all the cache stored in Internal and External Directory
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
            File externalDir = context.getExternalCacheDir();
            if (externalDir != null && externalDir.isDirectory()) {
                deleteDir(externalDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

    // Deletes the provided directory from the storage
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir != null && dir.delete();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    // Converts pixels to DP
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // Method to manually check connection status
    public static boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    // Returns days, hours, minutes, seconds from a give date string
    public static long[] getDateDifference(String postDate) throws ParseException {

        // Makes date object with UTC
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(postDate);
        } catch (ParseException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
        // Makes date object with current timezone
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        // Makes a date object with current date and time
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        Date currentDate = dateFormatter.parse(c.get(Calendar.YEAR) + "-" +
                month + "-" +
                c.get(Calendar.DAY_OF_MONTH) + " " +
                c.get(Calendar.HOUR_OF_DAY) + ":" +
                c.get(Calendar.MINUTE) + ":" +
                c.get(Calendar.SECOND));
        Date postdate = (Date) dateFormatter.parse(dt);

        // Gives difference b/w two dates in milliseconds
        long different = currentDate.getTime() - postdate.getTime();

        // Converts difference to days, hours, minutes, seconds
        long diffInDays = TimeUnit.MILLISECONDS.toDays(different);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(different);
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(different);
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(different);

        if (diffInHours >= 12) {
            diffInHours -= 12;
            diffInMin -= 720;
        }

        return new long[]{diffInDays, diffInHours, diffInMin, diffInSec};
    }

    // Shows Dialog for comment and posts it.
    public static void commentDialog(final Context context, final String type, final String id) {
        final EditText edittext = new EditText(context);
        edittext.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        final AlertDialog.Builder nameDialog = new AlertDialog.Builder(context);
        nameDialog.setTitle(R.string.title_enter_your_comment);
        nameDialog.setView(edittext);

        nameDialog.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                ActionFeed actionFeed = new ActionFeed(type, "comment", id, editTextValue);
                actionFeed.execute();
                Toast.makeText(context, R.string.text_comment_posted_2, Toast.LENGTH_SHORT).show();

            }
        });

        nameDialog.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        nameDialog.show();
    }

    // Fetches Blog data from json and returns a Feed object
    public static Feed getBlogData(Feed feed, JSONObject feedJson) {

        JSONObject feedData = null;
        try {
            feedData = feedJson.getJSONObject("blog");

            JSONObject titleData = feedData.getJSONObject("blog_header");
            feed.setTitle(titleData.getString("title"));

            feed.setContent(feedData.getString("content"));
            feed.setTime(feedData.getString("created_at"));
            if (!feedData.isNull("likes")) {
                feed.setLikes(feedData.getInt("likes"));
            }
            if (!feedData.isNull("shares")) {
                feed.setLikes(feedData.getInt("shares"));
            }
            feed.setLiked(!feedData.isNull("liked"));

            ArrayList<Comment> comments = new ArrayList<>();
            JSONArray commentsArray = feedData.getJSONArray("comments");
            feed.setComments(commentsArray.length());
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = commentsArray.getJSONObject(i);
                String commentString = commentObject.getString("comment");
                int id = 0, replyCount = 0, likes = 0;
                boolean liked = false;
                String time = "";
                if (!commentObject.isNull("id")) {
                    id = commentObject.getInt("id");
                }
                if (!commentObject.isNull("reply_count")) {
                    replyCount = commentObject.getInt("reply_count");
                }
                if (!commentObject.isNull("likes")) {
                    likes = commentObject.getInt("likes");
                }
                if (!commentObject.isNull("liked")) {
                    liked = true;
                }
                time = commentObject.getString("created_at");

                JSONObject userObject = commentObject.getJSONObject("user");
                String name = userObject.getString("name");
                String username = userObject.getString("username");
                String picture = "";
                if (!userObject.isNull("profile_pic")) {
                    JSONObject userPicObject = userObject.getJSONObject("profile_pic");
                    picture = userPicObject.getString("pic_name");
                }
                comments.add(new Comment(commentString, time, id, replyCount, likes, liked, name, username, picture));
            }
            feed.setCommentsList(comments);

            ArrayList<String> images = new ArrayList<>();
            JSONArray imagesArray = feedData.getJSONArray("images");
            for (int i = 0; i < imagesArray.length(); i++) {
                JSONObject imageObject = imagesArray.getJSONObject(i);
                String image = imageObject.getString("doc");
                images.add(image);
            }
            feed.setImages(images);

            ArrayList<String> links = new ArrayList<>();
            JSONArray linksArray = feedData.getJSONArray("links");
            for (int i = 0; i < linksArray.length(); i++) {
                String link = linksArray.getString(i);
                images.add(link);
            }
            feed.setLinks(links);

            ArrayList<String> youtubeVideos = new ArrayList<>();
            JSONArray youtubeVideosArray = feedData.getJSONArray("youtube_videos");
            for (int i = 0; i < youtubeVideosArray.length(); i++) {
                String youtubeVideo = youtubeVideosArray.getString(i);
                images.add(youtubeVideo);
            }
            feed.setYoutubeVideos(youtubeVideos);

            JSONObject userObject = feedData.getJSONObject("user");
            feed.setDoctorName(userObject.getString("name"));
            feed.setDoctorUsername(userObject.getString("username"));
            feed.setDoctorSpec(userObject.getString("speciality"));
            JSONObject pictureObject = userObject.getJSONObject("profile_pic");
            feed.setDoctorPicture(pictureObject.getString("pic_name"));

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        return feed;
    }

    // Fetches Post data from json and returns a Feed object
    public static Feed getPostData(Feed feed, JSONObject feedJson) {

        JSONObject feedData = null;
        try {
            feedData = feedJson.getJSONObject("post");

            feed.setContent(feedData.getString("content"));
            feed.setTime(feedData.getString("created_at"));
            if (!feedData.isNull("likes")) {
                feed.setLikes(feedData.getInt("likes"));
            }
            if (!feedData.isNull("shares")) {
                feed.setLikes(feedData.getInt("shares"));
            }
            feed.setLiked(!feedData.isNull("liked"));

            ArrayList<Comment> comments = new ArrayList<>();
            JSONArray commentsArray = feedData.getJSONArray("comments");
            feed.setComments(commentsArray.length());
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = commentsArray.getJSONObject(i);
                String commentString = commentObject.getString("comment");
                int id = 0, replyCount = 0, likes = 0;
                boolean liked = false;
                String time = "";
                if (!commentObject.isNull("id")) {
                    id = commentObject.getInt("id");
                }
                if (!commentObject.isNull("reply_count")) {
                    replyCount = commentObject.getInt("reply_count");
                }
                if (!commentObject.isNull("likes")) {
                    likes = commentObject.getInt("likes");
                }
                if (!commentObject.isNull("liked")) {
                    liked = true;
                }
                time = commentObject.getString("created_at");

                JSONObject userObject = commentObject.getJSONObject("user");
                String name = userObject.getString("name");
                String username = userObject.getString("username");
                String picture = "";
                if (!userObject.isNull("profile_pic")) {
                    JSONObject userPicObject = userObject.getJSONObject("profile_pic");
                    picture = userPicObject.getString("pic_name");
                }
                comments.add(new Comment(commentString, time, id, replyCount, likes, liked, name, username, picture));
            }
            feed.setCommentsList(comments);

            ArrayList<String> images = new ArrayList<>();
            JSONArray imagesArray = feedData.getJSONArray("images");
            for (int i = 0; i < imagesArray.length(); i++) {
                JSONObject imageObject = imagesArray.getJSONObject(i);
                String image = imageObject.getString("doc");
                images.add(image);
            }
            feed.setImages(images);

            ArrayList<String> youtubeVideos = new ArrayList<>();
            JSONArray youtubeVideosArray = feedData.getJSONArray("youtube_videos");
            for (int i = 0; i < youtubeVideosArray.length(); i++) {
                String youtubeVideo = youtubeVideosArray.getString(i);
                youtubeVideos.add(youtubeVideo);
            }
            feed.setYoutubeVideos(youtubeVideos);

            ArrayList<String> links = new ArrayList<>();
            JSONArray linksArray = feedData.getJSONArray("links");
            for (int i = 0; i < linksArray.length(); i++) {
                String link = linksArray.getString(i);
                links.add(link);
            }
            feed.setLinks(links);

            JSONObject userObject = feedData.getJSONObject("user");
            feed.setDoctorName(userObject.getString("name"));
            feed.setDoctorUsername(userObject.getString("username"));
            feed.setDoctorSpec(userObject.getString("speciality"));
            JSONObject pictureObject = userObject.getJSONObject("profile_pic");
            feed.setDoctorPicture(pictureObject.getString("pic_name"));

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        return feed;
    }

    // Fetches Query data from json and returns a Feed object
    public static Feed getQueryData(Feed feed, JSONObject feedJson) {

        JSONObject feedData = null;
        try {
            feedData = feedJson.getJSONObject("query");

            feed.setTitle(feedData.getString("question"));
            feed.setContent(feedData.getString("description"));
            feed.setTime(feedData.getString("created_at"));
            if (!feedData.isNull("followings")) {
                feed.setFollowings(feedData.getInt("followings"));
            }
            feed.setFollowed(!feedData.isNull("followed"));

            ArrayList<Comment> comments = new ArrayList<>();
            JSONArray commentsArray = feedData.getJSONArray("comments");
            feed.setComments(commentsArray.length());
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentObject = commentsArray.getJSONObject(i);
                String commentString = commentObject.getString("comment");
                int id = 0, replyCount = 0, likes = 0;
                boolean liked = false;
                String time = "";
                if (!commentObject.isNull("id")) {
                    id = commentObject.getInt("id");
                }
                if (!commentObject.isNull("reply_count")) {
                    replyCount = commentObject.getInt("reply_count");
                }
                if (!commentObject.isNull("likes")) {
                    likes = commentObject.getInt("likes");
                }
                if (!commentObject.isNull("liked")) {
                    liked = true;
                }
                time = commentObject.getString("created_at");

                JSONObject userObject = commentObject.getJSONObject("user");
                String name = userObject.getString("name");
                String username = userObject.getString("username");
                JSONObject userPicObject = userObject.getJSONObject("profile_pic");
                String picture = userPicObject.getString("pic_name");
                comments.add(new Comment(commentString, time, id, replyCount, likes, liked, name, username, picture));
            }
            feed.setCommentsList(comments);

            JSONObject answerObject = feedData.getJSONObject("answer");
            String content = answerObject.getString("content");
            int answerID = 0, replyCount = 0, likes = 0;
            boolean liked = false;
            String time = "";
            if (!answerObject.isNull("id")) {
                answerID = answerObject.getInt("id");
            }
            if (!answerObject.isNull("comments")) {
                replyCount = answerObject.getInt("comments");
            }
            if (!answerObject.isNull("likes")) {
                likes = answerObject.getInt("likes");
            }
            if (!answerObject.isNull("liked")) {
                liked = true;
            }
            time = answerObject.getString("created_at");

            JSONObject userObject = answerObject.getJSONObject("user");
            String name = userObject.getString("name");
            String username = userObject.getString("username");
            String speciality = userObject.getString("speciality");
            JSONObject userPicObject = userObject.getJSONObject("profile_pic");
            String picture = userPicObject.getString("pic_name");
            feed.setAnswer(new Answer(content, time, answerID, replyCount, likes, liked, name, username, speciality, picture));

            ArrayList<String> images = new ArrayList<>();
            JSONArray imagesArray = feedData.getJSONArray("images");
            for (int i = 0; i < imagesArray.length(); i++) {
                JSONObject imageObject = imagesArray.getJSONObject(i);
                String image = imageObject.getString("doc");
                images.add(image);
            }
            feed.setImages(images);

        } catch (JSONException e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

        return feed;
    }

    // performs action for like, follow, comment, share, etc
    public static class ActionFeed extends AsyncTask<Void, Void, Void> {

        String type, action, id, comment;

        public ActionFeed(String type, String action, String id, String comment) {
            this.type = type;
            this.action = action;
            this.id = id;
            this.comment = comment;
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            String idTag = "post";

            String url = "http://www.cureinstant.com/api";
            switch (type) {
                case "POST":
                case "BLOG":
                    url += "/post";
                    break;
                case "QUERY":
                    url += "/query";
                    break;
                case "ANSWER":
                    url += "/answer";
                    idTag = "answer";
                    break;
            }

            switch (action) {
                case "like":
                    url += "/like";
                    break;
                case "unlike":
                    url += "/liked";
                    break;
                case "follow":
                    url += "/follow";
                    break;
                case "unfollow":
                    url += "/unfollow";
                    break;
                case "share":
                    url += "/share";
                    break;
                case "comment":
                    url += "/comment/submit";
                    break;
            }

            RequestBody body;
            if (action.equals("comment")) {
                body = new FormBody.Builder()
                        .add(idTag, id)
                        .add("comment", comment)
                        .build();
            } else {
                body = new FormBody.Builder()
                        .add(idTag, id)
                        .build();
            }

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject feedJson = new JSONObject(result);
                String status = feedJson.getString("success");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }
    }

    // performs action for like, reply, etc on comment
    public static class ActionComment extends AsyncTask<Void, Void, Void> {

        String action, comment;
        int id;

        public ActionComment(String action, int id, String comment) {
            this.action = action;
            this.id = id;
            this.comment = comment;
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body;

            if (action.equals("reply")) {
                body = new FormBody.Builder()
                        .add("id", String.valueOf(id))
                        .add("comment", comment)
                        .build();
            } else {
                body = new FormBody.Builder()
                        .add("comment", String.valueOf(id))
                        .build();
            }
            String url = "http://www.cureinstant.com/api/post";

            switch (action) {
                case "like":
                    url += "/comment/like";
                    break;
                case "liked":
                    url += "/comment/liked";
                    break;
                case "reply":
                    url += "/comment/reply/submit";
                    break;
            }

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject feedJson = new JSONObject(result);
                String status = feedJson.getString("success");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }
    }

    // Fetch and display replies to the comment
    public static class GetComments extends AsyncTask<Void, Void, ArrayList<Comment>> {

        private Context context;
        private int id;
        private FragmentManager fragmentManager;
        private ProgressDialog progressDialog;

        public GetComments(Context context, int id, FragmentManager fragmentManager) {
            this.context = context;
            this.id = id;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<Comment> doInBackground(Void... params) {

            ArrayList<Comment> replies = new ArrayList<>();

            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("id", String.valueOf(id))
                    .build();

            String url = "http://www.cureinstant.com/api/post/comment/reply";

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject feedJson = new JSONObject(result);
                JSONArray repliesArray = feedJson.getJSONArray("replies");
                for (int i = 0; i < repliesArray.length(); i++) {
                    JSONObject replyObject = repliesArray.getJSONObject(i);
                    String commentString = replyObject.getString("comment");
                    int id = 0, replyCount = 0, likes = 0;
                    boolean liked = false;
                    String time = "";
                    if (!replyObject.isNull("id")) {
                        id = replyObject.getInt("id");
                    }
                    if (!replyObject.isNull("reply_count")) {
                        replyCount = replyObject.getInt("reply_count");
                    }
                    if (!replyObject.isNull("likes")) {
                        likes = replyObject.getInt("likes");
                    }
                    if (!replyObject.isNull("liked")) {
                        liked = true;
                    }
                    time = replyObject.getString("created_at");

                    JSONObject userObject = replyObject.getJSONObject("user");
                    String name = userObject.getString("name");
                    String username = userObject.getString("username");
                    String picture = "";
                    if (!userObject.isNull("profile_pic")) {
                        JSONObject userPicObject = userObject.getJSONObject("profile_pic");
                        picture = userPicObject.getString("pic_name");
                    }
                    replies.add(new Comment(commentString, time, id, replyCount, likes, liked, name, username, picture));
                }
                String status = feedJson.getString("success");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return replies;
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> comments) {
            super.onPostExecute(comments);
            progressDialog.dismiss();
            RepliesBottomSheetFragment repliesBottomSheetFragment = RepliesBottomSheetFragment.getInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("replies", comments);
            repliesBottomSheetFragment.setArguments(bundle);
            repliesBottomSheetFragment.show(fragmentManager, "Replies");
        }
    }

    // Follows and Unfollows the provided UserID
    public static class FollowDoctor extends AsyncTask<Void, Void, Void> {

        private boolean toFollow;
        private int userID;

        public FollowDoctor(boolean toFollow, int userID) {
            this.toFollow = toFollow;
            this.userID = userID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://www.cureinstant.com/api/connection/";
            if (toFollow) {
                url += "follow";
            } else {
                url += "unfollow";
            }

            RequestBody body = new FormBody.Builder()
                    .add("id", String.valueOf(userID))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }
    }

}
