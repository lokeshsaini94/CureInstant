package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Feed;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.List;

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
        holder.type.setText(feed.getType());
        holder.title.setText(feed.getTitle());
        holder.content.setText(feed.getContent());
        holder.time.setText(feed.getTime());
        holder.doctorName.setText(feed.getDoctorName());
        holder.doctorSpeciality.setText(feed.getDoctorSpec());
        Glide.with(context).load(Utilities.profilePicSmallBaseUrl + feed.getDoctorPicture()).placeholder(R.drawable.doctor_placeholder).into(holder.doctorPicture);
        // TODO: 15-03-2017 make image fetching work!
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class MyFeedViewHolder extends RecyclerView.ViewHolder {

        TextView type, title, content, time, doctorName, doctorSpeciality;
        ImageView doctorPicture;

        public MyFeedViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.post_type);
            title = (TextView) itemView.findViewById(R.id.post_title);
            content = (TextView) itemView.findViewById(R.id.post_desc);
            time = (TextView) itemView.findViewById(R.id.post_time);
            doctorName = (TextView) itemView.findViewById(R.id.doctor_name);
            doctorSpeciality = (TextView) itemView.findViewById(R.id.doctor_speciality);
            doctorPicture = (ImageView) itemView.findViewById(R.id.doctor_picture);
        }
    }
}
