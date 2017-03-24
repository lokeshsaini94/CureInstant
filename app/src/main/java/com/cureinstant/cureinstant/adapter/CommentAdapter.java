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
 * Created by lokeshsaini94 on 24-03-2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_comment, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        Comment comment = comments.get(position);

        holder.comment.setText(comment.getComment());

        try {
            long[] feedTime = Utilities.getDateDifference(comment.getTime());
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

        holder.countHelpful.setText(String.format(context.getString(R.string.helpful_count), comment.getLikes()));
        holder.countReplies.setText(String.format(context.getString(R.string.replies_count), comment.getReplyCount()));

        holder.doctorName.setText(comment.getName());
        String imageURL = Utilities.profilePicSmallBaseUrl + comment.getPicture();
        Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.doctorPicture);

        // TODO: 24-03-2017 Handle user name and image here
    }

    @Override
    public int getItemCount() {
        return comments.size();
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
