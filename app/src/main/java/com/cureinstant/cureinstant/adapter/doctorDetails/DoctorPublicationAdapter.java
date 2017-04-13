package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorPublication;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Publications of a doctor
public class DoctorPublicationAdapter extends RecyclerView.Adapter<DoctorPublicationAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorPublication> doctorPublication;

    public DoctorPublicationAdapter(Context context, ArrayList<DoctorPublication> doctorPublication) {
        this.context = context;
        this.doctorPublication = doctorPublication;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_publication, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.title.setText(doctorPublication.get(position).getTitle());
        viewHolder.content.setText(doctorPublication.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return doctorPublication.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView content;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.doctor_publication_item_title);
            content = (TextView) itemView.findViewById(R.id.doctor_publication_item_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, doctorPublication.get(getPosition()).getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
