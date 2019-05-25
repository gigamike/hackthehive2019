package com.gigabytes.freebee.homescreen.views.model;

import java.util.Objects;

public class ContactsDO {
    private String id,firstname,middlename,lastname,organization,profilePic,country,city,mobileNumber;

    private boolean isOnline;
    private double distance;

    public ContactsDO(String id, String firstname, String middlename, String lastname, String organization, String profilePic, String country, String city, boolean isOnline, double distance, String mobileNumber) {
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.organization = organization;
        this.profilePic = profilePic;
        this.country = country;
        this.city = city;
        this.isOnline = isOnline;
        this.distance = distance;
        this.mobileNumber = mobileNumber;
    }

    public String getId() {
        return id;
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

    public String getOrganization() {
        return organization;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public double getDistance() {
        return distance;
    }

    public String getMobileNumber() { return mobileNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactsDO that = (ContactsDO) o;
        return isOnline == that.isOnline &&
                Double.compare(that.distance, distance) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(middlename, that.middlename) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(organization, that.organization) &&
                Objects.equals(profilePic, that.profilePic) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(mobileNumber, that.mobileNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstname, middlename, lastname, organization, profilePic, country, city, mobileNumber, isOnline, distance);
    }

    @Override
    public String toString() {
        return "ContactsDO{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", organization='" + organization + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", isOnline=" + isOnline +
                ", distance=" + distance +
                '}';
    }
}
