package com.gigabytes.freebee.homescreen.views.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.activities.ProfileActivity;
import com.gigabytes.freebee.homescreen.views.activities.ViewOtherProfileActivity;
import com.gigabytes.freebee.homescreen.views.model.ContactsDO;
import com.gigabytes.freebee.videocall.views.activities.VideoCallActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.model.Document;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ContactsListRecyclerViewAdapter extends RecyclerView.Adapter<ContactsListRecyclerViewAdapter.ContactListViewHolder> {
    private List<ContactsDO> volunteerList;
    private Context context;

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    public static class ContactListViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.cardView_volunteer) CardView cardView_volunteer;
        @InjectView(R.id.imgVolunteerProfilePic) CircleImageView imgVolunteerProfilePic;
        @InjectView(R.id.lblVolunteerName) TextView lblVolunteerName;
        @InjectView(R.id.lblVolunteerOrganization) TextView lblVolunteerOrganization;
        @InjectView(R.id.lblVolunteerLocation) TextView lblVolunteerLocation;
        @InjectView(R.id.lblVolunteerDistance) TextView lblVolunteerDistance;
        @InjectView(R.id.lblVolunteerStatus) TextView lblVolunteerStatus;
        @InjectView(R.id.btnTalkToMe) AppCompatButton btnTalkToMe;

        public ContactListViewHolder(View view) {
            super(view);
            ButterKnife.inject(this,view);
        }
    }

    public ContactsListRecyclerViewAdapter(List<ContactsDO> volunteerList, Context context) {
        this.volunteerList = volunteerList;
        this.context = context;
    }

    @Override
    public ContactsListRecyclerViewAdapter.ContactListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volunteer, parent, false);
        ContactListViewHolder vh = new ContactListViewHolder(v);
        return vh;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(ContactListViewHolder viewHolder, int position) {
        final ContactsDO contactsDO = volunteerList.get(position);

        StringBuilder sb = new StringBuilder();
        sb.append(contactsDO.getFirstname());
        sb.append(" ");
        sb.append(contactsDO.getLastname());

        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) context.getApplicationContext();

        if(freeBeeApplication.userRole.equals("ofw")){
            viewHolder.lblVolunteerOrganization.setVisibility(View.GONE);
        }else if(freeBeeApplication.userRole.equals("volunteer")){
            viewHolder.lblVolunteerOrganization.setVisibility(View.VISIBLE);
        }else{
            viewHolder.lblVolunteerOrganization.setVisibility(View.GONE);
        }

        viewHolder.lblVolunteerName.setText(sb.toString());
        viewHolder.lblVolunteerOrganization.setText(contactsDO.getOrganization());
        viewHolder.lblVolunteerLocation.setText(contactsDO.getCity() + ", " + contactsDO.getCountry());
        viewHolder.lblVolunteerDistance.setText(contactsDO.getDistance() + " KM.");

        if(contactsDO.isOnline()) {
            viewHolder.lblVolunteerStatus.setText("Online");
            viewHolder.lblVolunteerStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
        } else {
            viewHolder.lblVolunteerStatus.setText("Offline");
            viewHolder.lblVolunteerStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        Picasso.with(context).load(contactsDO.getProfilePic()).placeholder(R.drawable.no_image).into(viewHolder.imgVolunteerProfilePic);

        viewHolder.cardView_volunteer.setOnClickListener(v -> {
            Intent otherProfileIntent = new Intent(context, ViewOtherProfileActivity.class);
            otherProfileIntent.putExtra("userId", contactsDO.getId());
            otherProfileIntent.putExtra("fullName", contactsDO.getFirstname() + " " + contactsDO.getLastname());
            otherProfileIntent.putExtra("organization", contactsDO.getOrganization());
            otherProfileIntent.putExtra("userPictureURL", contactsDO.getProfilePic());
            otherProfileIntent.putExtra("city", contactsDO.getCity());
            otherProfileIntent.putExtra("country", contactsDO.getCountry());
            otherProfileIntent.putExtra("mobileNumber", contactsDO.getMobileNumber());
            otherProfileIntent.putExtra("isOnline", contactsDO.isOnline());
            context.startActivity(otherProfileIntent);
        });

        viewHolder.btnTalkToMe.setOnClickListener(v -> {

            FirebaseFirestore  db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            db.setFirestoreSettings(settings);

            if(viewHolder.lblVolunteerStatus.getText().equals("Online")) {

                ProgressDialog callingProgressDialog = new ProgressDialog(context);

                callingProgressDialog.setIndeterminate(true);
                callingProgressDialog.setMessage("Please wait...");
                callingProgressDialog.setCancelable(false);
                callingProgressDialog.show();

                db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                  .whereEqualTo("userId", contactsDO.getId()).get().addOnCompleteListener(command -> {

                      List<DocumentSnapshot> documentList = command.getResult().getDocuments();

                      if (documentList == null || documentList.isEmpty()) {
                          Toast.makeText(context, "Cannot call, User is offline", Toast.LENGTH_LONG).show();
                      }

                      DocumentSnapshot documentSnapshot = documentList.get(0);

                      if (!documentSnapshot.getBoolean("isOnCall")) {

                          db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                  .document(freeBeeApplication.userId)
                                  .update("isOnCall", true).addOnSuccessListener(command1 -> {
                                  db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                          .document(contactsDO.getId())
                                          .update("isOnCall", true).addOnSuccessListener(command2 -> {

                                      callingProgressDialog.dismiss();

                                      Intent videoCallActivityIntent = new Intent(context, VideoCallActivity.class);

                                      videoCallActivityIntent.putExtra(VideoCallActivity.USER_CALL_ROLE, VideoCallActivity.USER_CALLER);

                                      videoCallActivityIntent.putExtra("userId", freeBeeApplication.userId);
                                      videoCallActivityIntent.putExtra("calleeId", contactsDO.getId());
                                      videoCallActivityIntent.putExtra("userPictureURL", contactsDO.getProfilePic());
                                      videoCallActivityIntent.putExtra("fullName", viewHolder.lblVolunteerName.getText());

                                      Log.d("debug", "called by " + freeBeeApplication.userId);
                                      Log.d("debug", "calling user id " + contactsDO.getId());

                                      context.startActivity(videoCallActivityIntent);
                                  });
                          });

                      } else {
                          callingProgressDialog.dismiss();
                          Toast.makeText(context, "Cannot call, User is on call", Toast.LENGTH_LONG).show();
                      }

                });

            } else {
                if(StringUtils.isBlank(contactsDO.getMobileNumber())) {
                    Toast.makeText(context, "User is not available to call right now", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + contactsDO.getMobileNumber()));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }
}
