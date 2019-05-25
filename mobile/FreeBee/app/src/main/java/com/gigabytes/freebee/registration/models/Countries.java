package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class Countries {
    String id;

    @SerializedName("is_logged")
    String countryName;

    public Countries(String id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
