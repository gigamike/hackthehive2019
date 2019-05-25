package com.gigabytes.freebee.login.models;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginAPI {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @GET("volunteers")
    Call<VolunteersResponse> getVolunteerByUserId(@Query("user_id") String userId);

    @GET("ofws")
    Call<OFWResponse> getOFWByUserId(@Query("user_id") String userId);

}
