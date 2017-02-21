package com.cureinstant.cureinstant.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.NotificationAdapter;
import com.cureinstant.cureinstant.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends DialogFragment implements View.OnClickListener {

    private List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.action_close_notif_frag);
        imageButton.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);

        notificationAdapter = new NotificationAdapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notificationAdapter);

        prepareMovieData();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                notificationAdapter.remove(position);
                notificationAdapter.notifyItemRemoved(position);
                notificationAdapter.notifyItemRangeChanged(position, notificationAdapter.getItemCount());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Notification notification = new Notification("New Message", "Action & Adventure");
                        notificationList.add(0, notification);
                        notificationAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NotifDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    private void prepareMovieData() {
        Notification notification = new Notification("Mad Max: Fury Road", "Action & Adventure");
        notificationList.add(notification);

        notification = new Notification("Inside Out", "Animation, Kids & Family");
        notificationList.add(notification);

        notification = new Notification("Star Wars: Episode VII - The Force Awakens", "Action");
        notificationList.add(notification);

        notification = new Notification("Shaun the Sheep", "Animation");
        notificationList.add(notification);

        notification = new Notification("The Martian", "Science Fiction & Fantasy");
        notificationList.add(notification);

        notification = new Notification("Mission: Impossible Rogue Nation", "Action");
        notificationList.add(notification);

        notification = new Notification("Up", "Animation");
        notificationList.add(notification);

        notification = new Notification("Star Trek", "Science Fiction");
        notificationList.add(notification);

        notification = new Notification("The LEGO Movie", "Animation");
        notificationList.add(notification);

        notification = new Notification("Iron Man", "Action & Adventure");
        notificationList.add(notification);

        notification = new Notification("Aliens", "Science Fiction");
        notificationList.add(notification);

        notification = new Notification("Chicken Run", "Animation");
        notificationList.add(notification);

        notification = new Notification("Back to the Future", "Science Fiction");
        notificationList.add(notification);

        notification = new Notification("Raiders of the Lost Ark", "Action & Adventure");
        notificationList.add(notification);

        notification = new Notification("Goldfinger", "Action & Adventure");
        notificationList.add(notification);

        notification = new Notification("Guardians of the Galaxy", "Science Fiction & Fantasy");
        notificationList.add(notification);

        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_close_notif_frag:
                getDialog().dismiss();
                break;
        }
    }
}
