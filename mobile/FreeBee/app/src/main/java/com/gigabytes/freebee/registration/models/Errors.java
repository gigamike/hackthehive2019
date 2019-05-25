package com.gigabytes.freebee.registration.models;

import com.google.gson.annotations.SerializedName;

public class Errors {
    private String email,password,country,city,organization;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("mobile_no")
    private String mobileNumber;

    @SerializedName("profile_picture")
    private String profilePicture;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }


    public String getOrganization() {
        return organization;
    }

    public String getFirstName() {
        return firstName;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    @Override
    public String toString() {
        return "Errors{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", organization='" + organization + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
