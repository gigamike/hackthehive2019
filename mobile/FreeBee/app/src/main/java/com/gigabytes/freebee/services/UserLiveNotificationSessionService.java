package com.gigabytes.freebee.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gigabytes.freebee.videocall.views.activities.VideoCallActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

public class UserLiveNotificationSessionService extends Service {

    private final String USER_LIVE_NOTIFICATION_SESSION_COLLECTION = "UserLiveNotificationSession";
    private final String IS_ON_CALL_LISTENER = "IsOnCallListener";
    private final String IS_CALL_REJECTED_LISTENER = "IsCallRejectedListener";
    private final String USER_ID = "userId";

    private boolean isListenersInitialized = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("debug", "UserLiveNotificationSessionService started");
        startUserLiveNotificationSession(intent.getStringExtra(USER_ID));

        return START_NOT_STICKY;
    }


    private void startUserLiveNotificationSession(String userId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .get().addOnCompleteListener(task -> {

                    String documentId = task.getResult().getDocuments().get(0).getId();

                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                            .document(documentId)
                            .collection(IS_CALL_REJECTED_LISTENER)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task2 -> {

                                String isCallRejectedListenerId = task2.getResult().getDocuments().get(0).getId();

                                db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                        .document(documentId)
                                        .collection(IS_CALL_REJECTED_LISTENER)
                                        .document(isCallRejectedListenerId)
                                        .update("isCallRejected", false,
                                                "userCallRole", "").addOnCompleteListener(command -> {

                                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                            .document(documentId)
                                            .collection(IS_CALL_REJECTED_LISTENER)
                                            .addSnapshotListener((queryDocumentSnapshots1, e1) -> {

                                                Log.d("debug", IS_CALL_REJECTED_LISTENER + " invoked on service");

                                                List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots1.getDocuments();
                                                DocumentSnapshot documentSnapshot = documentSnapshotList.get(0);

                                                if (!isListenersInitialized) {

                                                    db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                                            .document(documentId)
                                                            .collection(IS_ON_CALL_LISTENER)
                                                            .addSnapshotListener((queryDocumentSnapshots, e) -> {
                                                                        Log.d("debug", "IS_ON_CALL_LISTENER invoked on service");

                                                                        if (queryDocumentSnapshots.getDocuments().get(0).getBoolean("isOnCall") != null &&
                                                                                queryDocumentSnapshots.getDocuments().get(0).getBoolean("isOnCall")) {

                                                                            String callerId = queryDocumentSnapshots.getDocuments().get(0).getString("callerId");
                                                                            boolean isVoiceCall = queryDocumentSnapshots.getDocuments().get(0).getBoolean("isVoiceCall");

                                                                            db.collection(USER_LIVE_NOTIFICATION_SESSION_COLLECTION)
                                                                                    .whereEqualTo(USER_ID, callerId)
                                                                                    .get().addOnCompleteListener(task1 -> {

                                                                                Intent intent = new Intent(UserLiveNotificationSessionService.this, VideoCallActivity.class);

                                                                                String fullName = task1.getResult().getDocuments().get(0).getString("fullName");
                                                                                String userPictureURL = task1.getResult().getDocuments().get(0).getString("userPictureURL");

                                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                                                intent.putExtra(VideoCallActivity.USER_CALL_ROLE, VideoCallActivity.USER_CALLEE);
                                                                                intent.putExtra(USER_ID, userId);
                                                                                intent.putExtra("callerId", queryDocumentSnapshots.getDocuments().get(0).getString("callerId"));
                                                                                intent.putExtra("userPictureURL", userPictureURL);
                                                                                intent.putExtra("fullName", fullName);

                                                                                if(isVoiceCall) {
                                                                                    intent.putExtra("isVoiceCall", true);
                                                                                }

                                                                                startActivity(intent);

                                                                            });

                                                                        }
                                                                    }
                                                            );

                                                    isListenersInitialized = true;


                                                } else {

                                                    if (documentSnapshot.getBoolean("isCallRejected") != null &&
                                                        documentSnapshot.getBoolean("isCallRejected")) {

                                                        Log.d("debug","call rejected in service");

                                                        Intent sendRejectCallMessage = new Intent("reject-event");
                                                        sendRejectCallMessage.putExtra(VideoCallActivity.USER_CALL_ROLE, documentSnapshot.getString("userCallRole"));
                                                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sendRejectCallMessage);

                                                    }
                                                }


                                            });
                                });

                            });

                }
        );
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
