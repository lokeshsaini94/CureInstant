package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorFeedback;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Feedback of a doctor
public class DoctorFeedbackAdapter extends RecyclerView.Adapter<DoctorFeedbackAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorFeedback> doctorFeedback;

    public DoctorFeedbackAdapter(Context context, ArrayList<DoctorFeedback> doctorFeedback) {
        this.context = context;
        this.doctorFeedback = doctorFeedback;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_feedback, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.feedback.setText(doctorFeedback.get(position).getFeedback());
    }

    @Override
    public int getItemCount() {
        return doctorFeedback.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView feedback;

        MyViewHolder(View itemView) {
            super(itemView);
            feedback = (TextView) itemView.findViewById(R.id.doctor_feedback_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, doctorFeedback.get(getPosition()).getFeedback(), Toast.LENGTH_SHORT).show();
        }
    }
}
