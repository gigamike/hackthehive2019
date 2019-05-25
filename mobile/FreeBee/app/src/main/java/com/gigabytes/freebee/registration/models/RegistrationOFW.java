package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class RegistrationOFW {
    String email,password;

    @SerializedName("country_id")
    int country;

    String city;

    @SerializedName("first_name")
    String firstname;

    @SerializedName("middle_name")
    String middleName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("mobile_no")
    String mobileNumber;

    public RegistrationOFW(String email, String password, int country, String city, String firstname, String middleName, String lastName, String mobileNumber) {
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.firstname = firstname;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
