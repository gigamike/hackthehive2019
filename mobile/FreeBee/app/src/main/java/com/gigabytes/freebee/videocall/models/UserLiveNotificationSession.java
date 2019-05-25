package com.gigabytes.freebee.videocall.models;

public class UserLiveNotificationSession {

    private String userId;
    private String callerId;

    private String sessionId;
    private String sessionRoomName;
    private String tokenId;

    private boolean isCallAccepted;
    private boolean isOnCall;
    private boolean isLogin;

    public UserLiveNotificationSession() {}

    public UserLiveNotificationSession(String userId, String callerId, String sessionId, String sessionRoomName, String tokenId, boolean isCallAccepted, boolean isOnCall, boolean isLogin) {
        this.userId = userId;
        this.callerId = callerId;
        this.sessionId = sessionId;
        this.sessionRoomName = sessionRoomName;
        this.tokenId = tokenId;
        this.isCallAccepted = isCallAccepted;
        this.isOnCall = isOnCall;
        this.isLogin = isLogin;
    }

    public String getUserId() {
        return userId;
    }

    public String getCallerId() {
        return callerId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionRoomName() {
        return sessionRoomName;
    }

    public String getTokenId() {
        return tokenId;
    }

    public boolean isCallAccepted() {
        return isCallAccepted;
    }

    public boolean isOnCall() {
        return isOnCall;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
