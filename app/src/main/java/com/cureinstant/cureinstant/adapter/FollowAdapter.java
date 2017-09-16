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

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.model.Follow;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 04-04-2017.
 */

// Adapter to show list of followings and followers of the user
public class FollowAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private Context context;
    private String type;
    private ArrayList<Follow> follows;

    public FollowAdapter(Context context, String type, ArrayList<Follow> follows, RecyclerView recyclerView) {
        this.context = context;
        this.type = type;
        this.follows = follows;

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
        return follows.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_follow_profile, parent, false);
            return new FollowAdapter.ItemViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_progressbar, parent, false);
            return new FollowAdapter.ProgressViewHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_follow_profile, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            ((ItemViewHolder) holder).name.setText(follows.get(position).getName());
            ((ItemViewHolder) holder).speciality.setText(follows.get(position).getSpeciality());
            String imageURL = Utilities.profilePicSmallBaseUrl + follows.get(position).getPicture();
            Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(((ItemViewHolder) holder).picture);

            if (!type.equals("followers") || follows.get(position).isFollowing()) {
                ((ItemViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ((ItemViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.white));
                ((ItemViewHolder) holder).followButton.setText(R.string.title_unfollow);
            } else {
                ((ItemViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                ((ItemViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                ((ItemViewHolder) holder).followButton.setText(R.string.title_follow);
            }


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.doctor_follow_button:
                            if (type.equals("followers") && !follows.get(holder.getAdapterPosition()).isFollowing()) {
                                Utilities.FollowDoctor followDoctor = new Utilities.FollowDoctor(true, follows.get(holder.getAdapterPosition()).getUserID());
                                followDoctor.execute();
                                ((ItemViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                ((ItemViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.white));
                                ((ItemViewHolder) holder).followButton.setText(R.string.title_unfollow);
                            } else {
                                Utilities.FollowDoctor followDoctor = new Utilities.FollowDoctor(false, follows.get(holder.getAdapterPosition()).getUserID());
                                followDoctor.execute();
                                ((ItemViewHolder) holder).followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                                ((ItemViewHolder) holder).followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                ((ItemViewHolder) holder).followButton.setText(R.string.title_follow);
                            }
                            break;
                        default:
                            Intent intentAnswer = new Intent(context, DoctorProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("name", follows.get(holder.getAdapterPosition()).getName());
                            b.putString("username", follows.get(holder.getAdapterPosition()).getUsername());
                            intentAnswer.putExtras(b);
                            context.startActivity(intentAnswer);
                            break;
                    }
                }
            };

            ((ItemViewHolder) holder).followButton.setOnClickListener(onClickListener);
            ((ItemViewHolder) holder).rootView.setOnClickListener(onClickListener);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return follows.size();
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

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView name, speciality;
        ImageView picture;
        Button followButton;

        ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.doctor_root_view);
            name = (TextView) itemView.findViewById(R.id.doctor_name);
            speciality = (TextView) itemView.findViewById(R.id.doctor_speciality);
            picture = (ImageView) itemView.findViewById(R.id.doctor_picture);
            followButton = (Button) itemView.findViewById(R.id.doctor_follow_button);
        }
    }
}
