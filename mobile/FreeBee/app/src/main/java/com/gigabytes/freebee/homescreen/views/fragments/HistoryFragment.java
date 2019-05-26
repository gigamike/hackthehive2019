package com.gigabytes.freebee.homescreen.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.adapters.ContactsListRecyclerViewAdapter;
import com.gigabytes.freebee.homescreen.views.adapters.HistoryListRecyclerViewAdapter;
import com.gigabytes.freebee.homescreen.views.model.ContactsAPI;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.gigabytes.freebee.login.models.OFW;
import com.gigabytes.freebee.login.models.OFWResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryFragment extends Fragment {
    @InjectView(R.id.callHistoryRV) RecyclerView callHistoryRV;
    @InjectView(R.id.swipeCallHistory) SwipeRefreshLayout swipeCallHistory;

    Retrofit retrofit;

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
        ButterKnife.inject(this, view);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        callHistoryRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        callHistoryRV.setLayoutManager(layoutManager);

        showAllOFW();

        swipeCallHistory.setOnRefreshListener(() -> {
            showAllOFW();
            swipeCallHistory.setRefreshing(false);
        });

        return view;
    }

    private void showAllOFW(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading call history..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
        contactsAPI.getAllOFW().enqueue(new Callback<OFWResponse>() {
            @Override
            public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {
                progressDialog.dismiss();

                List<OFW> ofwList = response.body().getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

                FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getActivity().getApplication();

                for (OFW ofw : ofwList) {

                    if(ofw.getId().equals(freeBeeApplication.userId)) {
                        continue;
                    }

                    ContactsDO contactsDO = new ContactsDO(
                            ofw.getId(),
                            ofw.getFirstName(),
                            ofw.getMiddleName(),
                            ofw.getLastName(),
                            ofw.getOrganization(),
                            ofw.getProfilePic(),
                            ofw.getCountry(),
                            ofw.getCity(),
                            ofw.isOnline(),
                            ofw.getDistance(),
                            ofw.getMobileNumber()
                    );

                    contactsDOList.add(contactsDO);
                    Log.d("debug","contact " + contactsDO);
                }

                callHistoryRV.setAdapter(new HistoryListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }
}
