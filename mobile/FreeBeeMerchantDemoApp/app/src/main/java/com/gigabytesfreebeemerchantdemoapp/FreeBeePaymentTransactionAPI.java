package com.gigabytesfreebeemerchantdemoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FreeBeePaymentTransactionAPI {

    @GET("transaction/{transactionCode}")
    Call<GetPaymentTransactionResponse> getPaymentTransaction(@Path("transactionCode") String transactionCode);

    @GET("transaction/complete/{transactionCode}")
    Call<CompletePaymentTransactionResponse> completePaymentTransaction(@Path("transactionCode") String transactionCode);


}

