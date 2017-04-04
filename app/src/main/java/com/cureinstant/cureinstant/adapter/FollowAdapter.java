package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.model.Follow;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 04-04-2017.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ItemViewHolder> {

    private Context context;
    private String type;
    private ArrayList<Follow> follows;

    public FollowAdapter(Context context, String type, ArrayList<Follow> follows) {
        this.context = context;
        this.type = type;
        this.follows = follows;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_follow_profile, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.name.setText(follows.get(position).getName());
        holder.speciality.setText(follows.get(position).getSpeciality());
        String imageURL = Utilities.profilePicSmallBaseUrl + follows.get(position).getPicture();
        Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.picture);

        if (type.equals("followers")) {
            holder.followButton.setVisibility(View.VISIBLE);
            if (follows.get(position).isFollowing()) {
                holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.followButton.setTextColor(context.getResources().getColor(R.color.white));
                holder.followButton.setText("Unfollow");
            } else {
                holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.followButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.followButton.setText("Follow");
            }
        } else {
            holder.followButton.setVisibility(View.GONE);
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.doctor_follow_button:
                        break;
                    default:
                        Intent intentAnswer = new Intent(context, DoctorProfileActivity.class);
                        Bundle b = new Bundle();
                        b.putString("name", follows.get(holder.getAdapterPosition()).getName());
                        b.putString("username", follows.get(holder.getAdapterPosition()).getUsername());
                        intentAnswer.putExtras(b);
                        intentAnswer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentAnswer);
                        break;
                }
            }
        };

        holder.followButton.setOnClickListener(onClickListener);
        holder.rootView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return follows.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

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
