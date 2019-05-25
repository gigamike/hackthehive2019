package com.gigabytes.freebee.videocall.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FreeBeeOpenTokAPI {
    @GET("room/{sessionRoomName}")
    Call<OpenTokSessionResponse> createSession(@Path("sessionRoomName") String sessionRoomName);
}
