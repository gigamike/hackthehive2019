package com.gigabytes.freebee.homescreen.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryListRecyclerViewAdapter extends RecyclerView.Adapter<HistoryListRecyclerViewAdapter.HistoryListViewHolder> {

    private List<ContactsDO> volunteerList;
    private Context context;

    public HistoryListRecyclerViewAdapter(List<ContactsDO> volunteerList, Context context) {
        this.volunteerList = volunteerList;
        this.context = context;
    }

    public static class HistoryListViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imgCalledProfilePic) CircleImageView imgCalledProfilePic;
        @InjectView(R.id.lblVolunteerName) TextView lblVolunteerName;
        @InjectView(R.id.lblCalleeLocation) TextView lblCalleeLocation;
        @InjectView(R.id.lblTimeCalled) TextView lblTimeCalled;

        public HistoryListViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public HistoryListRecyclerViewAdapter.HistoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_history_list, parent, false);
        return new HistoryListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryListViewHolder viewHolder, int position) {
        final ContactsDO contactsDO = volunteerList.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(contactsDO.getFirstname());
        sb.append(" ");
        sb.append(contactsDO.getLastname());

        viewHolder.lblVolunteerName.setText(sb.toString());
        viewHolder.lblCalleeLocation.setText(contactsDO.getCity() + ", " + contactsDO.getCountry());
        viewHolder.lblTimeCalled.setText("10 mins ago");

        Picasso.with(context).load(contactsDO.getProfilePic()).placeholder(R.drawable.no_image).into(viewHolder.imgCalledProfilePic);
    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }
}

