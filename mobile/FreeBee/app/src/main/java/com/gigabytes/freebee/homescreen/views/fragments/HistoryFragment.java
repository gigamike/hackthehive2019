package com.gigabytes.freebee.homescreen.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.adapters.HistoryListRecyclerViewAdapter;

public class HistoryFragment extends Fragment {
    public static HistoryFragment createInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_fragment, container, false);
        RecyclerView historyRecyclerview = view.findViewById(R.id.historyRecyclerview);
        historyRecyclerview.setAdapter(new HistoryListRecyclerViewAdapter());
        return view;
    }
}
