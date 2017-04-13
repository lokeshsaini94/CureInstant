package com.cureinstant.cureinstant.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 28-03-2017.
 */

// Adapter to show list of Youtube videos in a given feed Item
public class FeedYoutubeAdapter extends RecyclerView.Adapter<FeedYoutubeAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<String> youtubeVideos;

    public FeedYoutubeAdapter(Context context, ArrayList<String> youtubeVideos) {
        this.context = context;
        this.youtubeVideos = youtubeVideos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_youtube, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        String imageURL = "http://img.youtube.com/vi/";
        imageURL += youtubeVideos.get(position);
        imageURL += "/0.jpg";
        Glide.with(context).load(imageURL).thumbnail(0.1f).placeholder(R.drawable.doctor_placeholder).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeVideos.get(holder.getAdapterPosition())));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + youtubeVideos.get(holder.getAdapterPosition())));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.youtube_image);
        }
    }
}
