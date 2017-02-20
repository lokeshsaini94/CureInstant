package com.cureinstant.cureinstant.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.ViewPagerAdapter;
import com.cureinstant.cureinstant.fragments.read.FeedFragment;
import com.cureinstant.cureinstant.fragments.read.TrendingFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ReadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_read, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.custom_tab_icon).setAlpha(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.custom_tab_icon).setAlpha((float) 0.5);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.read_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FeedFragment(), getString(R.string.feed));
        adapter.addFragment(new TrendingFragment(), getString(R.string.trending));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        View customTab1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_tab, null);
        TextView tabOneText = (TextView) customTab1.findViewById(R.id.custom_tab_text);
        ImageView tabOneIcon = (ImageView) customTab1.findViewById(R.id.custom_tab_icon);
        tabOneText.setText(getString(R.string.feed));
        tabOneIcon.setImageResource(R.drawable.ic_read);
        tabLayout.getTabAt(0).setCustomView(customTab1);

        View customTab2 = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_tab, null);
        TextView tabTwoText = (TextView) customTab2.findViewById(R.id.custom_tab_text);
        ImageView tabTwoIcon = (ImageView) customTab2.findViewById(R.id.custom_tab_icon);
        tabTwoIcon.setAlpha((float) 0.5);
        tabTwoText.setText(getString(R.string.trending));
        tabTwoIcon.setImageResource(R.drawable.ic_trending);
        tabLayout.getTabAt(1).setCustomView(customTab2);
    }
}
