package com.gigabytes.freebee.registration.models;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegistrationAPI {
    @GET("countries")
    Call<List<CountriesResponse>> getAllCountries();

    @GET("organizations")
    Call<List<OrganizationsResponse>> getAllOrganizations();

    @POST("ofw-registration")
    Call<OFWRegistrationResponse> registerOFW(@Body RegistrationOFW registrationOfw);

    @POST("volunteer-registration")
    Call<VolunteerRegistrationResponse> registerVolunteer(@Body RegistrationVolunteer registrationVolunteer);
}
