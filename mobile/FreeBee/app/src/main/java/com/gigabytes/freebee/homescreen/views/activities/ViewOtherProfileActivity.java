package com.gigabytes.freebee.homescreen.views.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.model.SendMessageAPI;
import com.gigabytes.freebee.login.views.activities.LoginActivity;
import com.gigabytes.freebee.videocall.views.activities.VideoCallActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewOtherProfileActivity extends AppCompatActivity {

    @InjectView(R.id.buttonBackbutton) ImageButton buttonBackbutton;
    @InjectView(R.id.imgProfilePic) CircleImageView imgProfilePic;
    @InjectView(R.id.lblFullname) TextView lblFullname;
    @InjectView(R.id.lblOrganization) TextView lblOrganization;

    @InjectView(R.id.lblCity) TextView lblCity;
    @InjectView(R.id.lblCountry) TextView lblCountry;
    @InjectView(R.id.lblMobileNo) TextView lblMobileNo;

    @InjectView(R.id.btnVideoCall) LinearLayout btnVideoCall;
    @InjectView(R.id.btnVoiceCall) LinearLayout btnVoiceCall;
    @InjectView(R.id.btnDirectCall) LinearLayout btnDirectCall;
    @InjectView(R.id.btnDirectSMS) LinearLayout btnDirectSMS;

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_profile);
        ButterKnife.inject(this);

        Intent viewOtherProfileIntent = getIntent();
        FreeBeeApplication freeBeeApplication = (FreeBeeApplication)getApplicationContext();

        buttonBackbutton.setOnClickListener(v -> {
            finish();
        });

        Picasso.with(this).load(viewOtherProfileIntent.getStringExtra("userPictureURL")).placeholder(R.drawable.no_image).into(imgProfilePic);
        lblFullname.setText(viewOtherProfileIntent.getStringExtra("fullName"));
        lblOrganization.setText(viewOtherProfileIntent.getStringExtra("organization"));

        lblCity.setText(viewOtherProfileIntent.getStringExtra("city"));
        lblCountry.setText(viewOtherProfileIntent.getStringExtra("country"));

        if(StringUtils.isBlank(viewOtherProfileIntent.getStringExtra("mobileNumber"))) {
            lblMobileNo.setText("N/A");
        } else {
            lblMobileNo.setText(viewOtherProfileIntent.getStringExtra("mobileNumber"));
        }

        boolean isOnline =  viewOtherProfileIntent.getBooleanExtra("isOnline", false);

        String calleeFullName = viewOtherProfileIntent.getStringExtra("fullName");
        String calleeUserPictureURL = viewOtherProfileIntent.getStringExtra("userPictureURL");
        String calleeMobileNumber = viewOtherProfileIntent.getStringExtra("mobileNumber");

        String calleeId = viewOtherProfileIntent.getStringExtra("userId");
        String callerId = freeBeeApplication.userId;


        btnVideoCall.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            db.setFirestoreSettings(settings);

            if(isOnline) {

                ProgressDialog callingProgressDialog = new ProgressDialog(this);

                callingProgressDialog.setIndeterminate(true);
                callingProgressDialog.setMessage("Please wait...");
                callingProgressDialog.setCancelable(false);
                callingProgressDialog.show();

                db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                        .whereEqualTo("userId", calleeId).get().addOnCompleteListener(command -> {

                    List<DocumentSnapshot> documentList = command.getResult().getDocuments();

                    if (documentList == null || documentList.isEmpty()) {
                        Toast.makeText(this, "Cannot call, User is offline", Toast.LENGTH_LONG).show();
                    }

                    DocumentSnapshot documentSnapshot = documentList.get(0);

                    if (!documentSnapshot.getBoolean("isOnCall")) {

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(callerId)
                                .update("isOnCall", true).addOnSuccessListener(command1 -> {
                            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                    .document(calleeId)
                                    .update("isOnCall", true).addOnSuccessListener(command2 -> {    

                                callingProgressDialog.dismiss();

                                Intent videoCallActivityIntent = new Intent(this, VideoCallActivity.class);

                                videoCallActivityIntent.putExtra(VideoCallActivity.USER_CALL_ROLE, VideoCallActivity.USER_CALLER);

                                videoCallActivityIntent.putExtra("userId", callerId);
                                videoCallActivityIntent.putExtra("calleeId", calleeId);
                                videoCallActivityIntent.putExtra("userPictureURL", calleeUserPictureURL);
                                videoCallActivityIntent.putExtra("fullName", calleeFullName);

                                Log.d("debug", "called by " + callerId);
                                Log.d("debug", "calling user id " + calleeId);

                                startActivity(videoCallActivityIntent);
                            });
                        });

                    } else {
                        callingProgressDialog.dismiss();
                        Toast.makeText(this, "Cannot call, User is on call", Toast.LENGTH_LONG).show();
                    }

                });

            } else {
                Toast.makeText(this, "User is offline", Toast.LENGTH_LONG).show();
            }
        });

        btnVoiceCall.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            db.setFirestoreSettings(settings);

            if(isOnline) {

                ProgressDialog callingProgressDialog = new ProgressDialog(this);

                callingProgressDialog.setIndeterminate(true);
                callingProgressDialog.setMessage("Please wait...");
                callingProgressDialog.setCancelable(false);
                callingProgressDialog.show();

                db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                        .whereEqualTo("userId", calleeId).get().addOnCompleteListener(command -> {

                    List<DocumentSnapshot> documentList = command.getResult().getDocuments();

                    if (documentList == null || documentList.isEmpty()) {
                        Toast.makeText(this, "Cannot call, User is offline", Toast.LENGTH_LONG).show();
                    }

                    DocumentSnapshot documentSnapshot = documentList.get(0);

                    if (!documentSnapshot.getBoolean("isOnCall")) {

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(callerId)
                                .update("isOnCall", true).addOnSuccessListener(command1 -> {
                            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                    .document(calleeId)
                                    .update("isOnCall", true).addOnSuccessListener(command2 -> {

                                callingProgressDialog.dismiss();

                                Intent videoCallActivityIntent = new Intent(this, VideoCallActivity.class);

                                videoCallActivityIntent.putExtra(VideoCallActivity.USER_CALL_ROLE, VideoCallActivity.USER_CALLER);

                                videoCallActivityIntent.putExtra("userId", callerId);
                                videoCallActivityIntent.putExtra("calleeId", calleeId);
                                videoCallActivityIntent.putExtra("userPictureURL", calleeUserPictureURL);
                                videoCallActivityIntent.putExtra("fullName", calleeFullName);
                                videoCallActivityIntent.putExtra("isVoiceCall", true);

                                Log.d("debug", "called by " + callerId);
                                Log.d("debug", "calling user id " + calleeId);

                                startActivity(videoCallActivityIntent);
                            });
                        });

                    } else {
                        callingProgressDialog.dismiss();
                        Toast.makeText(this, "Cannot call, User is on call", Toast.LENGTH_LONG).show();
                    }

                });

            } else {
                Toast.makeText(this, "User is offline", Toast.LENGTH_LONG).show();
            }
        });

        btnDirectCall.setOnClickListener(v -> {
            if (StringUtils.isBlank(calleeMobileNumber)) {
                Toast.makeText(getApplicationContext(), "User is not available to call right now", Toast.LENGTH_LONG).show();
                return;
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + calleeMobileNumber));
            startActivity(callIntent);
        });

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.sms_dialog_layout);
        dialog.setTitle("SMS");
        dialog.setCancelable(true);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Message...");
        progressDialog.setCancelable(false);

        EditText sendMessageEditText = dialog.findViewById(R.id.sendMessageEditText);

        dialog.findViewById(R.id.buttonCancelBuyCreditsDialog).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.findViewById(R.id.buttonSendMessage).setOnClickListener(v -> {

            if(StringUtils.isBlank(sendMessageEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please compose a message", Toast.LENGTH_LONG).show();
                return;
            }

            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://hackthehive2019.gigamike.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SendMessageAPI sendMessageAPI = retrofit.create(SendMessageAPI.class);

            sendMessageAPI.sendMessage(callerId, calleeId, sendMessageEditText.getText().toString()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    sendMessageEditText.setText(StringUtils.EMPTY);
                    Toast.makeText(getApplicationContext(), "Successfully Sent Message", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        });

        btnDirectSMS.setOnClickListener(v -> {
            dialog.show();
        });

    }
}
