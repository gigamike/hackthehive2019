package com.gigabytes.freebee.registration.models;

public class OrganizationsResponse {
    String id;
    String organization;

    public String getId() {
        return id;
    }

    public String getOrganization() {
        return organization;
    }

    @Override
    public String toString() {
        return "OrganizationsResponse{" +
                "id='" + id + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
