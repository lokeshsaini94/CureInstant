package com.cureinstant.cureinstant.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.DoctorProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {


    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        View docView = rootView.findViewById(R.id.doctor_info_card);
        docView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DoctorProfileActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
