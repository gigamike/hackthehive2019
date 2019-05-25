package com.gigabytes.freebee.login.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.activities.HomeScreenActivity;
import com.gigabytes.freebee.login.models.LoginAPI;
import com.gigabytes.freebee.login.models.LoginResponse;
import com.gigabytes.freebee.login.models.OFW;
import com.gigabytes.freebee.login.models.OFWResponse;
import com.gigabytes.freebee.login.models.Volunteers;
import com.gigabytes.freebee.login.models.VolunteersResponse;
import com.gigabytes.freebee.registration.views.OFWActivity;
import com.gigabytes.freebee.registration.views.VolunteerActivity;
import com.gigabytes.freebee.utilities.SharedPrefManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.frmLoginAccount_txtEmail) EditText frmLoginAccount_txtEmail;
    @InjectView(R.id.frmLoginAccount_txtPassword) EditText frmLoginAccount_txtPassword;
    @InjectView(R.id.frmLoginAccount_btnLoginAccount) Button frmLoginAccount_btnLoginAccount;
    @InjectView(R.id.btnGoToOFWRegistration) TextView btnGoToOFWRegistration;
    @InjectView(R.id.btnGoToVolunteerRegistration) TextView btnGoToVolunteerRegistration;

    ProgressDialog progressDialog;
    private String emailAddress,password;

    private final String IS_CALL_ACCEPTED_LISTENER = "IsCallAcceptedListener";
    private final String IS_CALL_INITIALIZED_LISTENER = "IsCallInitializedListener";
    private final String IS_CALL_REJECTED_LISTENER = "IsCallRejectedListener";
    private final String IS_ON_CALL_LISTENER = "IsOnCallListener";

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    private final String USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, HomeScreenActivity.class));
        }

        frmLoginAccount_btnLoginAccount.setOnClickListener(v -> {
            emailAddress = frmLoginAccount_txtEmail.getText().toString();
            password = frmLoginAccount_txtPassword.getText().toString();

            try{
                validate();
            }catch(Exception ex){
                Toast.makeText(LoginActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        });

        btnGoToOFWRegistration.setOnClickListener(v -> startActivity(new Intent(this, OFWActivity.class)));
        btnGoToVolunteerRegistration.setOnClickListener(v -> startActivity(new Intent(this, VolunteerActivity.class)));
    }

    public void validate(){
        if(TextUtils.isEmpty(emailAddress)){
            frmLoginAccount_txtEmail.setError("Please enter your email!");
            frmLoginAccount_txtEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            frmLoginAccount_txtPassword.setError("Please enter your password!");
            frmLoginAccount_txtPassword.requestFocus();
        }else {

            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    () -> loginUser(), 3000);
        }
    }

    private void loginUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginAPI loginAPI = retrofit.create(LoginAPI.class);
        loginAPI.loginUser(emailAddress, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(!loginResponse.isLogged()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid email and password! Please try again!", Toast.LENGTH_LONG).show();
                }else{

                    FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getApplicationContext();
                    String userId = loginResponse.getUserID();
                    String userRole = loginResponse.getRole();

                    freeBeeApplication.userRole = userRole;

                    switch (userRole){
                        case "ofw":
                            loginAPI.getOFWByUserId(userId).enqueue(new Callback<OFWResponse>() {
                                @Override
                                public void onResponse(Call<OFWResponse> call, Response<OFWResponse> response) {

                                    List<OFW> ofwList = response.body().getResults();

                                    if(ofwList == null || ofwList.isEmpty()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    OFW ofw = ofwList.get(0);

                                    freeBeeApplication.fullName = ofw.getFirstName() + " " + ofw.getMiddleName() + " " +  ofw.getLastName();
                                    freeBeeApplication.isLogin = ofw.isOnline();
                                    freeBeeApplication.userId = ofw.getId();
                                    freeBeeApplication.userPictureURL = ofw.getProfilePic();

                                    freeBeeApplication.organization = ofw.getOrganization();
                                    freeBeeApplication.city = ofw.getCity();
                                    freeBeeApplication.country = ofw.getCountry();
                                    freeBeeApplication.mobileNumber = ofw.getMobileNumber();

                                    startUserLiveNotificationSession(freeBeeApplication.userId,
                                            freeBeeApplication.fullName,
                                            freeBeeApplication.userPictureURL);

                                }

                                @Override
                                public void onFailure(Call<OFWResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                            break;
                        case "volunteer":
                            loginAPI.getVolunteerByUserId(userId).enqueue(new Callback<VolunteersResponse>() {
                                @Override
                                public void onResponse(Call<VolunteersResponse> call, Response<VolunteersResponse> response) {

                                    List<Volunteers> volunteerList = response.body().getResults();

                                    if(volunteerList == null || volunteerList.isEmpty()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    Volunteers volunteers = volunteerList.get(0);

                                    freeBeeApplication.fullName = volunteers.getFirstName() + " " + volunteers.getMiddleName() + " " +  volunteers.getLastName();
                                    freeBeeApplication.isLogin = volunteers.isOnline();
                                    freeBeeApplication.userId = volunteers.getId();
                                    freeBeeApplication.userPictureURL = volunteers.getProfilePic();

                                    freeBeeApplication.organization = volunteers.getOrganization();
                                    freeBeeApplication.city = volunteers.getCity();
                                    freeBeeApplication.country = volunteers.getCountry();
                                    freeBeeApplication.mobileNumber = volunteers.getMobileNumber();

                                    startUserLiveNotificationSession(freeBeeApplication.userId,
                                            freeBeeApplication.fullName,
                                            freeBeeApplication.userPictureURL);

                                }

                                @Override
                                public void onFailure(Call<VolunteersResponse> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startUserLiveNotificationSession(String userId,
                                                  String fullName,
                                                  String userPictureURL) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        Map<String, Object> userData = new HashMap<>();

        userData.put("isLogin", true);
        userData.put("userId", userId);
        userData.put("fullName", fullName);
        userData.put("userPictureURL", userPictureURL);
        userData.put("isOnCall", false);

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener(command -> {
                    if (command.isSuccessful()) {
                        if(command.getResult().exists()) {
                            Log.d("debug", "user exists");
                            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                    .document(userId)
                                    .update("isLogin", true).addOnSuccessListener(command1 -> {

                                progressDialog.dismiss();

                                FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getApplicationContext();

                                SharedPrefManager.getInstance(getApplicationContext())
                                        .loginUser(emailAddress, freeBeeApplication);

                                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(intent);
                                finish();

                                Log.d("debug", "started user notification session");

                            });
                        } else {
                            Log.d("debug", "user not exists");

                            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                    .document(userId)
                                    .set(userData).addOnSuccessListener(command1 -> {
                                Map<String, Object> isCallAcceptedUserData = new HashMap<>();

                                isCallAcceptedUserData.put("calleeId", "");
                                isCallAcceptedUserData.put("isCallAccepted", false);

                                db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                        .document(userId)
                                        .collection(IS_CALL_ACCEPTED_LISTENER)
                                        .document(userId)
                                        .set(isCallAcceptedUserData).addOnSuccessListener(command2 -> {

                                    Map<String, Object> isCallInitialized = new HashMap<>();

                                    isCallInitialized.put("isCallInitialized", false);
                                    isCallInitialized.put("sessionRoomName", "");

                                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                            .document(userId)
                                            .collection(IS_CALL_INITIALIZED_LISTENER)
                                            .document(userId)
                                            .set(isCallInitialized).addOnSuccessListener(command3 -> {

                                        Map<String, Object> isCallRejected = new HashMap<>();

                                        isCallRejected.put("isCallRejected", false);
                                        isCallRejected.put("userCallRole", "");

                                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                                .document(userId)
                                                .collection(IS_CALL_REJECTED_LISTENER)
                                                .document(userId)
                                                .set(isCallRejected)
                                                .addOnSuccessListener(command4 -> {

                                                    Map<String, Object> isOnCall = new HashMap<>();

                                                    isOnCall.put("callerId", "");
                                                    isOnCall.put("isOnCall", false);
                                                    isOnCall.put("isVoiceCall", false);

                                                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                                            .document(userId)
                                                            .collection(IS_ON_CALL_LISTENER)
                                                            .document(userId)
                                                            .set(isOnCall)
                                                            .addOnSuccessListener(command5 -> {

                                                                progressDialog.dismiss();

                                                                FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getApplicationContext();

                                                                SharedPrefManager.getInstance(getApplicationContext())
                                                                        .loginUser(emailAddress, freeBeeApplication);

                                                                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                                startActivity(intent);
                                                                finish();

                                                                Log.d("debug", "started user notification session");
                                                            });

                                                });
                                    });
                                });


                            });

                        }
                    }
                });


    }
}
