package com.cureinstant.cureinstant.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.FollowAdapter;
import com.cureinstant.cureinstant.misc.SimpleDividerItemDecoration;
import com.cureinstant.cureinstant.model.Follow;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {

    RecyclerView followList;
    FollowAdapter followAdapter;
    private String type;
    private ArrayList<Follow> follows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getString("type");
            follows = b.getParcelableArrayList("follows");
        }

        if (type == null) {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(type);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        followList = (RecyclerView) findViewById(R.id.follow_recycler_view);
        followList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        followAdapter = new FollowAdapter(getApplicationContext(), type, follows);
        followList.setAdapter(followAdapter);
        followList.setItemAnimator(new DefaultItemAnimator());
        followList.addItemDecoration(new SimpleDividerItemDecoration(this));
    }
}
