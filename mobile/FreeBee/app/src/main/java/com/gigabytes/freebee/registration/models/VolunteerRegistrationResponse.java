package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class VolunteerRegistrationResponse {
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
        return "VolunteerRegistrationResponse{" +
                "userID='" + userID + '\'' +
                ", error=" + error +
                '}';
    }
}
