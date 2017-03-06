package com.cureinstant.cureinstant.fragment.read;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment implements View.OnClickListener {


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout feedContainer = (LinearLayout) rootView.findViewById(R.id.feed_container);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, marginInDP(8));

        View item1 = layoutInflater.inflate(R.layout.layout_feed_post, null);
        item1.setLayoutParams(params);
        Button follow1 = (Button) item1.findViewById(R.id.post_follow_button);
        follow1.setOnClickListener(this);
        feedContainer.addView(item1);

        View item2 = layoutInflater.inflate(R.layout.layout_feed_post, null);
        TextView postType = (TextView) item2.findViewById(R.id.post_type);
        postType.setText("Question");
        TextView postTitle = (TextView) item2.findViewById(R.id.post_title);
        postTitle.setText("I am 13, What do my symptoms mean?");
        item2.findViewById(R.id.doctor_info_container).setVisibility(View.GONE);
        item2.setLayoutParams(params);
        Button follow2 = (Button) item2.findViewById(R.id.post_follow_button);
        follow2.setOnClickListener(this);
        feedContainer.addView(item2);

        View item3 = layoutInflater.inflate(R.layout.layout_feed_post, null);
        TextView postType3 = (TextView) item3.findViewById(R.id.post_type);
        postType3.setText("Post");
        View title3 = item3.findViewById(R.id.post_title);
        title3.setVisibility(View.GONE);
        item3.setLayoutParams(params);
        Button follow3 = (Button) item3.findViewById(R.id.post_follow_button);
        follow3.setOnClickListener(this);
        feedContainer.addView(item3);

        View item4 = layoutInflater.inflate(R.layout.layout_feed_post, null);
        TextView postType4 = (TextView) item4.findViewById(R.id.post_type);
        postType4.setText("Article");
        item4.setLayoutParams(params);
        Button follow4 = (Button) item4.findViewById(R.id.post_follow_button);
        follow4.setOnClickListener(this);
        feedContainer.addView(item4);

        return rootView;
    }

    private int marginInDP(int dp) {
        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
                        .getDisplayMetrics());
        return marginInDp;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_follow_button:
                Button button = (Button) view;
                View parentView = (View) button.getParent();
                TextView count = (TextView) parentView.findViewById(R.id.post_follow_count);
                boolean visibility = isVisible(count);
                count.setVisibility(visibility ? View.GONE : View.VISIBLE);
                changeButtonColours(visibility, button);
        }
    }

    private boolean isVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return true;
        } else if (view.getVisibility() == View.GONE) {
            return false;
        }
        return false;
    }

    private void changeButtonColours(boolean b, Button button) {
        if (b) {
            button.setTextColor(getResources().getColor(R.color.colorPrimary));
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setText("Follow");
        } else {
            button.setTextColor(getResources().getColor(R.color.primary_text));
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            button.setText("Unfollow");
        }
    }
}
