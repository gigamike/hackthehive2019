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
import com.gigabytes.freebee.registration.models.OFWRegistrationResponse;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
import com.gigabytes.freebee.registration.models.RegistrationOFW;
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

public class OFWActivity extends AppCompatActivity {
    @InjectView(R.id.btnGoToLoginPage) TextView btnGoToLoginPage;
    //@InjectView(R.id.previewImage) CircleImageView previewImage;
    //@InjectView(R.id.btnChoosePicture) Button btnChoosePicture;
    @InjectView(R.id.frmRegister_txtEmail) EditText frmRegister_txtEmail;
    @InjectView(R.id.frmRegister_txtPassword) EditText frmRegister_txtPassword;
    @InjectView(R.id.frmRegister_txtFirstname) EditText frmRegister_txtFirstname;
    @InjectView(R.id.frmRegister_txtMiddleInitial) EditText frmRegister_txtMiddleInitial;
    @InjectView(R.id.frmRegister_txtLastname) EditText frmRegister_txtLastname;
    @InjectView(R.id.frmRegister_cmbCurrentCountry) MaterialBetterSpinner frmRegister_cmbCurrentCountry;
    @InjectView(R.id.frmRegister_txtCurrentCity) EditText frmRegister_txtCurrentCity;
    @InjectView(R.id.frmRegister_txtMobileNumber) EditText frmRegister_txtMobileNumber;
    @InjectView(R.id.btnCreateOFW) AppCompatButton btnCreateOFW;

    ProgressDialog progressDialog;
    ArrayList<Countries> countriesList;
    Retrofit retrofit;

    String email,password,firstname,middlename,lastname,mobileNumber,currentCity;
    int countryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofw);
        ButterKnife.inject(this);

        /*
        //Check and ask for permissions in version Android API 23 and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        */

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        countriesList = new ArrayList<>();

        btnGoToLoginPage.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        /*
        btnChoosePicture.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(galleryIntent, 0);
        });
        */

        loadCountries();

        btnCreateOFW.setOnClickListener(v -> {
            registerOFW();
        });
    }

    /*
    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);

        }
    }
    */

    private void loadCountries(){
        progressDialog = new ProgressDialog(OFWActivity.this);
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
                    Countries countries = new Countries(country.getId(),country.getCountryName());
                    countriesList.add(countries);
                }

                frmRegister_cmbCurrentCountry.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, countriesList));

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

    private void registerOFW(){
        progressDialog = new ProgressDialog(OFWActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account for ofw...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        email = frmRegister_txtEmail.getText().toString().trim();
        password = frmRegister_txtPassword.getText().toString().trim();
        firstname = frmRegister_txtFirstname.getText().toString().trim();
        middlename = frmRegister_txtMiddleInitial.getText().toString().trim();
        lastname = frmRegister_txtLastname.getText().toString().trim();
        mobileNumber = frmRegister_txtMobileNumber.getText().toString().trim();
        currentCity = frmRegister_txtCurrentCity.getText().toString().trim();

        RegistrationOFW registrationOFW = new RegistrationOFW(email,password, countryID, currentCity, firstname, middlename, lastname, mobileNumber);

        Log.d("debug", registrationOFW.toString());

        RegistrationAPI registrationAPI = retrofit.create(RegistrationAPI.class);

        registrationAPI
                .registerOFW(registrationOFW)
                .enqueue(new Callback<OFWRegistrationResponse>() {
            @Override
            public void onResponse(Call<OFWRegistrationResponse> call, Response<OFWRegistrationResponse> response) {
                progressDialog.dismiss();

                Errors errors = response.body().getError();
                Log.d("debug", "Errors: " + errors);

                //String userID = response.body().getUserID();
                //Log.d("debug", "UserID: " + userID);

                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onFailure(Call<OFWRegistrationResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }
}
