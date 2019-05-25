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
            @Query("organization_id") Integer organization,
            @Query("first_name_keyword") String firstName,
            @Query("last_name_keyword") String lastname);

    @GET("ofws")
    Call<OFWResponse> getAllOFW();

    @GET("ofws")
    Call<OFWResponse> getSearchedOFW(
            @Query("first_name_keyword") String firstName,
            @Query("last_name_keyword") String lastname);

    @GET("user-update")
    Call<Void> updateUserLastLogin(@Query("user_id") Integer userId);
}
