package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class CountriesResponse {

    String id;

    @SerializedName("country_name")
    private
    String countryName;

    public String getId() {
        return id;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return "CountriesResponse{" +
                "id='" + id + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
