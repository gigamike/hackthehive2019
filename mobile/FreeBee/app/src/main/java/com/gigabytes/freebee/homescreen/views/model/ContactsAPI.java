package com.gigabytes.freebee.homescreen.views.model;

import com.gigabytes.freebee.login.models.OFWResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContactsAPI {
    @GET("volunteers")
    Call<VolunteerResponse> getAllVolunteer();

    @GET("volunteers")
    Call<VolunteerResponse> getSearchedVolunteer(
            @Query("organization_id") int organization,
            @Query("country_code") String countryCode,
            @Query("city_keyword") String city);

    @GET("volunteers")
    Call<VolunteerResponse> searchVolunteerWithoutOrganization(
            @Query("country_code") String countryCode,
            @Query("city_keyword") String city);

    @GET("ofws")
    Call<OFWResponse> getAllOFW();

    @GET("ofws")
    Call<OFWResponse> getSearchedOFW(
            @Query("country_code") String countryCode,
            @Query("city_keyword") String city);

    @GET("user-update")
    Call<Void> updateUserLastLogin(@Query("user_id") Integer userId);
}
