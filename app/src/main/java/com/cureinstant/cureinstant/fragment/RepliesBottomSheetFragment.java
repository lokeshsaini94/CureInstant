package com.cureinstant.cureinstant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cureinstant.cureinstant.R;

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

        return rootView;
    }
}
