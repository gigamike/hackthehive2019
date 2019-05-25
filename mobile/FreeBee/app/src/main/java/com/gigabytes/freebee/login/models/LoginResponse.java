package com.gigabytes.freebee.login.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("is_logged")
    boolean isLogged;

    @SerializedName("user_id")
    String userID;

    String role;

    public boolean isLogged() {
        return isLogged;
    }

    public String getUserID() {
        return userID;
    }

    public String getRole() {
        return role;
    }
}
