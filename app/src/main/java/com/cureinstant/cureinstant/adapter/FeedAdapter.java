package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.FeedItemActivity;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyFeedViewHolder> {

    private Utilities.ActionFeed actionFeed;
    private Context context;
    private List<Feed> feedList;

    public FeedAdapter(Context context, List<Feed> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public MyFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_feed_post, parent, false);

        return new FeedAdapter.MyFeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyFeedViewHolder holder, int position) {
        final Feed feed = feedList.get(position);
        if (feed.getType().equals("POST")) {
            holder.type.setText("Post");
        } else if (feed.getType().equals("BLOG")) {
            holder.type.setText("Article");
        } else if (feed.getType().equals("QUERY")) {
            holder.type.setText("Question");
        } else {
            holder.type.setVisibility(View.GONE);
        }
        if (feed.getActionName().equals("")) {
            holder.actionContainer.setVisibility(View.GONE);
        } else {
            holder.actionContainer.setVisibility(View.VISIBLE);
            holder.actionName.setText(feed.getActionName());
            holder.actionType.setText(feed.getActionType());
        }
        if (feed.getTitle().equals("")) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(feed.getTitle());
        }
        if (feed.getContent().equals("")) {
            holder.content.setVisibility(View.GONE);
        } else {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(feed.getContent());
        }
        try {
            long[] time = Utilities.getDateDifference(feed.getTime());
            if (time[0] > 0) {
                holder.time.setText(time[0] + " days ago");
            } else {
                if (time[1] > 0) {
                    holder.time.setText(time[1] + " hours ago");
                } else {
                    if (time[2] > 0) {
                        holder.time.setText(time[2] + " minutes ago");
                    } else {
                        holder.time.setText("just now");
                    }
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "onBindViewHolder: " + e);
        }
        if (feed.getType().equals("QUERY") && !feed.getActionType().equals(" answered this")) {
            holder.doctorContainer.setVisibility(View.GONE);
        } else {
            holder.doctorContainer.setVisibility(View.VISIBLE);
            holder.doctorName.setText(feed.getDoctorName());
            holder.doctorSpeciality.setText(feed.getDoctorSpec());
            String imageURL = Utilities.profilePicSmallBaseUrl + feed.getDoctorPicture();
            Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.doctorPicture);
        }
        if (feed.getType().equals("QUERY")) {
            holder.countFollow.setVisibility(View.VISIBLE);
            holder.countHelpful.setVisibility(View.GONE);
            holder.followButton.setVisibility(View.VISIBLE);
            holder.helpfulButton.setVisibility(View.GONE);
            holder.countShare.setVisibility(View.GONE);
            holder.shareButton.setVisibility(View.GONE);
            holder.countFollow.setText(feed.getFollowings() + " Following");
            holder.countComment.setText(feed.getComments() + " Comments");
            if (feed.isFollowed()) {
                holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.followButton.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        } else {
            holder.countFollow.setVisibility(View.GONE);
            holder.countHelpful.setVisibility(View.VISIBLE);
            holder.followButton.setVisibility(View.GONE);
            holder.helpfulButton.setVisibility(View.VISIBLE);
            holder.countShare.setVisibility(View.VISIBLE);
            holder.shareButton.setVisibility(View.VISIBLE);
            holder.countHelpful.setText(feed.getLikes() + " Helpful");
            holder.countComment.setText(feed.getComments() + " Comments");
            holder.countShare.setText(feed.getShares() + " Shares");
            if (feed.isLiked()) {
                holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.post_title:
                    case R.id.post_desc:
                    case R.id.feed_post_container:
                        Intent feedIntent = new Intent(context, FeedItemActivity.class);
                        feedIntent.putExtra("feed_item", feed);
                        context.startActivity(feedIntent);
                        break;
                    case R.id.post_follow_button:
                        if (feed.isFollowed()) {
                            actionFeed = new Utilities.ActionFeed(feed.getType(), "unfollow", feed.getId(), "");
                            actionFeed.execute();
                            holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                            holder.followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            feed.setFollowed(false);
                            feed.setFollowings(feed.getFollowings() - 1);
                        } else {
                            actionFeed = new Utilities.ActionFeed(feed.getType(), "follow", feed.getId(), "");
                            actionFeed.execute();
                            holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                            holder.followButton.setTextColor(context.getResources().getColor(R.color.white));
                            feed.setFollowed(true);
                            feed.setFollowings(feed.getFollowings() + 1);
                        }
                        holder.countFollow.setText(feed.getFollowings() + " Following");
                        break;
                    case R.id.post_helpful_button:
                        if (feed.isLiked()) {
                            actionFeed = new Utilities.ActionFeed(feed.getType(), "unlike", feed.getId(), "");
                            actionFeed.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            feed.setLiked(false);
                            feed.setLikes(feed.getLikes() - 1);
                        } else {
                            actionFeed = new Utilities.ActionFeed(feed.getType(), "like", feed.getId(), "");
                            actionFeed.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
                            feed.setLiked(true);
                            feed.setLikes(feed.getLikes() + 1);
                        }
                        holder.countHelpful.setText(feed.getLikes() + " Helpful");
                        break;
                    case R.id.post_share_button:
                        actionFeed = new Utilities.ActionFeed(feed.getType(), "share", feed.getId(), "");
                        actionFeed.execute();
                        Toast.makeText(context, feed.getType() + " shared", Toast.LENGTH_SHORT).show();
                        feed.setShares(feed.getShares() + 1);
                        holder.countShare.setText(feed.getShares() + " Shares");
                        break;
                    case R.id.post_comment_button:
                        Utilities.commentDialog(context, feed);
                        feed.setComments(feed.getComments() + 1);
                        holder.countComment.setText(feed.getComments() + " Comments");
                        break;
                    case R.id.post_menu_overflow:
                        ImageButton menuButton = (ImageButton) v;
                        PopupMenu popupMenu = new PopupMenu(context, menuButton);
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
        };

        holder.title.setOnClickListener(onClickListener);
        holder.content.setOnClickListener(onClickListener);
        holder.feedContainer.setOnClickListener(onClickListener);
        holder.followButton.setOnClickListener(onClickListener);
        holder.helpfulButton.setOnClickListener(onClickListener);
        holder.commentButton.setOnClickListener(onClickListener);
        holder.shareButton.setOnClickListener(onClickListener);
        holder.menuOverflow.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class MyFeedViewHolder extends RecyclerView.ViewHolder {

        TextView type, actionName, actionType;
        TextView title, content, time, doctorName, doctorSpeciality;
        TextView countFollow, countHelpful, countComment, countShare;
        ImageView doctorPicture;
        View actionContainer, doctorContainer, feedContainer, menuOverflow;
        Button followButton, helpfulButton, commentButton, shareButton;

        public MyFeedViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.post_type);
            actionContainer = itemView.findViewById(R.id.post_action_container);
            actionName = (TextView) itemView.findViewById(R.id.post_action_name);
            actionType = (TextView) itemView.findViewById(R.id.post_action_type);
            title = (TextView) itemView.findViewById(R.id.post_title);
            content = (TextView) itemView.findViewById(R.id.post_desc);
            time = (TextView) itemView.findViewById(R.id.post_time);
            doctorContainer = itemView.findViewById(R.id.doctor_info_container);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_name);
            doctorSpeciality = (TextView) itemView.findViewById(R.id.doctor_speciality);
            doctorPicture = (ImageView) itemView.findViewById(R.id.doctor_picture);
            countHelpful = (TextView) itemView.findViewById(R.id.post_helpful_count);
            countFollow = (TextView) itemView.findViewById(R.id.post_follow_count);
            countComment = (TextView) itemView.findViewById(R.id.post_comment_count);
            countShare = (TextView) itemView.findViewById(R.id.post_share_count);
            followButton = (Button) itemView.findViewById(R.id.post_follow_button);
            helpfulButton = (Button) itemView.findViewById(R.id.post_helpful_button);
            commentButton = (Button) itemView.findViewById(R.id.post_comment_button);
            shareButton = (Button) itemView.findViewById(R.id.post_share_button);
            menuOverflow = itemView.findViewById(R.id.post_menu_overflow);
            feedContainer = itemView.findViewById(R.id.feed_post_container);
        }
    }
}
