package com.gigabytes.freebee.homescreen.views.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.adapters.ContactsListRecyclerViewAdapter;
import com.gigabytes.freebee.homescreen.views.model.ContactsAPI;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.gigabytes.freebee.login.models.OFW;
import com.gigabytes.freebee.login.models.OFWResponse;
import com.gigabytes.freebee.registration.models.Countries;
import com.gigabytes.freebee.registration.models.CountriesResponse;
import com.gigabytes.freebee.registration.models.Organizations;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
import com.gigabytes.freebee.registration.views.OFWActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoOfwFragment extends Fragment {

    @InjectView(R.id.coOfwRecyclerView) RecyclerView coOfwRecyclerView;
    @InjectView(R.id.swipeCoOFW) SwipeRefreshLayout swipeCoOFW;

    @InjectView(R.id.frmSearch_cmbCountry) MaterialBetterSpinner frmSearch_cmbCountry;
    @InjectView(R.id.frmSearch_txtCity) EditText frmSearch_txtCity;
    @InjectView(R.id.btnSearch) AppCompatButton btnSearch;

    Retrofit retrofit;

    ArrayList<Countries> countriesList;

    List<ContactsDO> contactsDOList;

    String city,countryCode;

    public static CoOfwFragment createInstance() {
        return new CoOfwFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_co_ofw, container, false);
        ButterKnife.inject(this, view);

        countriesList = new ArrayList<>();

        contactsDOList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        coOfwRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        coOfwRecyclerView.setLayoutManager(layoutManager);

        btnSearch.setOnClickListener(v -> {
            city = frmSearch_txtCity.getText().toString().trim();

            Log.d("city", city);

            searchOFW();
        });

        loadCountries();
        showAllOFW();

        swipeCoOFW.setOnRefreshListener(() -> {
            showAllOFW();
            swipeCoOFW.setRefreshing(false);
        });

        frmSearch_cmbCountry.setOnItemClickListener((parent, view1, position, id) -> {
            countryCode = countriesList.get(position).getCountryCode();
            Log.d("debug", "Selected: " + countryCode);
        });

        return view;
    }

    private void loadCountries(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading countries...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);
        registrationAPI.getAllCountries().enqueue(new Callback<List<CountriesResponse>>() {
            @Override
            public void onResponse(Call<List<CountriesResponse>> call, Response<List<CountriesResponse>> response) {
                progressDialog.dismiss();
                List<CountriesResponse> responseCountries = response.body();

                for(CountriesResponse country : Objects.requireNonNull(responseCountries)){
                    Countries countries = new Countries(country.getId(), country.getCountryCode(), country.getCountryName());
                    countriesList.add(countries);
                }

                frmSearch_cmbCountry.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countriesList));
            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void searchOFW(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Filtering OFWs..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);

        Log.d("debug", "countryCode: " + countryCode);
        Log.d("debug", "city: " + city);

        contactsAPI.getSearchedOFW(countryCode, city).enqueue(new Callback<OFWResponse>() {
            @Override
            public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {
                progressDialog.dismiss();

                List<OFW> ofwList = Objects.requireNonNull(response.body()).getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

                for (OFW ofw : ofwList) {

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
                            ofw.getMobileNumber());

                    contactsDOList.add(contactsDO);
                    Log.d("debug","contact " + contactsDO);
                }

                coOfwRecyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getCause().getMessage());
            }
        });
    }

    private void showAllOFW(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Showing all OFWs..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
        contactsAPI.getAllOFW().enqueue(new Callback<OFWResponse>() {
            @Override
            public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {
                progressDialog.dismiss();

                List<OFW> ofwList = response.body().getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

                for (OFW ofw : ofwList) {

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

                coOfwRecyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }
}
