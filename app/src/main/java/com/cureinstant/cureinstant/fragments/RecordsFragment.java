package com.cureinstant.cureinstant.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.AddRecordActivity;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.RecordAdapter;
import com.cureinstant.cureinstant.model.Record;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {

    private List<Record> recordList = new ArrayList<>();
    private RecordAdapter recordAdapter;
    private static final int ADD_RECORD = 99;
    Record addRecordFAB;


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
        }

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                recordAdapter.remove(position);
                // TODO: 28-02-2017 Remove record file too from the storage
                recordAdapter.notifyItemRemoved(position);
                recordAdapter.notifyItemRangeChanged(position, recordAdapter.getItemCount());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        recordList.add(record);

        record = new Record(null, "MNO Health Record", R.drawable.ic_appointments);
        recordList.add(record);

        record = new Record(null, "XYZ Health Record", R.drawable.ic_appointments);
        recordList.add(record);

        recordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_RECORD) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                int icon = data.getIntExtra("icon", R.drawable.ic_appointments);
                Record record = new Record(null, title, icon);
                recordList.add(record);
                recordAdapter.notifyDataSetChanged();
            }
        }
    }
}
