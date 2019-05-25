package com.gigabytes.freebee.registration.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.login.views.activities.LoginActivity;
import com.gigabytes.freebee.registration.models.CountriesResponse;
import com.gigabytes.freebee.registration.models.OrganizationsResponse;
import com.gigabytes.freebee.registration.models.RegistrationAPI;
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

public class VolunteerActivity extends AppCompatActivity {

    @InjectView(R.id.btnGoToLoginPage) TextView btnGoToLoginPage;
    @InjectView(R.id.previewImage) CircleImageView previewImage;
    @InjectView(R.id.btnChoosePicture) Button btnChoosePicture;
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

    ArrayList<String> organizationsList,countriesList;
    Retrofit retrofit;

    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        ButterKnife.inject(this);

        //Check and ask for permissions in version Android API 23 and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        organizationsList = new ArrayList<>();
        countriesList = new ArrayList<>();

        btnGoToLoginPage.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        btnChoosePicture.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(galleryIntent, 0);
        });

        btnCreateVolunteer.setOnClickListener(v -> {
            //do tasks here
        });

        loadOrganizations();
        loadCountries();
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                } else {
                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                previewImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
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
                    organizationsList.add(organization.getOrganization());
                }

                frmRegister_cmbOrganization.setAdapter(new ArrayAdapter<>(VolunteerActivity.this, android.R.layout.simple_spinner_dropdown_item, organizationsList));
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
                    countriesList.add(country.getCountryName());
                }

                frmRegister_cmbCurrentCountry.setAdapter(new ArrayAdapter<>(VolunteerActivity.this, android.R.layout.simple_spinner_dropdown_item, countriesList));
            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("error", t.getMessage());
            }
        });
    }
}
