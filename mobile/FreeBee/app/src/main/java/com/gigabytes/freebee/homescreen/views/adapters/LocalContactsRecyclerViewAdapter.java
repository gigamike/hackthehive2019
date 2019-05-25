package com.gigabytes.freebee.homescreen.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.model.LocalContactsModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LocalContactsRecyclerViewAdapter extends RecyclerView.Adapter<LocalContactsRecyclerViewAdapter.LocalContactsRecyclerViewHolder> {

    private List<LocalContactsModel> localContactsModelList;
    private Context context;

    public static class LocalContactsRecyclerViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.contactNameTextView) TextView contactNameTextView;
        @InjectView(R.id.contactNumberTextView) TextView contactNumberTextView;
        @InjectView(R.id.contactItemCardView) CardView contactItemCardView;

        public LocalContactsRecyclerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this,view);
        }
    }

    public LocalContactsRecyclerViewAdapter(List<LocalContactsModel> localContactsModelList, Context context) {
        this.localContactsModelList = localContactsModelList;
        this.context = context;
    }

    @Override
    public LocalContactsRecyclerViewAdapter.LocalContactsRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_layout_list_item, parent, false);
        LocalContactsRecyclerViewHolder vh = new LocalContactsRecyclerViewHolder(v);
        return vh;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(LocalContactsRecyclerViewHolder viewHolder, int position) {
       LocalContactsModel localContactsModel = localContactsModelList.get(position);
       viewHolder.contactNameTextView.setText(localContactsModel.getName());
       viewHolder.contactNumberTextView.setText(localContactsModel.getNumber());

       viewHolder.contactItemCardView.setOnClickListener(v -> {
           Intent callIntent = new Intent(Intent.ACTION_CALL);
           callIntent.setData(Uri.parse("tel:" + localContactsModel.getNumber()));
           context.startActivity(callIntent);
       });
    }

    @Override
    public int getItemCount() {
        return localContactsModelList.size();
    }
}
