package com.gigabytes.freebee.login.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Volunteers {

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

    @SerializedName("isOnline")
    private boolean isOnline;

    @SerializedName("profile_pic")
    private String profilePic;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteers that = (Volunteers) o;
        return Double.compare(that.distance, distance) == 0 &&
                isOnline == that.isOnline &&
                Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country) &&
                Objects.equals(organization, that.organization) &&
                Objects.equals(profilePic, that.profilePic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, city, country, organization, distance, isOnline, profilePic);
    }

    @Override
    public String toString() {
        return "Volunteers{" +
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
                '}';
    }
}
