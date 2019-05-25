package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class CountriesResponse {

    String id;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("country_name")
    private String countryName;

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
        return "CountriesResponse{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
