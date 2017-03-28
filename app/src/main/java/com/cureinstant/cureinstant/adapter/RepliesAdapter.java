package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 28-03-2017.
 */

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Comment> replies;

    public RepliesAdapter(Context context, ArrayList<Comment> replies) {
        this.context = context;
        this.replies = replies;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_comment, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final Comment reply = replies.get(position);

        holder.comment.setText(reply.getComment());

        try {
            long[] feedTime = Utilities.getDateDifference(reply.getTime());
            if (feedTime[0] > 0) {
                holder.time.setText(String.format(context.getString(R.string.time_days_count), feedTime[0]));
            } else {
                if (feedTime[1] > 0) {
                    holder.time.setText(String.format(context.getString(R.string.time_hours_count), feedTime[1]));
                } else {
                    if (feedTime[2] > 0) {
                        holder.time.setText(String.format(context.getString(R.string.time_minutes_count), feedTime[2]));
                    } else {
                        holder.time.setText(R.string.time_just_now);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.countHelpful.setText(String.format(context.getString(R.string.helpful_count), reply.getLikes()));
        holder.countReplies.setVisibility(View.GONE);
        if (reply.isLiked()) {
            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.replyButton.setVisibility(View.GONE);

        holder.doctorName.setText(reply.getName());
        String imageURL = Utilities.profilePicSmallBaseUrl + reply.getPicture();
        Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.doctorPicture);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.comment_helpful_button:
                        if (reply.isLiked()) {
                            Utilities.ActionComment actionComment = new Utilities.ActionComment("liked", reply.getId(), null);
                            actionComment.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            reply.setLiked(false);
                            reply.setLikes(reply.getLikes() - 1);
                        } else {
                            Utilities.ActionComment actionComment = new Utilities.ActionComment("like", reply.getId(), null);
                            actionComment.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
                            reply.setLiked(true);
                            reply.setLikes(reply.getLikes() + 1);
                        }
                        holder.countHelpful.setText(String.format(context.getString(R.string.helpful_count), reply.getLikes()));
                        break;
                }
            }
        };

        holder.helpfulButton.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView comment, time, doctorName;
        TextView countHelpful, countReplies;
        ImageView doctorPicture;
        View menuOverflow;
        Button helpfulButton, replyButton;

        ItemViewHolder(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.comment_content);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            doctorName = (TextView) itemView.findViewById(R.id.user_name);
            doctorPicture = (ImageView) itemView.findViewById(R.id.user_picture);
            countHelpful = (TextView) itemView.findViewById(R.id.comment_helpful_count);
            countReplies = (TextView) itemView.findViewById(R.id.comment_reply_count);
            menuOverflow = itemView.findViewById(R.id.comment_menu_overflow);
            helpfulButton = (Button) itemView.findViewById(R.id.comment_helpful_button);
            replyButton = (Button) itemView.findViewById(R.id.comment_reply_button);
        }
    }
}
