package com.gigabytes.freebee.homescreen.views.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    @InjectView(R.id.frmSearch_cmbOrganization) MaterialBetterSpinner frmSearch_cmbOrganization;
    @InjectView(R.id.frmSearch_txtFirstname) EditText frmSearch_txtFirstname;
    @InjectView(R.id.frmSearch_txtLastname) EditText frmSearch_txtLastname;
    @InjectView(R.id.btnSearch) AppCompatButton btnSearch;

    Retrofit retrofit;

    ArrayList<Organizations> organizationsList;
    List<ContactsDO> contactsDOList;

    String firstname,lastname;
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
            firstname = frmSearch_txtFirstname.getText().toString().trim();
            lastname = frmSearch_txtLastname.getText().toString().trim();

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
        if (freeBeeApplication.userRole.equals("volunteer")) {

            progressDialog.setMessage("Loading ofws...");
            progressDialog.show();

            contactsAPI.getAllOFW().enqueue(new Callback<OFWResponse>() {
                @Override
                public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {
                    progressDialog.dismiss();

                    List<OFW> ofwList = response.body().getResults();
                    List<ContactsDO> contactsDOList = new ArrayList<>();

                    for (OFW ofw : ofwList) {

                        ContactsDO contactsDO = new ContactsDO(ofw.getId(),
                                                               ofw.getFirstName(),
                                                               ofw.getMiddleName(),
                                                               ofw.getLastName(),
                                                               ofw.getOrganization(),
                                                               ofw.getProfilePic(),
                                                               ofw.getCountry(),
                                                               ofw.getCity(),
                                                               ofw.isOnline(),
                                                               ofw.getDistance());

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

        } else if (freeBeeApplication.userRole.equals("ofw")) {
            progressDialog.setMessage("Loading volunteers...");
            progressDialog.show();

            contactsAPI.getAllVolunteer().enqueue(new Callback<VolunteerResponse>() {
                @Override
                public void onResponse(Call<VolunteerResponse> call, Response<VolunteerResponse> response) {
                    progressDialog.dismiss();

                    List<Volunteer> volunteerList = Objects.requireNonNull(response.body()).getResults();

                    Volunteer volunteerBean = new Volunteer();

                    for (Volunteer volunteer : volunteerList) {

                        volunteerBean.setId(volunteer.getId());
                        volunteerBean.setFirstname(volunteer.getFirstname());
                        volunteerBean.setMiddlename(volunteer.getMiddlename());
                        volunteerBean.setLastname(volunteer.getLastname());
                        volunteerBean.setOrganization(volunteer.getOrganization());
                        volunteerBean.setProfilePic(volunteer.getProfilePic());
                        volunteerBean.setCountry(volunteer.getCountry());
                        volunteerBean.setCity(volunteer.getCity());
                        volunteerBean.setDistance(volunteer.getDistance());
                        volunteerBean.setOnline(volunteer.isOnline());

                        ContactsDO contactsDO = new ContactsDO(
                                volunteerBean.getId(),
                                volunteerBean.getFirstname(),
                                volunteerBean.getMiddlename(),
                                volunteerBean.getLastname(),
                                volunteerBean.getOrganization(),
                                volunteerBean.getProfilePic(),
                                volunteerBean.getCountry(),
                                volunteerBean.getCity(),
                                volunteerBean.isOnline(),
                                volunteerBean.getDistance()
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
                frmSearch_cmbOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        organizationID = organizationsList.get(position).getOrgId();
                        Log.e("debug", "Selected: " + organizationID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<OrganizationsResponse>> call, Throwable t) {
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
        contactsAPI.getSearchedVolunteer(organizationID,firstname,lastname).enqueue(new Callback<VolunteerResponse>() {
            @Override
            public void onResponse(Call<VolunteerResponse> call, Response<VolunteerResponse> response) {
                progressDialog.dismiss();
                List<Volunteer> volunteerList = Objects.requireNonNull(response.body()).getResults();
                List<ContactsDO> contactsDOList = new ArrayList<>();

                Volunteer volunteerBean = new Volunteer();

                for (Volunteer volunteer : volunteerList) {

                    volunteerBean.setId(volunteer.getId());
                    volunteerBean.setFirstname(volunteer.getFirstname());
                    volunteerBean.setMiddlename(volunteer.getMiddlename());
                    volunteerBean.setLastname(volunteer.getLastname());
                    volunteerBean.setOrganization(volunteer.getOrganization());
                    volunteerBean.setProfilePic(volunteer.getProfilePic());
                    volunteerBean.setCountry(volunteer.getCountry());
                    volunteerBean.setCity(volunteer.getCity());
                    volunteerBean.setDistance(volunteer.getDistance());
                    volunteerBean.setOnline(volunteer.isOnline());

                    ContactsDO contactsDO = new ContactsDO(
                            volunteerBean.getId(),
                            volunteerBean.getFirstname(),
                            volunteerBean.getMiddlename(),
                            volunteerBean.getLastname(),
                            volunteerBean.getOrganization(),
                            volunteerBean.getProfilePic(),
                            volunteerBean.getCountry(),
                            volunteerBean.getCity(),
                            volunteerBean.isOnline(),
                            volunteerBean.getDistance()
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
        contactsAPI.getSearchedOFW(firstname,lastname).enqueue(new Callback<OFWResponse>() {
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
                            ofw.getDistance());

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
}
