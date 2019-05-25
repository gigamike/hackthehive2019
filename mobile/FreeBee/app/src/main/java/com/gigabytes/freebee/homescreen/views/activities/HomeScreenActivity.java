package com.gigabytes.freebee.homescreen.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gigabytes.freebee.BuildConfig;
import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.homescreen.views.adapters.HomeScreenViewPagerAdapter;
import com.gigabytes.freebee.homescreen.views.fragments.CallFragment;
import com.gigabytes.freebee.homescreen.views.fragments.CoOfwFragment;
import com.gigabytes.freebee.homescreen.views.fragments.ContactsFragment;
import com.gigabytes.freebee.homescreen.views.fragments.HistoryFragment;
import com.gigabytes.freebee.homescreen.views.model.ContactsAPI;
import com.gigabytes.freebee.login.views.activities.LoginActivity;
import com.gigabytes.freebee.services.UserLiveNotificationSessionService;
import com.gigabytes.freebee.utilities.SharedPrefManager;
import com.gigabytes.freebee.videocall.views.activities.VideoCallActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import im.dlg.dialer.DialpadActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static im.dlg.dialer.DialpadActivity.EXTRA_FORMAT_AS_YOU_TYPE;

public class HomeScreenActivity extends AppCompatActivity {

    private MenuItem prevMenuItem;
    private ViewPager viewPager;

    private FirebaseFirestore db;

    private final String IS_CALL_ACCEPTED_LISTENER = "IsCallAcceptedListener";
    private final String IS_CALL_INITIALIZED_LISTENER = "IsCallInitializedListener";
    private final String IS_CALL_REJECTED_LISTENER = "IsCallRejectedListener";
    private final String IS_ON_CALL_LISTENER = "IsOnCallListener";

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    private final String USER_ID = "userId";

    private FloatingActionButton dialerFloatingActionButton;

    private void startListeningForCalls(String userId) {
        Intent userLiveNotificationSessionServiceIntent = new Intent(this, UserLiveNotificationSessionService.class);
        userLiveNotificationSessionServiceIntent.putExtra("userId", userId);
        stopService(userLiveNotificationSessionServiceIntent);
        startService(userLiveNotificationSessionServiceIntent);
    }

    private void clearUserCallerState(String userId) {
        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, userId)
                .get().addOnCompleteListener(task1 -> {
            String userDocumentId = task1.getResult().getDocuments().get(0).getId();

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_ON_CALL_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isOnCallListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_ON_CALL_LISTENER)
                                .document(isOnCallListenerId)
                                .update("callerId", "",
                                        "isOnCall", false);
                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_CALL_REJECTED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isCallRejectedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_CALL_REJECTED_LISTENER)
                                .document(isCallRejectedListenerId)
                                .update("isCallRejected", false,
                                        "userCallRole", "");

                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_CALL_ACCEPTED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isCallAcceptedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_CALL_ACCEPTED_LISTENER)
                                .document(isCallAcceptedListenerId)
                                .update("calleeId", "",
                                        "isCallAccepted", false);

                        startListeningForCalls(userId);
                    });


        });


    }

    private void clearUserCalleeState(String userId) {
        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, userId)
                .get().addOnCompleteListener(task1 -> {

            String userDocumentId = task1.getResult().getDocuments().get(0).getId();

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_ON_CALL_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isOnCallListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_ON_CALL_LISTENER)
                                .document(isOnCallListenerId)
                                .update("callerId", "",
                                        "isOnCall", false);
                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_CALL_REJECTED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isCallRejectedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_CALL_REJECTED_LISTENER)
                                .document(isCallRejectedListenerId)
                                .update("isCallRejected", false,
                                        "userCallRole", "");
                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .collection(IS_CALL_INITIALIZED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String isCallInitializedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(userDocumentId)
                                .collection(IS_CALL_INITIALIZED_LISTENER)
                                .document(isCallInitializedListenerId)
                                .update("isCallInitialized", false,
                                        "sessionRoomName", "");

                        startListeningForCalls(userId);
                    });

        });

    }

    private void updateOnCallState(String userId) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();

        Log.d("debug", "user state " + sharedPreferences.getAll());

        if (sharedPreferences.contains("isOnCall") &&
                sharedPreferences.contains(VideoCallActivity.USER_CALL_ROLE)) {
            if (sharedPreferences.getBoolean("isOnCall", false)) {
                if (sharedPreferences.getString(VideoCallActivity.USER_CALL_ROLE, "").equals(VideoCallActivity.USER_CALLER)) {
                    Log.d("debug", "caller is on call");
                    clearUserCallerState(userId);
                } else if (sharedPreferences.getString(VideoCallActivity.USER_CALL_ROLE, "").equals(VideoCallActivity.USER_CALLEE)) {
                    Log.d("debug", "callee is on call");
                    clearUserCalleeState(userId);
                }
            }
            sharedPreferenceEditor.clear();
            sharedPreferenceEditor.apply();
        } else {
            startListeningForCalls(userId);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contacts:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_co_ofw:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_call:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_history:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private void stopUserLiveNotificationSession(String userId) {
        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                .document(userId)
                .update("isLogin", false)
                .addOnSuccessListener(command -> {
                    //logout complete
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hackthehive2019.gigamike.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        dialerFloatingActionButton = findViewById(R.id.dialerFloatingActionButton);

        dialerFloatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, DialpadActivity.class);
            intent.putExtra(EXTRA_FORMAT_AS_YOU_TYPE, true);
            startActivityForResult(intent, 100); // any result request code is ok
        });

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            FreeBeeApplication freeBeeApplication = (FreeBeeApplication) getApplication();
            freeBeeApplication.userId = SharedPrefManager.getInstance(this).getUserId();
            freeBeeApplication.userRole = SharedPrefManager.getInstance(this).getUserRole();
            freeBeeApplication.fullName = SharedPrefManager.getInstance(this).getUserFullname();
            freeBeeApplication.userPictureURL = SharedPrefManager.getInstance(this).getUserProfilePic();
            freeBeeApplication.email = SharedPrefManager.getInstance(this).getEmailAddress();
            freeBeeApplication.organization = SharedPrefManager.getInstance(this).getOrganization();
            freeBeeApplication.city = SharedPrefManager.getInstance(this).getCity();
            freeBeeApplication.country = SharedPrefManager.getInstance(this).getCountry();
            freeBeeApplication.mobileNumber = SharedPrefManager.getInstance(this).getMobileNumber();

            updateOnCallState(freeBeeApplication.userId);

            ContactsAPI contactsAPI = retrofit.create(ContactsAPI.class);
            contactsAPI.updateUserLastLogin(Integer.valueOf(freeBeeApplication.userId)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    //do nothing
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    //do nothing
                }
            });
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.viewpager);

        HomeScreenViewPagerAdapter adapter = new HomeScreenViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ContactsFragment.createInstance());
        adapter.addFragment(CoOfwFragment.createInstance());
        adapter.addFragment(CallFragment.createInstance());
        adapter.addFragment(HistoryFragment.createInstance());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 1) {
                    dialerFloatingActionButton.show();
                } else {
                    dialerFloatingActionButton.hide();
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String formatted = data.getStringExtra(DialpadActivity.EXTRA_RESULT_FORMATTED);
            String raw = data.getStringExtra(DialpadActivity.EXTRA_RESULT_RAW);

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + formatted));
            startActivity(callIntent);


            Log.d("debug", "formatted: " + formatted);
            Log.d("debug", "raw: " + raw);
        }
    }

}
