package com.gigabytes.freebee.homescreen.views.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Volunteer {
    private String id,organization,country,city;

    private double distance;

    @SerializedName("first_name")
    private String firstname;

    @SerializedName("middle_name")
    private String middlename;

    @SerializedName("last_name")
    private String lastname;

    @SerializedName("profile_pic")
    private String profilePic;

    @SerializedName("is_online")
    private boolean isOnline;

    @SerializedName("mobile_no")
    private String mobileNumber;

    public void setId(String id) {
        this.id = id;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getId() {
        return id;
    }

    public String getOrganization() {
        return organization;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public double getDistance() {
        return distance;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getMobileNumber() { return mobileNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Double.compare(volunteer.distance, distance) == 0 &&
                isOnline == volunteer.isOnline &&
                Objects.equals(id, volunteer.id) &&
                Objects.equals(organization, volunteer.organization) &&
                Objects.equals(country, volunteer.country) &&
                Objects.equals(city, volunteer.city) &&
                Objects.equals(firstname, volunteer.firstname) &&
                Objects.equals(middlename, volunteer.middlename) &&
                Objects.equals(lastname, volunteer.lastname) &&
                Objects.equals(profilePic, volunteer.profilePic) &&
                Objects.equals(mobileNumber, volunteer.mobileNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, organization, country, city, distance, firstname, middlename, lastname, profilePic, isOnline, mobileNumber);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id='" + id + '\'' +
                ", organization='" + organization + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", distance=" + distance +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", isOnline=" + isOnline +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
