package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorSkill;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

public class DoctorSkillsAdapter extends RecyclerView.Adapter<DoctorSkillsAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorSkill> doctorSkills;

    public DoctorSkillsAdapter(Context context, ArrayList<DoctorSkill> doctorSkills) {
        this.context = context;
        this.doctorSkills = doctorSkills;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_specialisation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.skill.setText(doctorSkills.get(position).getSkill());
    }

    @Override
    public int getItemCount() {
        return doctorSkills.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView skill;

        MyViewHolder(View itemView) {
            super(itemView);
            skill = (TextView) itemView.findViewById(R.id.doctor_specialisation_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, doctorSkills.get(getPosition()).getSkill(), Toast.LENGTH_SHORT).show();
        }
    }
}
