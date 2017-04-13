package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorEduDetail;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Education of a doctor
public class DoctorEduAdapter extends RecyclerView.Adapter<DoctorEduAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorEduDetail> doctorEduDetail;

    public DoctorEduAdapter(Context context, ArrayList<DoctorEduDetail> doctorEduDetail) {
        this.context = context;
        this.doctorEduDetail = doctorEduDetail;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_edu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.eduInstitute.setText(doctorEduDetail.get(position).getInstituteName());
        viewHolder.eduCourse.setText(doctorEduDetail.get(position).getCourseName());
        viewHolder.eduTime.setText(doctorEduDetail.get(position).getEduTime());
    }

    @Override
    public int getItemCount() {
        return doctorEduDetail.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView eduInstitute;
        private TextView eduCourse;
        private TextView eduTime;

        MyViewHolder(View itemView) {
            super(itemView);
            eduInstitute = (TextView) itemView.findViewById(R.id.doctor_edu_institute);
            eduCourse = (TextView) itemView.findViewById(R.id.doctor_edu_course);
            eduTime = (TextView) itemView.findViewById(R.id.doctor_edu_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, doctorEduDetail.get(getPosition()).getCourseName(), Toast.LENGTH_SHORT).show();
        }
    }
}
