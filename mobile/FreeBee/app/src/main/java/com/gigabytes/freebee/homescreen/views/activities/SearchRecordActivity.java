package com.gigabytes.freebee.homescreen.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.model.ContactsAPI;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.gigabytes.freebee.homescreen.views.model.Volunteer;
import com.gigabytes.freebee.homescreen.views.model.VolunteerResponse;
import com.gigabytes.freebee.login.models.OFW;
import com.gigabytes.freebee.login.models.OFWResponse;
import com.gigabytes.freebee.registration.models.Organizations;
import com.gigabytes.freebee.registration.models.OrganizationsResponse;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
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

public class SearchRecordActivity extends AppCompatActivity {
    @InjectView(R.id.frmSearch_cmbOrganization) MaterialBetterSpinner frmSearch_cmbOrganization;
    @InjectView(R.id.frmSearch_txtFirstname) EditText frmSearch_txtFirstname;
    @InjectView(R.id.frmSearch_txtLastname) EditText frmSearch_txtLastname;
    @InjectView(R.id.btnSearch) AppCompatButton btnSearch;

    Retrofit retrofit;
    ArrayList<Organizations> organizationsList;

    String firstname,lastname;
    Integer organizationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_record);
        ButterKnife.inject(this);

        FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getApplication();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        organizationsList = new ArrayList<>();

        btnSearch.setOnClickListener(v -> {
            firstname = frmSearch_txtFirstname.getText().toString().trim();
            lastname = frmSearch_txtLastname.getText().toString().trim();

            switch (freeBeeApplication.userRole){
                case "ofw":
                    searchOFW();
                    break;
                case "volunteer":
                    searchVolunteer();
                    break;
            }

        });

        loadOrganizations();
    }

    private void loadOrganizations(){
        ProgressDialog progressDialog = new ProgressDialog(SearchRecordActivity.this);
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

                frmSearch_cmbOrganization.setAdapter(new ArrayAdapter<>(SearchRecordActivity.this, android.R.layout.simple_spinner_dropdown_item, organizationsList));
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
        ProgressDialog progressDialog = new ProgressDialog(SearchRecordActivity.this);
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
                    Log.d("debug","contact " + contactsDO);
                }
            }

            @Override
            public void onFailure(Call<VolunteerResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void searchOFW(){
        ProgressDialog progressDialog = new ProgressDialog(SearchRecordActivity.this);
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

                    ContactsDO contactsDO = new ContactsDO(ofw.getId(),
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
            }

            @Override
            public void onFailure(Call<OFWResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
