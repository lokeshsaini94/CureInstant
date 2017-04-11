package com.cureinstant.cureinstant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.DoctorsSearchAdapter;
import com.cureinstant.cureinstant.misc.SimpleDividerItemDecoration;
import com.cureinstant.cureinstant.model.BookDoctor;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 11-04-2017.
 */

public class DoctorsBottomSheetFragment extends BottomSheetDialogFragment {
    public static DoctorsBottomSheetFragment getInstance() {
        return new DoctorsBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_doctors_search, container, false);

        ArrayList<BookDoctor> bookDoctors = getArguments().getParcelableArrayList("doctors");

        RecyclerView doctorsList = (RecyclerView) rootView.findViewById(R.id.doctors_list);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        doctorsList.setLayoutManager(mLayoutManager);
        doctorsList.setAdapter(new DoctorsSearchAdapter(getContext(), bookDoctors));
        doctorsList.setItemAnimator(new DefaultItemAnimator());
        doctorsList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        return rootView;
    }
}
