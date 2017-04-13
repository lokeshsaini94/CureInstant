package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkDetail;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Work Details of a doctor
public class DoctorWorkDetailsAdapter extends RecyclerView.Adapter<DoctorWorkDetailsAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorWorkDetail> doctorWorkDetails;

    public DoctorWorkDetailsAdapter(Context context, ArrayList<DoctorWorkDetail> doctorWorkDetails) {
        this.context = context;
        this.doctorWorkDetails = doctorWorkDetails;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_workplace, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.workPlaceName.setText(doctorWorkDetails.get(position).getDoctorWorkPlace().getName());
        viewHolder.workPlaceAddress.setText(doctorWorkDetails.get(position).getDoctorWorkPlace().getAddress());
    }

    @Override
    public int getItemCount() {
        return doctorWorkDetails.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView workPlaceName;
        private TextView workPlaceAddress;

        MyViewHolder(View itemView) {
            super(itemView);
            workPlaceName = (TextView) itemView.findViewById(R.id.doctor_workplace_name);
            workPlaceAddress = (TextView) itemView.findViewById(R.id.doctor_workplace_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, doctorWorkDetails.get(getPosition()).getDoctorWorkPlace().getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
