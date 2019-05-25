package com.gigabytes.freebee.credits.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FreeBeePaymentTransactionAPI {

    @POST("transaction")
    Call<CreatePaymentTransactionResponse> createPaymentTransaction(@Body CreatePaymentTransactionRequest createPaymentTransactionRequest);

    @POST("transaction/{transactionCode}")
    Call<CreatePaymentTransactionResponse> createPaymentTransactionCodeWithTransactionCode(@Path("transactionCode") String transactionCode,
                                                                                           @Body CreatePaymentTransactionRequest createPaymentTransactionRequest);

}
