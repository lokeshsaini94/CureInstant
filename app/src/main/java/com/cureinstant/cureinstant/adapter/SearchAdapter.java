package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;
import com.cureinstant.cureinstant.model.SearchProfile;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 06-04-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SearchProfile> searchProfiles;

    public SearchAdapter(Context context, ArrayList<SearchProfile> searchProfiles) {
        this.context = context;
        this.searchProfiles = searchProfiles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_profile, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

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
    }

    @Override
    public int getItemCount() {
        return searchProfiles.size();
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
