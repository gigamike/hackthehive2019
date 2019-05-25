package com.gigabytes.freebee.homescreen.views.model;

public class ContactsDO {
    private String id,firstname,middlename,lastname,organization,profilePic,country,city;

    private boolean isOnline;
    private double distance;

    public ContactsDO(String id, String firstname, String middlename, String lastname, String organization, String profilePic, String country, String city, boolean isOnline, double distance) {
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
                ", isOnline=" + isOnline +
                ", distance=" + distance +
                '}';
    }
}
