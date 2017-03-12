package com.cureinstant.cureinstant.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.activity.MyPreferencesActivity;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.ProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {


    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        View profileView = rootView.findViewById(R.id.user_details_container);
        profileView.setOnClickListener(this);
        View settings = rootView.findViewById(R.id.more_settings);
        settings.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_details_container:
                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.more_settings:
                Intent settingsIntent = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(settingsIntent);
                break;
        }

    }
}
