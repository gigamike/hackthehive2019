package com.gigabytes.freebee.homescreen.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.credits.views.activities.CreditsActivity;
import com.gigabytes.freebee.login.views.activities.LoginActivity;
import com.gigabytes.freebee.utilities.SharedPrefManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends AppCompatActivity {
    @InjectView(R.id.lblFullname) TextView lblFullname;
    @InjectView(R.id.imgProfilePic) ImageView imgProfilePic;
    @InjectView(R.id.btnBuyCredits) LinearLayout btnBuyCredits;
    @InjectView(R.id.btnAbout) LinearLayout btnAbout;
    @InjectView(R.id.btnLogout) LinearLayout btnLogout;

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    private FirebaseFirestore db;

    private void stopUserLiveNotificationSession(String userId) {
        ProgressDialog logoutProgressDialog = new ProgressDialog(ProfileActivity.this);;

        logoutProgressDialog.setIndeterminate(true);
        logoutProgressDialog.setMessage("Logging out...");
        logoutProgressDialog.setCancelable(false);
        logoutProgressDialog.show();

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                .document(userId)
                .update("isLogin", false,
                        "isOnCall", false)
                .addOnSuccessListener(command -> {
                    logoutProgressDialog.dismiss();
                    SharedPrefManager.getInstance(getApplicationContext()).logoutUser();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) getApplication();

        lblFullname.setText(freeBeeApplication.fullName);
        Picasso.with(getApplicationContext()).load(Objects.requireNonNull(freeBeeApplication).userPictureURL).placeholder(R.drawable.no_image).into(imgProfilePic);

        btnBuyCredits.setOnClickListener(v -> {
            Intent creditsActivityIntent = new Intent(this, CreditsActivity.class);
            startActivity(creditsActivityIntent);
        });

        btnAbout.setOnClickListener(v -> {

        });

        btnLogout.setOnClickListener(v -> showLogoutPrompt("Confirm", "Are you sure you want to logout?"));
    }

    public void showLogoutPrompt(String title, String message) {
        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) getApplication();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, id) -> {
                    stopUserLiveNotificationSession(freeBeeApplication.userId);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
