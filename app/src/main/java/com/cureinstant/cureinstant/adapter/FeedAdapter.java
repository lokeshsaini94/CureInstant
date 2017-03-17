package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyFeedViewHolder> {

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
    public void onBindViewHolder(MyFeedViewHolder holder, int position) {
        Feed feed = feedList.get(position);
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
        // TODO: 16-03-2017 fix formating of time
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
            holder.countFollow.setVisibility(View.VISIBLE);
            holder.countHelpful.setVisibility(View.GONE);
            holder.followButton.setVisibility(View.VISIBLE);
            holder.helpfulButton.setVisibility(View.GONE);
            holder.countFollow.setText(feed.getFollowings() + " Following");
            holder.countComment.setText(feed.getComments() + " Comments");
            holder.countShare.setText(feed.getShares() + " Shares");
        } else {
            holder.countFollow.setVisibility(View.GONE);
            holder.countHelpful.setVisibility(View.VISIBLE);
            holder.followButton.setVisibility(View.GONE);
            holder.helpfulButton.setVisibility(View.VISIBLE);
            holder.countHelpful.setText(feed.getLikes() + " Helpful");
            holder.countComment.setText(feed.getComments() + " Comments");
            holder.countShare.setText(feed.getShares() + " Shares");
        }
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
        View actionContainer, doctorContainer;
        Button followButton, helpfulButton;

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
        }
    }
}
