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

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by lokeshsaini94 on 24-03-2017.
 */

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

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
        Log.e(TAG, "onBindViewHolder: comment.getComment() " + comment.getComment() );

        holder.comment.setText(comment.getComment());

        try {
            long[] time = Utilities.getDateDifference(comment.getTime());
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
            e.printStackTrace();
        }

        holder.countHelpful.setText(comment.getLikes() + " likes");
        holder.countReplies.setText(comment.getReplyCount() + " replies");

        // TODO: 24-03-2017 Handle user name and image here
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView comment, time, doctorName;
        TextView countHelpful, countReplies;
        ImageView doctorPicture;
        View menuOverflow;
        Button helpfulButton, replyButton;

        public ItemViewHolder(View itemView) {
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
