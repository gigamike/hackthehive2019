package com.gigabytes.freebee.login.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OFW {

    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    @SerializedName("organization")
    private String organization;

    @SerializedName("distance")
    private double distance;

    @SerializedName("is_online")
    private boolean isOnline;

    @SerializedName("profile_pic")
    private String profilePic;

    @SerializedName("mobile_no")
    private String mobileNumber;

    public String getId() {
        return id;
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

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getOrganization() {
        return organization;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getMobileNumber() { return mobileNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OFW ofw = (OFW) o;
        return Double.compare(ofw.distance, distance) == 0 &&
                isOnline == ofw.isOnline &&
                Objects.equals(id, ofw.id) &&
                Objects.equals(firstName, ofw.firstName) &&
                Objects.equals(middleName, ofw.middleName) &&
                Objects.equals(lastName, ofw.lastName) &&
                Objects.equals(city, ofw.city) &&
                Objects.equals(country, ofw.country) &&
                Objects.equals(organization, ofw.organization) &&
                Objects.equals(profilePic, ofw.profilePic) &&
                Objects.equals(mobileNumber, ofw.mobileNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, middleName, lastName, city, country, organization, distance, isOnline, profilePic, mobileNumber);
    }

    @Override
    public String toString() {
        return "OFW{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", organization='" + organization + '\'' +
                ", distance=" + distance +
                ", isOnline=" + isOnline +
                ", profilePic='" + profilePic + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
