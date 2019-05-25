package com.gigabytes.freebee.registration.models;

public class Organizations {
    int orgId;
    String organizationName;

    public Organizations(int orgId, String organizationName) {
        this.orgId = orgId;
        this.organizationName = organizationName;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public String toString() {
        return organizationName;
    }
}
