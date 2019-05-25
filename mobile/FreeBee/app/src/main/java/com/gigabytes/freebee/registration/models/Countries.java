package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class Countries {
    String id;

    @SerializedName("country_code")
    String countryCode;

    @SerializedName("country_name")
    String countryName;

    public Countries(String id, String countryCode, String countryName) {
        this.id = id;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
