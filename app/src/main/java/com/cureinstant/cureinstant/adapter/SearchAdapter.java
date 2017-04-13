package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.model.SearchProfile;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 06-04-2017.
 */

// Adapter to show list of search results (only doctors for now)
public class SearchAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private Context context;
    private ArrayList<SearchProfile> searchProfiles;

    public SearchAdapter(Context context, ArrayList<SearchProfile> searchProfiles, RecyclerView recyclerView) {
        this.context = context;
        this.searchProfiles = searchProfiles;

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
        return searchProfiles.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_search_profile, parent, false);
            return new SearchAdapter.ItemViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_progressbar, parent, false);
            return new SearchAdapter.ProgressViewHolder(itemView);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_profile, parent,
                        false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {

            ((ItemViewHolder) holder).doctorName.setText(searchProfiles.get(position).getName());
            ((ItemViewHolder) holder).doctorSpeciality.setText(searchProfiles.get(position).getSpeciality());
            String imageURL = Utilities.profilePicSmallBaseUrl + searchProfiles.get(position).getPicture();
            Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(((ItemViewHolder) holder).doctorPicture);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.search_root_view:
                            Intent intent = new Intent(context, DoctorProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("name", searchProfiles.get(holder.getAdapterPosition()).getName());
                            b.putString("username", searchProfiles.get(holder.getAdapterPosition()).getUsername());
                            intent.putExtras(b);
                            context.startActivity(intent);
                            break;
                    }
                }
            };

            ((ItemViewHolder) holder).rootView.setOnClickListener(onClickListener);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return searchProfiles.size();
    }

    public void setLoaded() {
        this.notifyDataSetChanged();
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

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        TextView doctorName, doctorSpeciality;
        ImageView doctorPicture;

        ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.search_root_view);
            doctorName = (TextView) itemView.findViewById(R.id.search_doctor_name);
            doctorSpeciality = (TextView) itemView.findViewById(R.id.search_doctor_speciality);
            doctorPicture = (ImageView) itemView.findViewById(R.id.search_doctor_picture);
        }
    }
}
