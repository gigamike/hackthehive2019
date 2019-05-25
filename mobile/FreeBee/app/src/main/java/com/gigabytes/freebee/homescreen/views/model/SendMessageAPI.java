package com.gigabytes.freebee.homescreen.views.model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SendMessageAPI {
    @POST("message")
    @FormUrlEncoded
    Call<Void> sendMessage(@Field("sender_user_id") String sendUserId,
                           @Field("recipient_user_id") String recipientUserId,
                           @Field("message") String message);
}
