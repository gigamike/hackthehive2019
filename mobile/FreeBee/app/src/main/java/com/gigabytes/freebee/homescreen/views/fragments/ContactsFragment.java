package com.gigabytes.freebee.homescreen.views.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.credits.views.activities.CreditsActivity;
import com.gigabytes.freebee.homescreen.views.activities.ProfileActivity;
import com.gigabytes.freebee.homescreen.views.adapters.ContactsListRecyclerViewAdapter;
import com.gigabytes.freebee.homescreen.views.model.ContactsAPI;
import com.gigabytes.freebee.homescreen.views.model.Volunteer;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.gigabytes.freebee.homescreen.views.model.VolunteerResponse;
import com.gigabytes.freebee.login.models.OFW;
import com.gigabytes.freebee.login.models.OFWResponse;
import com.gigabytes.freebee.registration.models.Countries;
import com.gigabytes.freebee.registration.models.CountriesResponse;
import com.gigabytes.freebee.registration.models.Organizations;
import com.gigabytes.freebee.registration.models.OrganizationsResponse;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsFragment extends Fragment {

    @InjectView(R.id.my_recycler_view) RecyclerView recyclerView;
    @InjectView(R.id.swipeContacts) SwipeRefreshLayout swipeContacts;

    @InjectView(R.id.frmSearch_cmbOrganization) MaterialBetterSpinner frmSearch_cmbOrganization;
    @InjectView(R.id.frmSearch_cmbCountry) MaterialBetterSpinner frmSearch_cmbCountry;
    @InjectView(R.id.frmSearch_txtCity) EditText frmSearch_txtCity;
    @InjectView(R.id.btnSearch) AppCompatButton btnSearch;

    Retrofit retrofit;

    ArrayList<Organizations> organizationsList;
    ArrayList<Countries> countriesList;

    List<ContactsDO> contactsDOList;

    String city, countryCode;
    Integer organizationID;

    public static ContactsFragment createInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeEvents(View view) {
        Button buttonShowCredits = view.findViewById(R.id.buttonShowCredits);

        buttonShowCredits.setOnClickListener(v -> {
            Intent showCreditsIntentActivity = new Intent(getActivity(), CreditsActivity.class);
            startActivity(showCreditsIntentActivity);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) Objects.requireNonNull(getActivity()).getApplication();
        organizationsList = new ArrayList<>();
        countriesList = new ArrayList<>();

        contactsDOList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        View view = inflater.inflate(R.layout.contacts_fragment, container, false);
        ButterKnife.inject(this, view);

        initializeEvents(view);

        recyclerView.setHasFixedSize(true);

        CircleImageView profileImage = view.findViewById(R.id.profile_image);
        Picasso.with(getContext()).load(Objects.requireNonNull(freeBeeApplication).userPictureURL).placeholder(R.drawable.no_image).into(profileImage);

        profileImage.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfileActivity.class)));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        btnSearch.setOnClickListener(v -> {
            city = frmSearch_txtCity.getText().toString().trim() == null ? "" : frmSearch_txtCity.getText().toString().trim();

            switch (freeBeeApplication.userRole){
                case "ofw":
                    searchVolunteer();
                    break;
                case "volunteer":
                    searchOFW();
                    break;
            }
        });

        loadOrganizations();
        loadCountries();

        detectRoleToShowList(freeBeeApplication, progressDialog);

        swipeContacts.setOnRefreshListener(() -> {
            detectRoleToShowList(freeBeeApplication, progressDialog);
            swipeContacts.setRefreshing(false);
        });

        return view;
    }

    private void loadOrganizations(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading organizations...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);
        registrationAPI.getAllOrganizations().enqueue(new Callback<List<OrganizationsResponse>>() {
            @Override
            public void onResponse(Call<List<OrganizationsResponse>> call, Response<List<OrganizationsResponse>> response) {
                progressDialog.dismiss();

                List<OrganizationsResponse> organizationsResponses = response.body();

                for(OrganizationsResponse organization : Objects.requireNonNull(organizationsResponses)){
                    Organizations organizations = new Organizations(Integer.parseInt(organization.getId()), organization.getOrganization());
                    organizationsList.add(organizations);
                }

                frmSearch_cmbOrganization.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, organizationsList));
                frmSearch_cmbOrganization.setOnItemClickListener((parent, view, position, id) -> {
                    organizationID = organizationsList.get(position).getOrgId();
                    Log.d("debug", "Selected: " + organizationID);
                });
            }

            @Override
            public void onFailure(Call<List<OrganizationsResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
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
                frmSearch_cmbCountry.setOnItemClickListener((parent, view, position, id) -> {
                    countryCode = countriesList.get(position).getCountryCode();
                    Log.d("debug", "Selected: " + countryCode);
                });
            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void searchVolunteer(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Filtering volunteers..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) getActivity().getApplication();

        Log.d("debug", "organizationID: " + organizationID);
        Log.d("debug", "countryCode: " + countryCode);
        Log.d("debug", "city: " + city);

        contactsAPI.getSearchedVolunteer(organizationID, countryCode, city).enqueue(new Callback<VolunteerResponse>() {
            @Override
            public void onResponse(Call<VolunteerResponse> call, Response<VolunteerResponse> response) {
                progressDialog.dismiss();
                List<Volunteer> volunteerList = Objects.requireNonNull(response.body()).getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

                for (Volunteer volunteer : volunteerList) {

                    if(volunteer.getId().equals(freeBeeApplication.userId)) {
                        continue;
                    }

                    ContactsDO contactsDO = new ContactsDO(
                            volunteer.getId(),
                            volunteer.getFirstname(),
                            volunteer.getMiddlename(),
                            volunteer.getLastname(),
                            volunteer.getOrganization(),
                            volunteer.getProfilePic(),
                            volunteer.getCountry(),
                            volunteer.getCity(),
                            volunteer.isOnline(),
                            volunteer.getDistance(),
                            volunteer.getMobileNumber()
                    );

                    contactsDOList.add(contactsDO);
                    Log.d("debug","contact " + contactsDO);
                }

                recyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<VolunteerResponse> call, Throwable t) {
                progressDialog.dismiss();
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
        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) getActivity().getApplication();

        contactsAPI.getSearchedOFW(countryCode, city).enqueue(new Callback<OFWResponse>() {
            @Override
            public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {
                progressDialog.dismiss();

                List<OFW> ofwList = Objects.requireNonNull(response.body()).getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

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
                            ofw.getMobileNumber());

                    contactsDOList.add(contactsDO);
                    Log.d("debug","contact " + contactsDO);
                }

                recyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void loadAllOfw(ProgressDialog progressDialog, ContactsAPI contactsAPI){
        progressDialog.setMessage("Loading ofws...");
        progressDialog.show();

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

                recyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadAllVolunteer(ProgressDialog progressDialog, ContactsAPI contactsAPI){
        progressDialog.setMessage("Loading volunteers...");
        progressDialog.show();

        contactsAPI.getAllVolunteer().enqueue(new Callback<VolunteerResponse>() {
            @Override
            public void onResponse(Call<VolunteerResponse> call, Response<VolunteerResponse> response) {
                progressDialog.dismiss();

                List<Volunteer> volunteerList = Objects.requireNonNull(response.body()).getResults();

                for (Volunteer volunteer : volunteerList) {

                    ContactsDO contactsDO = new ContactsDO(
                            volunteer.getId(),
                            volunteer.getFirstname(),
                            volunteer.getMiddlename(),
                            volunteer.getLastname(),
                            volunteer.getOrganization(),
                            volunteer.getProfilePic(),
                            volunteer.getCountry(),
                            volunteer.getCity(),
                            volunteer.isOnline(),
                            volunteer.getDistance(),
                            volunteer.getMobileNumber()
                    );

                    contactsDOList.add(contactsDO);
                }

                recyclerView.setAdapter(new ContactsListRecyclerViewAdapter(contactsDOList, getContext()));

            }

            @Override
            public void onFailure(Call<VolunteerResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void detectRoleToShowList(FreeBeeApplication freeBeeApplication, ProgressDialog progressDialog){
        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
        if (freeBeeApplication.userRole.equals("volunteer")) {
            loadAllOfw(progressDialog, contactsAPI);
        } else if (freeBeeApplication.userRole.equals("ofw")) {
            loadAllVolunteer(progressDialog, contactsAPI);
        }
    }
}
