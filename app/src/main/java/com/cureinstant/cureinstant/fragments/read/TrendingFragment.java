package com.cureinstant.cureinstant.fragments.read;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cureinstant.cureinstant.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        ArrayList<String> data = new ArrayList<>();
        data.add("Apple");
        data.add("Peach");
        data.add("Pineapple");
        data.add("Banana");
        data.add("Orange");
        data.add("Watermelon");
        data.add("Apple");
        data.add("Peach");
        data.add("Pineapple");
        data.add("Banana");
        data.add("Orange");
        data.add("Watermelon");
        data.add("Apple");
        data.add("Peach");
        data.add("Pineapple");
        data.add("Banana");
        data.add("Orange");
        data.add("Watermelon");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        return rootView;
    }

}
