package com.gigabytes.freebee.homescreen.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigabytes.freebee.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HistoryListRecyclerViewAdapter extends RecyclerView.Adapter<HistoryListRecyclerViewAdapter.HistoryListViewHolder> {

    List<Integer> integerList;

    public static class HistoryListViewHolder extends RecyclerView.ViewHolder {
        public HistoryListViewHolder(View view) {
            super(view);
            ButterKnife.inject(this,view);
        }
    }

    public HistoryListRecyclerViewAdapter() {
      integerList = new ArrayList<>();
      for (int i = 0; i < 10; ++i) {
          integerList.add(0);
      }
    }

    @Override
    public HistoryListRecyclerViewAdapter.HistoryListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                               int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_layout_list_item, parent, false);
        HistoryListViewHolder vh = new HistoryListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder historyListViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return integerList.size();
    }
}

