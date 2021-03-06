package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.activity.FeedItemActivity;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;
import com.google.firebase.crash.FirebaseCrash;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

// Adapter to show list of Feed items including all queries, posts, articles
public class FeedAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private Utilities.ActionFeed actionFeed;
    private Context context;
    private List<Feed> feedList;

    public FeedAdapter(Context context, List<Feed> feedList, RecyclerView recyclerView) {
        this.context = context;
        this.feedList = feedList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return feedList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_feed_post, parent, false);
            return new FeedAdapter.MyFeedViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_progressbar, parent, false);
            return new FeedAdapter.ProgressViewHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_feed_post, parent, false);

        return new FeedAdapter.MyFeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyFeedViewHolder) {

            final Feed feed = feedList.get(position);
            if (feed.getType().equals("POST")) {
                ((MyFeedViewHolder) holder).type.setText(R.string.title_post);
            } else if (feed.getType().equals("BLOG")) {
                ((MyFeedViewHolder) holder).type.setText(R.string.title_article);
            } else if (feed.getType().equals("QUERY")) {
                ((MyFeedViewHolder) holder).type.setText(R.string.title_question);
            } else {
                ((MyFeedViewHolder) holder).type.setVisibility(View.GONE);
            }
            if (feed.getActionName().equals("")) {
                ((MyFeedViewHolder) holder).actionContainer.setVisibility(View.GONE);
            } else {
                ((MyFeedViewHolder) holder).actionContainer.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).actionName.setText(feed.getActionName());
                ((MyFeedViewHolder) holder).actionType.setText(feed.getActionType());
            }
            if (feed.getTitle().equals("")) {
                ((MyFeedViewHolder) holder).title.setVisibility(View.GONE);
            } else {
                ((MyFeedViewHolder) holder).title.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).title.setText(feed.getTitle());
            }
            if (feed.getContent().equals("")) {
                ((MyFeedViewHolder) holder).content.setVisibility(View.GONE);
            } else {
                ((MyFeedViewHolder) holder).content.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).content.setText(feed.getContent());
            }
            try {
                long[] feedTime = Utilities.getDateDifference(feed.getTime());
                if (feedTime[0] > 0) {
                    ((MyFeedViewHolder) holder).time.setText(String.format(context.getString(R.string.time_days_count), feedTime[0]));
                } else {
                    if (feedTime[1] > 0) {
                        ((MyFeedViewHolder) holder).time.setText(String.format(context.getString(R.string.time_hours_count), feedTime[1]));
                    } else {
                        if (feedTime[2] > 0) {
                            ((MyFeedViewHolder) holder).time.setText(String.format(context.getString(R.string.time_minutes_count), feedTime[2]));
                        } else {
                            ((MyFeedViewHolder) holder).time.setText(R.string.time_just_now);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            if (feed.getType().equals("QUERY") && !feed.getActionType().equals(" answered this")) {
                ((MyFeedViewHolder) holder).doctorContainer.setVisibility(View.GONE);
            } else {
                ((MyFeedViewHolder) holder).doctorContainer.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).doctorName.setText(feed.getDoctorName());
                ((MyFeedViewHolder) holder).doctorSpeciality.setText(feed.getDoctorSpec());
                String imageURL = Utilities.profilePicSmallBaseUrl + feed.getDoctorPicture();
                Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(((MyFeedViewHolder) holder).doctorPicture);
            }
            if (feed.getType().equals("QUERY")) {
                ((MyFeedViewHolder) holder).countFollow.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).countHelpful.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).followButton.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).helpfulButton.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).countShare.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).shareButton.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).countFollow.setText(String.format(context.getString(R.string.following_count), feed.getFollowings()));
                ((MyFeedViewHolder) holder).countComment.setText(String.format(context.getString(R.string.comments_count), feed.getComments()));
                if (feed.isFollowed()) {
                    ((MyFeedViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    ((MyFeedViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    ((MyFeedViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                    ((MyFeedViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                }
            } else {
                ((MyFeedViewHolder) holder).countFollow.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).countHelpful.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).followButton.setVisibility(View.GONE);
                ((MyFeedViewHolder) holder).helpfulButton.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).countShare.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).shareButton.setVisibility(View.VISIBLE);
                ((MyFeedViewHolder) holder).countHelpful.setText(String.format(context.getString(R.string.helpful_count), feed.getLikes()));
                ((MyFeedViewHolder) holder).countComment.setText(String.format(context.getString(R.string.comments_count), feed.getComments()));
                ((MyFeedViewHolder) holder).countShare.setText(String.format(context.getString(R.string.shares_count), feed.getShares()));
                if (feed.isLiked()) {
                    ((MyFeedViewHolder) holder).helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    ((MyFeedViewHolder) holder).helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    ((MyFeedViewHolder) holder).helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                    ((MyFeedViewHolder) holder).helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
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
                                ((MyFeedViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                                ((MyFeedViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                feed.setFollowed(false);
                                feed.setFollowings(feed.getFollowings() - 1);
                            } else {
                                actionFeed = new Utilities.ActionFeed(feed.getType(), "follow", feed.getId(), "");
                                actionFeed.execute();
                                ((MyFeedViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                ((MyFeedViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.white));
                                feed.setFollowed(true);
                                feed.setFollowings(feed.getFollowings() + 1);
                            }
                            ((MyFeedViewHolder) holder).countFollow.setText(String.format(context.getString(R.string.following_count), feed.getFollowings()));
                            break;
                        case R.id.post_helpful_button:
                            if (feed.isLiked()) {
                                actionFeed = new Utilities.ActionFeed(feed.getType(), "unlike", feed.getId(), "");
                                actionFeed.execute();
                                ((MyFeedViewHolder) holder).helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                                ((MyFeedViewHolder) holder).helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                feed.setLiked(false);
                                feed.setLikes(feed.getLikes() - 1);
                            } else {
                                actionFeed = new Utilities.ActionFeed(feed.getType(), "like", feed.getId(), "");
                                actionFeed.execute();
                                ((MyFeedViewHolder) holder).helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                ((MyFeedViewHolder) holder).helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
                                feed.setLiked(true);
                                feed.setLikes(feed.getLikes() + 1);
                            }
                            ((MyFeedViewHolder) holder).countHelpful.setText(String.format(context.getString(R.string.helpful_count), feed.getLikes()));
                            ((MyFeedViewHolder) holder).countComment.setText(String.format(context.getString(R.string.comments_count), feed.getComments()));
                            ((MyFeedViewHolder) holder).countShare.setText(String.format(context.getString(R.string.shares_count), feed.getShares()));
                            break;
                        case R.id.post_share_button:
                            actionFeed = new Utilities.ActionFeed(feed.getType(), "share", feed.getId(), "");
                            actionFeed.execute();
                            Toast.makeText(context, feed.getType() + context.getString(R.string.text_shared_2), Toast.LENGTH_SHORT).show();
                            feed.setShares(feed.getShares() + 1);
                            ((MyFeedViewHolder) holder).countHelpful.setText(String.format(context.getString(R.string.helpful_count), feed.getLikes()));
                            ((MyFeedViewHolder) holder).countComment.setText(String.format(context.getString(R.string.comments_count), feed.getComments()));
                            ((MyFeedViewHolder) holder).countShare.setText(String.format(context.getString(R.string.shares_count), feed.getShares()));
                            break;
                        case R.id.post_comment_button:
                            Utilities.commentDialog(context, feed.getType(), feed.getId());
                            feed.setComments(feed.getComments() + 1);
                            ((MyFeedViewHolder) holder).countComment.setText(String.format(context.getString(R.string.comments_count), feed.getComments()));
                            break;
                        case R.id.doctor_info_container:
                            Intent intent = new Intent(context, DoctorProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("name", feed.getDoctorName());
                            b.putString("username", feed.getDoctorUsername());
                            intent.putExtras(b);
                            context.startActivity(intent);
                            break;
                    }
                }

            };

            ((MyFeedViewHolder) holder).title.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).content.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).feedContainer.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).doctorContainer.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).followButton.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).helpfulButton.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).commentButton.setOnClickListener(onClickListener);
            ((MyFeedViewHolder) holder).shareButton.setOnClickListener(onClickListener);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    private class MyFeedViewHolder extends RecyclerView.ViewHolder {

        TextView type, actionName, actionType;
        TextView title, content, time, doctorName, doctorSpeciality;
        TextView countFollow, countHelpful, countComment, countShare;
        ImageView doctorPicture;
        View actionContainer, doctorContainer, feedContainer;
        Button followButton, helpfulButton, commentButton, shareButton;

        MyFeedViewHolder(View itemView) {
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
            feedContainer = itemView.findViewById(R.id.feed_post_container);
        }
    }
}
