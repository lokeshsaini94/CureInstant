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
import com.cureinstant.cureinstant.adapter.RepliesAdapter;
import com.cureinstant.cureinstant.model.Comment;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 25-03-2017.
 */

public class RepliesBottomSheetFragment extends BottomSheetDialogFragment {
    public static RepliesBottomSheetFragment getInstance() {
        return new RepliesBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_comment_replies, container, false);

        ArrayList<Comment> replies = getArguments().getParcelableArrayList("replies");

        RecyclerView repliesList = (RecyclerView) rootView.findViewById(R.id.replies_list);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        repliesList.setLayoutManager(mLayoutManager);
        repliesList.setAdapter(new RepliesAdapter(getContext(), replies));
        repliesList.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
