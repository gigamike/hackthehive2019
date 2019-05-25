package com.gigabytes.freebee.videocall.views.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gigabytes.freebee.BuildConfig;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.videocall.models.FreeBeeOpenTokAPI;
import com.gigabytes.freebee.videocall.models.OpenTokSessionResponse;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener,
                                                                    PublisherKit.PublisherListener,
                                                                    EasyPermissions.PermissionCallbacks{

    private FirebaseFirestore db;

    public static final String USER_CALLER = "CALLER";
    public static final String USER_CALLEE = "CALLEE";
    public static final String USER_CALL_ROLE = "USER_CALL_ROLE";

    private final String IS_CALL_ACCEPTED_LISTENER = "IsCallAcceptedListener";
    private final String IS_CALL_INITIALIZED_LISTENER = "IsCallInitializedListener";
    private final String IS_CALL_REJECTED_LISTENER = "IsCallRejectedListener";
    private final String IS_ON_CALL_LISTENER = "IsOnCallListener";

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";

    private final String USER_ID = "userId";

    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private static final int CALLING_DURATION_TIMEOUT = 30000;

    private FrameLayout publisherViewContainer;
    private FrameLayout subscriberViewContainer;

    private FloatingActionButton buttonTurnOffMicrophone;

    private String apiKey;
    private String sessionId;
    private String token;

    private Session userSession;

    private Publisher userPublisher;
    private Subscriber userSubscriber;

    private boolean isMicrophoneSwitchOff = false;

    private boolean isCallStarted = false;
    private boolean isCalling = false;

    private ConstraintLayout callingConstraintLayout;
    private ConstraintLayout onCallConstraintLayout;
    private ConstraintLayout videoCallConstraintLayout;

    private ListenerRegistration isCallAcceptedListener;
    private ListenerRegistration isCallIntializedListener;

    private MediaPlayer onCallerSound;
    private MediaPlayer onCalleeSound;

    private Handler callDurationTimeoutHandler;

    private boolean isPermissionOngoing = false;

    private BroadcastReceiver rejectCallBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler().postDelayed(() -> {
                if(intent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
                    stopCallerSound();
                    clearUserCallerState();
                    displayCallerRejectCall();
                } else if(intent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
                    stopCalleeSound();
                    clearUserCalleeState();
                    displayCalleeRejectCall();
                }

                if(userSession != null) {
                    userSession.disconnect();
                }

                isCalling = false;
                callDurationTimeoutHandler.removeCallbacksAndMessages(null);
                new Handler().postDelayed(() -> finish(), 2000);
            },1000);
        }
    };;

    private void stopCallerSound() {
        onCallerSound.stop();
    }

    private void startCallerSound() {
        onCallerSound.start();
    }

    private void stopCalleeSound() {
        onCalleeSound.stop();
    }

    private void startCalleeSound() {
        onCalleeSound.start();
    }

    private void clearUserCallerState() {
        Intent videoCallActivityIntent = getIntent();
        String callerId = videoCallActivityIntent.getStringExtra(USER_ID);

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, callerId)
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
                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
              .document(userDocumentId)
              .update("isOnCall", false);

        });

        if (isCallAcceptedListener != null) {
            isCallAcceptedListener.remove();
        }

    }

    private void clearUserCalleeState() {
        Intent videoCallActivityIntent = getIntent();
        String calleeId = videoCallActivityIntent.getStringExtra(USER_ID);

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, calleeId)
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
                    });

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(userDocumentId)
                    .update("isOnCall", false);

        });

        if (isCallIntializedListener != null) {
            isCallIntializedListener.remove();
        }
    }

    private void displayCallerEndCall() {
        TextView textCallingMessage = callingConstraintLayout.findViewById(R.id.textCallingMessage);
        View buttonCancelCalling = callingConstraintLayout.findViewById(R.id.buttonCancelCalling);

        textCallingMessage.setText("Call ended...");

        buttonCancelCalling.setVisibility(View.GONE);

        publisherViewContainer.removeAllViews();
        publisherViewContainer.setVisibility(View.GONE);
        videoCallConstraintLayout.setVisibility(View.GONE);

        callingConstraintLayout.setVisibility(View.VISIBLE);
    }

    private void displayCalleeEndCall() {
        if (subscriberViewContainer != null) {
            subscriberViewContainer.removeAllViews();
        }

        TextView textCallMessage = onCallConstraintLayout.findViewById(R.id.textCallMessage);

        View buttonAcceptCall = onCallConstraintLayout.findViewById(R.id.buttonAcceptCall);
        View buttonCancelCall = onCallConstraintLayout.findViewById(R.id.buttonCancelCall);

        textCallMessage.setText("Call ended...");

        buttonAcceptCall.setVisibility(View.GONE);
        buttonCancelCall.setVisibility(View.GONE);

        if (publisherViewContainer != null) {
            publisherViewContainer.removeAllViews();
            publisherViewContainer.setVisibility(View.GONE);
        }

        videoCallConstraintLayout.setVisibility(View.GONE);

        onCallConstraintLayout.setVisibility(View.VISIBLE);
    }

    private void displayCallerRejectCall() {
        TextView textCallingMessage = callingConstraintLayout.findViewById(R.id.textCallingMessage);
        View buttonCancelCalling = callingConstraintLayout.findViewById(R.id.buttonCancelCalling);

        textCallingMessage.setText("Call ended...");

        buttonCancelCalling.setVisibility(View.GONE);
    }

    private void displayCalleeRejectCall() {
        TextView textCallMessage = onCallConstraintLayout.findViewById(R.id.textCallMessage);

        View buttonAcceptCall = onCallConstraintLayout.findViewById(R.id.buttonAcceptCall);
        View buttonCancelCall = onCallConstraintLayout.findViewById(R.id.buttonCancelCall);

        textCallMessage.setText("Call ended...");

        buttonAcceptCall.setVisibility(View.GONE);
        buttonCancelCall.setVisibility(View.GONE);
    }

    private void callerRejectCall() {
        Intent videoCallActivityIntent = getIntent();
        String calleeId = videoCallActivityIntent.getStringExtra("calleeId");

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, calleeId)
                .get().addOnCompleteListener(task1 -> {
            String calleeDocumentId = task1.getResult().getDocuments().get(0).getId();

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(calleeDocumentId)
                    .collection(IS_CALL_REJECTED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String calleeIsCallRejectedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(calleeDocumentId)
                                .collection(IS_CALL_REJECTED_LISTENER)
                                .document(calleeIsCallRejectedListenerId)
                                .update("isCallRejected", true,
                                        "userCallRole", USER_CALLEE);
                    });


        });

        if(userSession != null) {
            Log.d("debug", "caller disconnect method call");
            userSession.disconnect();
        }
    }

    private void calleeRejectCall() {
        Intent videoCallActivityIntent = getIntent();
        String callerId = videoCallActivityIntent.getStringExtra("callerId");

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION).whereEqualTo(USER_ID, callerId)
                .get().addOnCompleteListener(task1 -> {
            String callerDocumentId = task1.getResult().getDocuments().get(0).getId();

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .document(callerDocumentId)
                    .collection(IS_CALL_REJECTED_LISTENER)
                    .limit(1)
                    .get()
                    .addOnCompleteListener(task2 -> {
                        String callerIsCallRejectedListenerId = task2.getResult().getDocuments().get(0).getId();

                        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                .document(callerDocumentId)
                                .collection(IS_CALL_REJECTED_LISTENER)
                                .document(callerIsCallRejectedListenerId)
                                .update("isCallRejected", true,
                                        "userCallRole", USER_CALLER);
                    });


        });

        if(userSession != null) {
            Log.d("debug", "callee disconnect method call");
            userSession.disconnect();
        }
    }

    private void toggleMicrophoneOnOrOff() {
        if(!isMicrophoneSwitchOff) {
            Log.d("debug", "microphone switch off");
            buttonTurnOffMicrophone.setImageDrawable(getDrawable(R.drawable.ic_round_mic_off));
            userPublisher.setPublishAudio(false);
            isMicrophoneSwitchOff = true;
        } else {
            Log.d("debug", "microphone switch on");
            buttonTurnOffMicrophone.setImageDrawable(getDrawable(R.drawable.ic_round_mic));
            userPublisher.setPublishAudio(true);
            isMicrophoneSwitchOff = false;
        }
    }

    private void initializeCall() {

        Intent videoCallActivityIntent = getIntent();
        String userid = videoCallActivityIntent.getStringExtra(USER_ID);

        if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
            startCalleeSound();
            onCallConstraintLayout.setVisibility(View.VISIBLE);

            CircleImageView userCallerImage = onCallConstraintLayout.findViewById(R.id.userCallerImage);
            TextView textUserCaller = onCallConstraintLayout.findViewById(R.id.textUserCaller);

            Picasso.with(getApplicationContext()).load(videoCallActivityIntent.getStringExtra("userPictureURL")).placeholder(R.mipmap.ic_launcher_round).into(userCallerImage);
            textUserCaller.setText(videoCallActivityIntent.getStringExtra("fullName"));

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .whereEqualTo(USER_ID, userid)
                    .get().addOnCompleteListener(task -> {

                String documentId = task.getResult().getDocuments().get(0).getId();

                isCallIntializedListener = db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                        .document(documentId)
                        .collection(IS_CALL_INITIALIZED_LISTENER)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            Log.d("debug", "IS_CALL_INITIALIZED_LISTENER invoked on service");

                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);

                            Log.d("debug", "call is initialized " + documentSnapshot.getBoolean("isCallInitialized"));
                            if(documentSnapshot.getBoolean("isCallInitialized") != null &&
                                    documentSnapshot.getBoolean("isCallInitialized")) {

                                String sessionRoomName = documentSnapshot.getString("sessionRoomName");

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://freebee-opentok-server.herokuapp.com/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                FreeBeeOpenTokAPI freeBeeOpenTokAPI = retrofit.create(FreeBeeOpenTokAPI.class);

                                freeBeeOpenTokAPI.createSession(sessionRoomName).enqueue(new Callback<OpenTokSessionResponse>() {
                                    @Override
                                    public void onResponse(Call<OpenTokSessionResponse> call, Response<OpenTokSessionResponse> response) {
                                        OpenTokSessionResponse openTokSessionResponse = response.body();

                                        apiKey = openTokSessionResponse.getApiKey();
                                        sessionId = openTokSessionResponse.getSessionId();
                                        token = openTokSessionResponse.getToken();

                                        requestPermissions();
                                    }

                                    @Override
                                    public void onFailure(Call<OpenTokSessionResponse> call, Throwable t) {

                                    }
                                });

                            }

                        });

            });

        } else if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {

            String calleeId = videoCallActivityIntent.getStringExtra("calleeId");

            startCallerSound();
            callingConstraintLayout.setVisibility(View.VISIBLE);

            CircleImageView userCallingImage = callingConstraintLayout.findViewById(R.id.userCallingImage);
            TextView textUserCalling = callingConstraintLayout.findViewById(R.id.textUserCalling);

            Picasso.with(getApplicationContext()).load(videoCallActivityIntent.getStringExtra("userPictureURL")).placeholder(R.mipmap.ic_launcher_round).into(userCallingImage);
            textUserCalling.setText(videoCallActivityIntent.getStringExtra("fullName"));

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                    .whereEqualTo(USER_ID, userid)
                    .get().addOnCompleteListener(task -> {

                String documentId = task.getResult().getDocuments().get(0).getId();

                isCallAcceptedListener = db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                        .document(documentId)
                        .collection(IS_CALL_ACCEPTED_LISTENER)
                        .addSnapshotListener((queryDocumentSnapshots, e) -> {
                            Log.d("debug", "IS_CALL_ACCEPTED_LISTENER invoked on service");

                            callDurationTimeoutHandler.removeCallbacksAndMessages(null);

                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);

                            if (documentSnapshot.getBoolean("isCallAccepted") != null &&
                                    documentSnapshot.getBoolean("isCallAccepted")) {

                                stopCallerSound();

                                Log.d("debug", "call is accepted");

                                Log.d("debug", documentSnapshotList.size() + " ");

                                TextView textCallingMessage = callingConstraintLayout.findViewById(R.id.textCallingMessage);
                                textCallingMessage.setText("Connecting...");

                                String callerId = userid;
                                String sessionRoomName = callerId + calleeId + new Date();
                                String encryptedSessionRoomName = Base64.encodeToString(sessionRoomName.getBytes(), Base64.NO_PADDING);

                                Log.d("debug", "sessionName "  + encryptedSessionRoomName);

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://freebee-opentok-server.herokuapp.com/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                FreeBeeOpenTokAPI freeBeeOpenTokAPI = retrofit.create(FreeBeeOpenTokAPI.class);

                                freeBeeOpenTokAPI.createSession(encryptedSessionRoomName).enqueue(new Callback<OpenTokSessionResponse>() {
                                    @Override
                                    public void onResponse(Call<OpenTokSessionResponse> call, Response<OpenTokSessionResponse> response) {
                                        OpenTokSessionResponse openTokSessionResponse = response.body();

                                        Log.d("deubg", openTokSessionResponse.toString());

                                        db.collection("UserLiveNotificationSession")
                                                .whereEqualTo("userId", calleeId)
                                                .get()
                                                .addOnCompleteListener(task12 -> {

                                                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                                      .whereEqualTo("userId", calleeId)
                                                      .get()
                                                      .addOnSuccessListener(command -> {

                                                          boolean isOnCall = command.getDocuments().get(0).getBoolean("isOnCall");

                                                          if(isOnCall) {
                                                              List<DocumentSnapshot> documentSnapshotList1 = task12.getResult().getDocuments();

                                                              if (!documentSnapshotList1.isEmpty()) {
                                                                  String calleeDocumentId = task12.getResult().getDocuments().get(0).getId();

                                                                  db.collection("UserLiveNotificationSession")
                                                                          .document(calleeDocumentId)
                                                                          .collection("IsCallInitializedListener")
                                                                          .limit(1)
                                                                          .get()
                                                                          .addOnCompleteListener(task13 -> {
                                                                              String documentIsCallInitializedId = task13.getResult().getDocuments().get(0).getId();

                                                                              db.collection("UserLiveNotificationSession")
                                                                                      .document(calleeDocumentId)
                                                                                      .collection("IsCallInitializedListener")
                                                                                      .document(documentIsCallInitializedId)
                                                                                      .update("sessionRoomName", encryptedSessionRoomName,
                                                                                              "isCallInitialized", true);

                                                                              apiKey = openTokSessionResponse.getApiKey();
                                                                              sessionId = openTokSessionResponse.getSessionId();
                                                                              token = openTokSessionResponse.getToken();

                                                                              Log.d("debug", "SESSION " + openTokSessionResponse);

                                                                              requestPermissions();
                                                                          });

                                                              }
                                                          }

                                                      });

                                                });

                                    }

                                    @Override
                                    public void onFailure(Call<OpenTokSessionResponse> call, Throwable t) {

                                    }
                                });


                            }
                        });

            });

            Query query = db.collection("UserLiveNotificationSession").whereEqualTo("userId", calleeId);
            query.get().addOnCompleteListener(task -> {

                String documentId = task.getResult().getDocuments().get(0).getId();

                db.collection("UserLiveNotificationSession")
                        .document(documentId)
                        .collection("IsOnCallListener")
                        .limit(1)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            String documentIsOnCallListenerId = task1.getResult().getDocuments().get(0).getId();

                            db.collection("UserLiveNotificationSession")
                                    .document(documentId)
                                    .collection("IsOnCallListener")
                                    .document(documentIsOnCallListenerId)
                                    .update("isOnCall", true,
                                            "callerId", userid);

                        });

            });

        }

    }

    private void initializeCallingTimeout() {
        Intent videoCallActivityIntent = getIntent();
        callDurationTimeoutHandler = new Handler();
        callDurationTimeoutHandler.postDelayed(() -> {
            if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
                stopCallerSound();
                callerRejectCall();
                clearUserCallerState();
                displayCallerRejectCall();
                isCalling = false;
            } else if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
                stopCalleeSound();
                calleeRejectCall();
                clearUserCalleeState();
                displayCalleeEndCall();
                isCalling = false;
            }
            new Handler().postDelayed(() -> finish(), 2000);
        }, CALLING_DURATION_TIMEOUT);
    }

    private void initializeResources() {
        callingConstraintLayout = findViewById(R.id.CallingConstraintLayout);
        onCallConstraintLayout = findViewById(R.id.OnCallConstraintLayout);
        videoCallConstraintLayout = findViewById(R.id.VideoCallConstraintLayout);

        onCalleeSound = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        onCalleeSound.setLooping(true);

        onCallerSound =  MediaPlayer.create(VideoCallActivity.this,R.raw.on_call_sound);
        onCallerSound.setLooping(true);

        callingConstraintLayout.setVisibility(View.GONE);
        onCallConstraintLayout.setVisibility(View.GONE);
        videoCallConstraintLayout.setVisibility(View.GONE);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        isCalling = true;

        initializeResources();
        initializeCallingTimeout();
        initializeCall();

        Intent videoCallActivityIntent = getIntent();
        String userid = videoCallActivityIntent.getStringExtra(USER_ID);

        Log.d("debug", "video activity started user id " + userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(rejectCallBroadcastReceiver);
        Intent videoCallActivityIntent = getIntent();

        Log.d("debug", "is calling " + isCalling);

        if(isCalling && !isPermissionOngoing) {
            if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
                stopCallerSound();
                callerRejectCall();
                clearUserCallerState();
                displayCallerRejectCall();
                isCalling = false;
            } else if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
                stopCalleeSound();
                calleeRejectCall();
                clearUserCalleeState();
                displayCalleeEndCall();
                isCalling = false;
            }
            new Handler().postDelayed(() -> finish(), 2000);
        }
        Log.d("debug", "isCallStarted " + isCallStarted + " role " + getIntent().getStringExtra(USER_CALL_ROLE));
        if(isCallStarted || isCalling) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();

            sharedPreferenceEditor.putString(USER_CALL_ROLE, getIntent().getStringExtra(USER_CALL_ROLE));
            sharedPreferenceEditor.putBoolean("isOnCall", true);
            sharedPreferenceEditor.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(this)
                             .registerReceiver(rejectCallBroadcastReceiver,
                                               new IntentFilter("reject-event"));

        Log.d("debug", "isCallStarted " + isCallStarted + " role " + getIntent().getStringExtra(USER_CALL_ROLE));
        if(isCallStarted || isCalling) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
            sharedPreferenceEditor.clear();
            sharedPreferenceEditor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        if(userSession != null) {
            userSession.onPause();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent videoCallIntent = getIntent();

        if(isCallStarted) {
            if(userSession != null) {
                userSession.disconnect();
                isCallStarted = false;
            }
        } else {
            if(videoCallIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
                stopCallerSound();
                callerRejectCall();
                clearUserCallerState();
                displayCallerRejectCall();
            } else if(videoCallIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
                stopCalleeSound();
                calleeRejectCall();
                clearUserCalleeState();
                displayCalleeRejectCall();
            }
            isCalling = false;
            callDurationTimeoutHandler.removeCallbacksAndMessages(null);
        }

        new Handler().postDelayed(() -> finish(), 2000);
    }



    public void buttonAcceptCall(View v) {
        Intent videoCallActivityIntent = getIntent();

        String userid = videoCallActivityIntent.getStringExtra(USER_ID);
        String callerId = videoCallActivityIntent.getStringExtra("callerId");

        TextView textCallMessage = onCallConstraintLayout.findViewById(R.id.textCallMessage);

        stopCalleeSound();
        textCallMessage.setText("Connecting...");
        v.setVisibility(View.GONE);

        db.collection("UserLiveNotificationSession").whereEqualTo("userId", callerId)
                .get().addOnCompleteListener(task1 -> {
            String callerDocumentId = task1.getResult().getDocuments().get(0).getId();

            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
              .document(callerId)
              .get()
              .addOnSuccessListener(command -> {
                  boolean isOnCall = command.getBoolean("isOnCall");
                  if (isOnCall) {
                      Log.d("debug", "user is on call");
                      db.collection("UserLiveNotificationSession")
                              .document(callerDocumentId)
                              .collection("IsCallAcceptedListener")
                              .limit(1)
                              .get()
                              .addOnCompleteListener(task2 -> {
                                  String callerIsCallAcceptedListenerId = task2.getResult().getDocuments().get(0).getId();

                                  db.collection("UserLiveNotificationSession")
                                          .document(callerDocumentId)
                                          .collection("IsCallAcceptedListener")
                                          .document(callerIsCallAcceptedListenerId)
                                          .update("isCallAccepted", true,
                                                  "calleeId", userid);
                              });
                  } else {

                      Log.d("debug", "user is not on call");
                      isCalling = false;
                      callDurationTimeoutHandler.removeCallbacksAndMessages(null);
                      stopCalleeSound();
                      clearUserCalleeState();
                      displayCalleeRejectCall();
                      new Handler().postDelayed(() -> finish(), 1000);
                  }
              });



        });

        callDurationTimeoutHandler.removeCallbacksAndMessages(null);
    }

    public void buttonCallerRejectCall(View v) {
        isCalling = false;
        callDurationTimeoutHandler.removeCallbacksAndMessages(null);
        stopCallerSound();
        callerRejectCall();
        clearUserCallerState();
        displayCallerRejectCall();
        new Handler().postDelayed(() -> finish(), 1000);
    }

    public void buttonCalleeRejectCall(View v) {
        Intent videoCallActivityIntent = getIntent();
        String callerId = videoCallActivityIntent.getStringExtra("callerId");

        isCalling = false;
        callDurationTimeoutHandler.removeCallbacksAndMessages(null);
        stopCalleeSound();
        displayCalleeRejectCall();

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
          .document(callerId)
          .get()
          .addOnSuccessListener(command -> {
              boolean isOnCall = command.getBoolean("isOnCall");
              if (isOnCall) {
                  calleeRejectCall();
              }
              clearUserCalleeState();
              new Handler().postDelayed(() -> finish(), 1000);
          });
    }

    public void buttonSwitchCamera(View v) {
        if(userPublisher != null) {
            userPublisher.cycleCamera();
        }
    }

    public void buttonTurnOffMicrophone(View v) {
        if(userPublisher != null) {
           toggleMicrophoneOnOrOff();
        }
    }

    public void buttonEndCall(View v) {
        if(userSession != null) {
            userSession.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            publisherViewContainer = findViewById(R.id.publisher_container);
            subscriberViewContainer = findViewById(R.id.subscriber_container);
            buttonTurnOffMicrophone = findViewById(R.id.buttonTurnOffMicrophone);

            // initialize and connect to the session
            userSession = new Session.Builder(this, apiKey, sessionId).build();
            userSession.setSessionListener(this);
            userSession.connect(token);

            Log.d("debug", "PERMISSION GRANTED");

        }
        else {
            Log.d("debug", "REQUIRED PERMISSION");
            isPermissionOngoing = true;
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    // SessionListener methods

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        userPublisher = new Publisher.Builder(this).frameRate(Publisher.CameraCaptureFrameRate.FPS_7)
                                                        .resolution(Publisher.CameraCaptureResolution.MEDIUM).build();
        userPublisher.setPublisherListener(this);
        userPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                                          BaseVideoRenderer.STYLE_VIDEO_FILL);

        publisherViewContainer.addView(userPublisher.getView());
        if (userPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) userPublisher.getView()).setZOrderOnTop(true);
        }
        userSession.publish(userPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
        Intent videoCallIntent = getIntent();
        if(videoCallIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
            clearUserCallerState();
            displayCallerEndCall();
        } else if(videoCallIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
            clearUserCalleeState();
            displayCalleeEndCall();
        }

        isCallStarted = false;
        new Handler().postDelayed(() -> finish(), 2000);
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (userSubscriber == null) {
            userSubscriber = new Subscriber.Builder(this, stream).build();
            userSubscriber.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                                 BaseVideoRenderer.STYLE_VIDEO_FILL);
            userSession.subscribe(userSubscriber);
            subscriberViewContainer.addView(userSubscriber.getView());

            Intent videoCallActivityIntent = getIntent();

            if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
                onCallConstraintLayout.setVisibility(View.GONE);
            } else if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
                callingConstraintLayout.setVisibility(View.GONE);
            }

            isCallStarted = true;
            videoCallConstraintLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (userSubscriber != null) {
            userSession.disconnect();
            new Handler().postDelayed(() -> finish(), 2000);
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        isPermissionOngoing = false;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Intent videoCallActivityIntent = getIntent();


        Log.d("debug", "permission denied");

        if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLER)) {
            callerRejectCall();
            displayCallerRejectCall();
        } else if(videoCallActivityIntent.getStringExtra(USER_CALL_ROLE).equals(USER_CALLEE)) {
            calleeRejectCall();
            displayCalleeRejectCall();
        }

        new Handler().postDelayed(() -> finish(), 2000);
    }
}
