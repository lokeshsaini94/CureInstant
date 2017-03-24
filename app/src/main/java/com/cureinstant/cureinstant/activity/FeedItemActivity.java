package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.CommentAdapter;
import com.cureinstant.cureinstant.adapter.FeedImagesAdapter;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.R.id.post_menu_overflow;
import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class FeedItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Feed feed;
    private Button followButton;
    private Button helpfulButton;

    private TextView countHelpful;
    private TextView countFollow;
    private TextView countShare;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_item);

        Intent i = getIntent();
        feed = i.getParcelableExtra("feed_item");

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        switch (feed.getType()) {
            case "BLOG":
                bar.setTitle("Article");
                break;
            case "POST":
                bar.setTitle("Post");
                break;
            case "QUERY":
                bar.setTitle("Question");
                break;
            default:
                bar.setTitle("Feed");
                break;
        }
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rootView = findViewById(R.id.rootLayout);
        countHelpful = (TextView) findViewById(R.id.post_helpful_count);
        countFollow = (TextView) findViewById(R.id.post_follow_count);
        countShare = (TextView) findViewById(R.id.post_share_count);
        followButton = (Button) findViewById(R.id.post_follow_button);
        helpfulButton = (Button) findViewById(R.id.post_helpful_button);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.feed_item_refresh);

        followButton.setOnClickListener(this);
        helpfulButton.setOnClickListener(this);
        followButton.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData refreshData = new RefreshData();
                refreshData.execute();
            }
        });
        RefreshData refreshData = new RefreshData();
        refreshData.execute();
    }

    private void setupFeed() {

        TextView title = (TextView) findViewById(R.id.post_title);
        TextView content = (TextView) findViewById(R.id.post_desc);
        TextView time = (TextView) findViewById(R.id.post_time);
        View doctorContainer = findViewById(R.id.doctor_info_container);
        TextView doctorName = (TextView) findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) findViewById(R.id.doctor_speciality);
        ImageView doctorPicture = (ImageView) findViewById(R.id.doctor_picture);
        TextView countComment = (TextView) findViewById(R.id.post_comment_count);
        Button commentButton = (Button) findViewById(R.id.post_comment_button);
        Button shareButton = (Button) findViewById(R.id.post_share_button);
        View menuOverflow = findViewById(post_menu_overflow);
        RecyclerView imagesRecyclerView = (RecyclerView) findViewById(R.id.post_images_list);
        RecyclerView commentsRecyclerView = (RecyclerView) findViewById(R.id.post_comments_list);
        View postAnswer = findViewById(R.id.post_answer);
        View postAnswerContainer = findViewById(R.id.post_answer_container);
        View commentsListText = findViewById(R.id.post_comments_list_text);

        if (feed.getImages().isEmpty()) {
            imagesRecyclerView.setVisibility(View.GONE);
        } else {
            imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            FeedImagesAdapter mAdapter = new FeedImagesAdapter(this, feed.getType(), feed.getImages());
            imagesRecyclerView.setAdapter(mAdapter);
            imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        TextView type = (TextView) findViewById(R.id.post_type);
        switch (feed.getType()) {
            case "BLOG":
                type.setText("Article");
                break;
            case "POST":
                type.setText("Post");
                break;
            case "QUERY":
                type.setText("Question");
                break;
            default:
                type.setText("Feed");
                break;
        }

        if (feed.getTitle().equals("")) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            title.setText(feed.getTitle());
        }
        if (feed.getContent().equals("")) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(feed.getContent());
        }
        try {
            long[] feedTime = Utilities.getDateDifference(feed.getTime());
            if (feedTime[0] > 0) {
                time.setText(String.format(getString(R.string.time_days_count), feedTime[0]));
            } else {
                if (feedTime[1] > 0) {
                    time.setText(String.format(getString(R.string.time_hours_count), feedTime[1]));
                } else {
                    if (feedTime[2] > 0) {
                        time.setText(String.format(getString(R.string.time_minutes_count), feedTime[2]));
                    } else {
                        time.setText(R.string.time_just_now);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (feed.getType().equals("QUERY") && !feed.getActionType().equals(" answered this")) {
            doctorContainer.setVisibility(View.GONE);
        } else {
            doctorContainer.setVisibility(View.VISIBLE);
            doctorName.setText(feed.getDoctorName());
            doctorSpeciality.setText(feed.getDoctorSpec());
            String imageURL = Utilities.profilePicSmallBaseUrl + feed.getDoctorPicture();
            Glide.with(this).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(doctorPicture);
        }
        if (feed.getType().equals("QUERY")) {
            countFollow.setVisibility(View.VISIBLE);
            countHelpful.setVisibility(View.GONE);
            followButton.setVisibility(View.VISIBLE);
            helpfulButton.setVisibility(View.GONE);
            countShare.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
            countFollow.setText(String.format(getString(R.string.following_count), feed.getFollowings()));
            countComment.setText(String.format(getString(R.string.comments_count), feed.getComments()));
            if (feed.isFollowed()) {
                followButton.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                followButton.setTextColor(this.getResources().getColor(R.color.white));
            } else {
                followButton.setBackgroundColor(this.getResources().getColor(R.color.white));
                followButton.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            }
        } else {
            countFollow.setVisibility(View.GONE);
            countHelpful.setVisibility(View.VISIBLE);
            followButton.setVisibility(View.GONE);
            helpfulButton.setVisibility(View.VISIBLE);
            countShare.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
            countHelpful.setText(String.format(getString(R.string.helpful_count), feed.getLikes()));
            countComment.setText(String.format(getString(R.string.comments_count), feed.getComments()));
            countShare.setText(String.format(getString(R.string.shares_count), feed.getShares()));
            if (feed.isLiked()) {
                helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                helpfulButton.setTextColor(this.getResources().getColor(R.color.white));
            } else {
                helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.white));
                helpfulButton.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            }
        }

        if (feed.getType().equals("QUERY")) {
            postAnswerContainer.setVisibility(View.VISIBLE);
            if (feed.getAnswer() != null) {
                // TODO: 24-03-2017 Handle answer content here
            }
        }
        if (feed.getCommentsList().isEmpty()) {
            commentsRecyclerView.setVisibility(View.GONE);
            commentsListText.setVisibility(View.GONE);
        } else {
            commentsRecyclerView.setVisibility(View.VISIBLE);
            commentsListText.setVisibility(View.VISIBLE);
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            commentsRecyclerView.setLayoutManager(mLayoutManager);
            CommentAdapter commentAdapter = new CommentAdapter(this, feed.getCommentsList());
            commentsRecyclerView.setAdapter(commentAdapter);
            commentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            commentAdapter.notifyDataSetChanged();
        }

        menuOverflow.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Utilities.ActionFeed actionFeed;
        switch (v.getId()) {
            case R.id.post_follow_button:
                if (feed.isFollowed()) {
                    actionFeed = new Utilities.ActionFeed(feed.getType(), "unfollow", feed.getId(), "");
                    actionFeed.execute();
                    followButton.setBackgroundColor(this.getResources().getColor(R.color.white));
                    followButton.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    feed.setFollowed(false);
                    feed.setFollowings(feed.getFollowings() - 1);
                } else {
                    actionFeed = new Utilities.ActionFeed(feed.getType(), "follow", feed.getId(), "");
                    actionFeed.execute();
                    followButton.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                    followButton.setTextColor(this.getResources().getColor(R.color.white));
                    feed.setFollowed(true);
                    feed.setFollowings(feed.getFollowings() + 1);
                }
                countFollow.setText(String.format(getString(R.string.following_count), feed.getFollowings()));
                break;
            case R.id.post_helpful_button:
                if (feed.isLiked()) {
                    actionFeed = new Utilities.ActionFeed(feed.getType(), "unlike", feed.getId(), "");
                    actionFeed.execute();
                    helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.white));
                    helpfulButton.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    feed.setLiked(false);
                    feed.setLikes(feed.getLikes() - 1);
                } else {
                    actionFeed = new Utilities.ActionFeed(feed.getType(), "like", feed.getId(), "");
                    actionFeed.execute();
                    helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                    helpfulButton.setTextColor(this.getResources().getColor(R.color.white));
                    feed.setLiked(true);
                    feed.setLikes(feed.getLikes() + 1);
                }
                countHelpful.setText(String.format(getString(R.string.helpful_count), feed.getLikes()));
                break;
            case R.id.post_comment_button:
                Utilities.commentDialog(this, feed.getType(), feed.getId());
                break;
            case R.id.post_share_button:
                actionFeed = new Utilities.ActionFeed(feed.getType(), "share", feed.getId(), "");
                actionFeed.execute();
                Toast.makeText(this, feed.getType() + " shared", Toast.LENGTH_SHORT).show();
                feed.setShares(feed.getShares() + 1);
                countShare.setText(String.format(getString(R.string.shares_count), feed.getShares()));
                break;
            case R.id.post_menu_overflow:
                ImageButton menuButton = (ImageButton) v;
                PopupMenu popupMenu = new PopupMenu(this, menuButton);
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


    // Fetches and Sets Feed item data from api call
    private class RefreshData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[0]);

            String url = "http://www.cureinstant.com/api/";

            switch (feed.getType()) {
                case "BLOG":
                    url += "blog/";
                    break;
                case "POST":
                    url += "post/";
                    break;
                case "QUERY":
                    url += "query/";
                    break;
            }

            url += feed.getId();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();

                JSONObject feedJson = new JSONObject(s);

                switch (feed.getType()) {
                    case "BLOG":
                        feed = Utilities.getBlogData(feed, feedJson);
                        break;
                    case "POST":
                        feed = Utilities.getPostData(feed, feedJson);
                        break;
                    case "QUERY":
                        feed = Utilities.getQueryData(feed, feedJson);
                        break;
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setupFeed();
            swipeRefreshLayout.setRefreshing(false);
            if (rootView.getVisibility() != View.VISIBLE) {
                rootView.setVisibility(View.VISIBLE);
            }
        }
    }
}
