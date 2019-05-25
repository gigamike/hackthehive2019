package com.gigabytes.freebee.registration.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.login.views.activities.LoginActivity;
import com.gigabytes.freebee.registration.models.Countries;
import com.gigabytes.freebee.registration.models.CountriesResponse;
import com.gigabytes.freebee.registration.models.Errors;
import com.gigabytes.freebee.registration.models.Organizations;
import com.gigabytes.freebee.registration.models.OrganizationsResponse;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
import com.gigabytes.freebee.registration.models.RegistrationVolunteer;
import com.gigabytes.freebee.registration.models.VolunteerRegistrationResponse;
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

public class VolunteerActivity extends AppCompatActivity {

    @InjectView(R.id.btnGoToLoginPage) TextView btnGoToLoginPage;
//    @InjectView(R.id.previewImage) CircleImageView previewImage;
//    @InjectView(R.id.btnChoosePicture) Button btnChoosePicture;
    @InjectView(R.id.frmRegister_txtEmail) EditText frmRegister_txtEmail;
    @InjectView(R.id.frmRegister_txtPassword) EditText frmRegister_txtPassword;
    @InjectView(R.id.frmRegister_txtFirstname) EditText frmRegister_txtFirstname;
    @InjectView(R.id.frmRegister_txtMiddleInitial) EditText frmRegister_txtMiddleInitial;
    @InjectView(R.id.frmRegister_txtLastname) EditText frmRegister_txtLastname;
    @InjectView(R.id.frmRegister_cmbOrganization) MaterialBetterSpinner frmRegister_cmbOrganization;
    @InjectView(R.id.frmRegister_cmbCurrentCountry) MaterialBetterSpinner frmRegister_cmbCurrentCountry;
    @InjectView(R.id.frmRegister_txtCurrentCity) EditText frmRegister_txtCurrentCity;
    @InjectView(R.id.frmRegister_txtMobileNumber) EditText frmRegister_txtMobileNumber;
    @InjectView(R.id.btnCreateVolunteer) AppCompatButton btnCreateVolunteer;

    ArrayList<Organizations> organizationsList;
    ArrayList<Countries> countriesList;

    Retrofit retrofit;
    ProgressDialog progressDialog;

    String email,password,firstname,middlename,lastname,mobileNumber,currentCity;
    int countryID, organizationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        ButterKnife.inject(this);

        //Check and ask for permissions in version Android API 23 and above.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkPermissions();
//        }

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        organizationsList = new ArrayList<>();
        countriesList = new ArrayList<>();

        btnGoToLoginPage.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        btnCreateVolunteer.setOnClickListener(v -> {
            registerVolunteeer();
        });

        loadOrganizations();
        loadCountries();
    }

    private void loadOrganizations(){
        ProgressDialog progressDialog = new ProgressDialog(VolunteerActivity.this);
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

                frmRegister_cmbOrganization.setAdapter(new ArrayAdapter<>(VolunteerActivity.this, android.R.layout.simple_spinner_dropdown_item, organizationsList));
                frmRegister_cmbOrganization.setOnItemClickListener((parent, view, position, id) -> {
                    frmRegister_cmbOrganization.setSelection(position);
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
        ProgressDialog progressDialog = new ProgressDialog(VolunteerActivity.this);
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

                frmRegister_cmbCurrentCountry.setAdapter(new ArrayAdapter<>(VolunteerActivity.this, android.R.layout.simple_spinner_dropdown_item, countriesList));
                frmRegister_cmbCurrentCountry.setOnItemClickListener((parent, view, position, id) -> {
                    frmRegister_cmbCurrentCountry.setSelection(position);
                    countryID = Integer.parseInt(countriesList.get(position).getId());
                    Log.d("debug", "Selected: " + countryID);
                });
            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void registerVolunteeer(){
        progressDialog = new ProgressDialog(VolunteerActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account for volunteer...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        email = frmRegister_txtEmail.getText().toString().trim();
        password = frmRegister_txtPassword.getText().toString().trim();
        firstname = frmRegister_txtFirstname.getText().toString().trim();
        middlename = frmRegister_txtMiddleInitial.getText().toString().trim();
        lastname = frmRegister_txtLastname.getText().toString().trim();
        mobileNumber = frmRegister_txtMobileNumber.getText().toString().trim();
        currentCity = frmRegister_txtCurrentCity.getText().toString().trim();

        RegistrationVolunteer registrationVolunteer = new RegistrationVolunteer(email,password, countryID, currentCity, organizationID, firstname, middlename, lastname, mobileNumber);

        Log.d("debug", registrationVolunteer.toString());

        RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);

        registrationAPI.registerVolunteer(registrationVolunteer).enqueue(new Callback<VolunteerRegistrationResponse>() {
            @Override
            public void onResponse(Call<VolunteerRegistrationResponse> call, Response<VolunteerRegistrationResponse> response) {
                progressDialog.dismiss();

                Errors errors = response.body().getError();
                Log.d("debug", "Errors: " + errors);

                //String userID = response.body().getUserID();
                //Log.d("debug", "UserID: " + userID);

                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onFailure(Call<VolunteerRegistrationResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }
}
