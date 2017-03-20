package com.cureinstant.cureinstant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;

import static android.content.ContentValues.TAG;
import static com.cureinstant.cureinstant.R.id.post_menu_overflow;

public class FeedItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Feed feed;

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

        setupFeed();
    }

    private void setupFeed() {
        TextView type = (TextView) findViewById(R.id.post_type);
        TextView title = (TextView) findViewById(R.id.post_title);
        TextView content = (TextView) findViewById(R.id.post_desc);
        TextView time = (TextView) findViewById(R.id.post_time);
        View doctorContainer = findViewById(R.id.doctor_info_container);
        TextView doctorName = (TextView) findViewById(R.id.doctor_name);
        TextView doctorSpeciality = (TextView) findViewById(R.id.doctor_speciality);
        ImageView doctorPicture = (ImageView) findViewById(R.id.doctor_picture);
        TextView countHelpful = (TextView) findViewById(R.id.post_helpful_count);
        TextView countFollow = (TextView) findViewById(R.id.post_follow_count);
        TextView countComment = (TextView) findViewById(R.id.post_comment_count);
        TextView countShare = (TextView) findViewById(R.id.post_share_count);
        Button followButton = (Button) findViewById(R.id.post_follow_button);
        Button helpfulButton = (Button) findViewById(R.id.post_helpful_button);
        View menuOverflow = findViewById(post_menu_overflow);
        menuOverflow.setOnClickListener(this);

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
                time.setText(feedTime[0] + " days ago");
            } else {
                if (feedTime[1] > 0) {
                    time.setText(feedTime[1] + " hours ago");
                } else {
                    if (feedTime[2] > 0) {
                        time.setText(feedTime[2] + " minutes ago");
                    } else {
                        time.setText("just now");
                    }
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "onBindViewHolder: " + e);
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
        if (feed.getFollowings().equals("null")) {
            feed.setFollowings("0");
        }
        if (feed.getLikes().equals("null")) {
            feed.setLikes("0");
        }
        if (feed.getComments().equals("null")) {
            feed.setComments("0");
        }
        if (feed.getShares().equals("null")) {
            feed.setShares("0");
        }
        if (feed.getType().equals("QUERY")) {
            countFollow.setVisibility(View.VISIBLE);
            countHelpful.setVisibility(View.GONE);
            followButton.setVisibility(View.VISIBLE);
            helpfulButton.setVisibility(View.GONE);
            countShare.setVisibility(View.GONE);
            countFollow.setText(feed.getFollowings() + " Following");
            countComment.setText(feed.getComments() + " Comments");
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
            countHelpful.setText(feed.getLikes() + " Helpful");
            countComment.setText(feed.getComments() + " Comments");
            countShare.setText(feed.getShares() + " Shares");
            if (feed.isLiked()) {
                helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
                helpfulButton.setTextColor(this.getResources().getColor(R.color.white));
            } else {
                helpfulButton.setBackgroundColor(this.getResources().getColor(R.color.white));
                helpfulButton.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
}
