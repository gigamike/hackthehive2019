package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class OFWRegistrationResponse {
    @SerializedName("user_id")
    String userID;

    Errors error;

    public String getUserID() {
        return userID;
    }

    public Errors getError() {
        return error;
    }

    @Override
    public String toString() {
        return "OFWRegistrationResponse{" +
                "userID='" + userID + '\'' +
                ", error=" + error +
                '}';
    }
}
