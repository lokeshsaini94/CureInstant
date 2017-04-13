package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.model.Comment;
import com.cureinstant.cureinstant.util.Utilities;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 24-03-2017.
 */

// Adapter to show list of comments on a given query, article, post
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Comment> comments;
    private FragmentManager fragmentManager;

    public CommentAdapter(Context context, ArrayList<Comment> comments, FragmentManager fragmentManager) {
        this.context = context;
        this.comments = comments;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_comment, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final Comment comment = comments.get(position);

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
        if (comment.isLiked()) {
            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        holder.doctorName.setText(comment.getName());
        String imageURL = Utilities.profilePicSmallBaseUrl + comment.getPicture();
        Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.doctorPicture);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.feed_comment_container:
                        if (comment.getReplyCount() > 0) {
                            Utilities.GetComments getComments = new Utilities.GetComments(context, comment.getId(), fragmentManager);
                            getComments.execute();
                        }
                        break;
                    case R.id.comment_helpful_button:
                        if (comment.isLiked()) {
                            Utilities.ActionComment actionComment = new Utilities.ActionComment("liked", comment.getId(), null);
                            actionComment.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            comment.setLiked(false);
                            comment.setLikes(comment.getLikes() - 1);
                        } else {
                            Utilities.ActionComment actionComment = new Utilities.ActionComment("like", comment.getId(), null);
                            actionComment.execute();
                            holder.helpfulButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                            holder.helpfulButton.setTextColor(context.getResources().getColor(R.color.white));
                            comment.setLiked(true);
                            comment.setLikes(comment.getLikes() + 1);
                        }
                        holder.countHelpful.setText(String.format(context.getString(R.string.helpful_count), comment.getLikes()));
                        break;
                    case R.id.comment_reply_button:
                        final EditText edittext = new EditText(context);
                        edittext.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                        final AlertDialog.Builder nameDialog = new AlertDialog.Builder(context);
                        nameDialog.setTitle("Enter your reply");
                        nameDialog.setView(edittext);

                        nameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String editTextValue = edittext.getText().toString();
                                Utilities.ActionComment actionComment = new Utilities.ActionComment("reply", comment.getId(), editTextValue);
                                actionComment.execute();
                                Toast.makeText(context, "Reply posted", Toast.LENGTH_SHORT).show();

                            }
                        });

                        nameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        nameDialog.show();
                        comment.setReplyCount(comment.getReplyCount() + 1);
                        holder.countReplies.setText(String.format(context.getString(R.string.replies_count), comment.getReplyCount()));
                        break;
                    case R.id.user_name:
                    case R.id.user_picture:
                        Intent intent = new Intent(context, DoctorProfileActivity.class);
                        Bundle b = new Bundle();
                        b.putString("name", comment.getName());
                        b.putString("username", comment.getUsername());
                        intent.putExtras(b);
                        context.startActivity(intent);
                        break;
                }
            }
        };

        holder.helpfulButton.setOnClickListener(onClickListener);
        holder.replyButton.setOnClickListener(onClickListener);
        holder.rootView.setOnClickListener(onClickListener);
        holder.doctorName.setOnClickListener(onClickListener);
        holder.doctorPicture.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView comment, time, doctorName;
        TextView countHelpful, countReplies;
        ImageView doctorPicture;
        View menuOverflow, rootView;
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
            rootView = itemView.findViewById(R.id.feed_comment_container);
        }
    }
}
