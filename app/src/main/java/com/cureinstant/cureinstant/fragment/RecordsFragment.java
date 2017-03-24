package com.cureinstant.cureinstant.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.AddRecordActivity;
import com.cureinstant.cureinstant.adapter.RecordAdapter;
import com.cureinstant.cureinstant.model.Record;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {

    private static final int ADD_RECORD = 99;
    private List<Record> recordList = new ArrayList<>();
    private RecordAdapter recordAdapter;


    public RecordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_records, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.records_recycler_view);

        recordAdapter = new RecordAdapter(recordList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recordAdapter);

        if (recordList.isEmpty()) {
            prepareRecords();
            prepareRecords();
            prepareRecords();
        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.records_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRecordActivity.class);
                startActivityForResult(intent, ADD_RECORD);
            }
        });

        return rootView;
    }

    private void prepareRecords() {
        Record record = new Record(null, "ABC Health Record", R.drawable.ic_appointments);
        recordList.add(0, record);

        record = new Record(null, "MNO Health Record", R.drawable.ic_appointments);
        recordList.add(0, record);

        record = new Record(null, "XYZ Health Record", R.drawable.ic_appointments);
        recordList.add(0, record);

        recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RECORD) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                int icon = data.getIntExtra("icon", R.drawable.ic_appointments);
                String filePath = data.getStringExtra("filePath");
                Log.e("TAG", filePath);
                Record record = new Record(filePath, title, icon);
                recordList.add(0, record);
                recordAdapter.notifyDataSetChanged();
            }
        }
    }
}
