package com.gigabytes.freebee.registration.models;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface RegistrationAPI {
    @GET("countries")
    Call<List<CountriesResponse>> getAllCountries();

    @GET("organizations")
    Call<List<OrganizationsResponse>> getAllOrganizations();

    @Multipart
    @POST("ofw-registration")
    Call<OFWRegistrationResponse> registerOFW(
            @PartMap Map<String, RequestBody> params);
}
