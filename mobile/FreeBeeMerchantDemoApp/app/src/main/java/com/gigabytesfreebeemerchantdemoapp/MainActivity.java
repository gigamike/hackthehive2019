package com.gigabytesfreebeemerchantdemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView transactionCodeTextView;
    private TextView transactionCodeValueTextview;

    private TextView userIdTextView;
    private TextView userIdValueTextView;

    private TextView statusTextView;
    private TextView statusValueTextView;

    private TextView creditsBuyedTextView;
    private TextView creditsBuyedValueTextView;

    private TextView transactionDateTimeTextView;
    private TextView transactionDateTimeValueTextView;

    private ProgressBar getTransactionCodeProgresBar;

    private String transactionCode;

    private Button btnScanQRCode;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionCodeTextView = findViewById(R.id.transactionCodeTextView);
        transactionCodeValueTextview = findViewById(R.id.transactionCodeValueTextview);

        userIdTextView = findViewById(R.id.userIdTextView);
        userIdValueTextView = findViewById(R.id.userIdTextViewValue);

        statusTextView = findViewById(R.id.statusTextView);
        statusValueTextView = findViewById(R.id.statusTextViewValue);

        creditsBuyedTextView = findViewById(R.id.creditsBuyedTextView);
        creditsBuyedValueTextView = findViewById(R.id.creditsBuyedValueTextView);

        transactionDateTimeTextView = findViewById(R.id.transactionDateTimeTextView);
        transactionDateTimeValueTextView = findViewById(R.id.transactionDateTimeValueTextView);

        getTransactionCodeProgresBar = findViewById(R.id.getTransactionCodeProgresBar);

        btnScanQRCode = findViewById(R.id.btn);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://freebee-payment-notify-server.herokuapp.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                FreeBeePaymentTransactionAPI freeBeePaymentTransactionAPI = retrofit.create(FreeBeePaymentTransactionAPI.class);

                getTransactionCodeProgresBar.setVisibility(View.VISIBLE);

                freeBeePaymentTransactionAPI.completePaymentTransaction(transactionCode).enqueue(new Callback<CompletePaymentTransactionResponse>() {
                    @Override
                    public void onResponse(Call<CompletePaymentTransactionResponse> call, Response<CompletePaymentTransactionResponse> response) {
                        if (response.body().getResponseStatus().equals("SUCCESS")) {
                            getTransactionCodeProgresBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "PAYMENT SUCCESS", Toast.LENGTH_LONG).show();
                            retrievePaymentTransactionDetailsPaid(transactionCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<CompletePaymentTransactionResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        });

        btnScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private  void retrievePaymentTransactionDetailsPaid(String transactionCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://freebee-payment-notify-server.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FreeBeePaymentTransactionAPI freeBeePaymentTransactionAPI = retrofit.create(FreeBeePaymentTransactionAPI.class);

        transactionCodeTextView.setVisibility(View.INVISIBLE);
        transactionCodeValueTextview.setVisibility(View.INVISIBLE);

        userIdTextView.setVisibility(View.INVISIBLE);
        userIdValueTextView.setVisibility(View.INVISIBLE);

        statusTextView.setVisibility(View.INVISIBLE);
        statusValueTextView.setVisibility(View.INVISIBLE);

        creditsBuyedTextView.setVisibility(View.INVISIBLE);
        creditsBuyedValueTextView.setVisibility(View.INVISIBLE);

        transactionDateTimeTextView.setVisibility(View.INVISIBLE);
        transactionDateTimeValueTextView.setVisibility(View.INVISIBLE);

        getTransactionCodeProgresBar.setVisibility(View.VISIBLE);

        freeBeePaymentTransactionAPI.getPaymentTransaction(transactionCode).enqueue(new Callback<GetPaymentTransactionResponse>() {
            @Override
            public void onResponse(Call<GetPaymentTransactionResponse> call, Response<GetPaymentTransactionResponse> response) {

                getTransactionCodeProgresBar.setVisibility(View.GONE);

                GetPaymentTransactionResponse getPaymentTransactionResponse = response.body();

                transactionCodeValueTextview.setText(getPaymentTransactionResponse.getTransactionCode());
                userIdValueTextView.setText(getPaymentTransactionResponse.getUserId());
                statusValueTextView.setText(getPaymentTransactionResponse.getStatus());
                creditsBuyedValueTextView.setText(Integer.toString(getPaymentTransactionResponse.getCreditsBuyed()));
                transactionDateTimeValueTextView.setText(getPaymentTransactionResponse.getTransactionDateTime());


                transactionCodeTextView.setVisibility(View.VISIBLE);
                transactionCodeValueTextview.setVisibility(View.VISIBLE);

                userIdTextView.setVisibility(View.VISIBLE);
                userIdValueTextView.setVisibility(View.VISIBLE);

                statusTextView.setVisibility(View.VISIBLE);
                statusValueTextView.setVisibility(View.VISIBLE);

                creditsBuyedTextView.setVisibility(View.VISIBLE);
                creditsBuyedValueTextView.setVisibility(View.VISIBLE);

                transactionDateTimeTextView.setVisibility(View.VISIBLE);
                transactionDateTimeValueTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<GetPaymentTransactionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void retrievePaymentTransactionDetails(String transactionCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://freebee-payment-notify-server.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FreeBeePaymentTransactionAPI freeBeePaymentTransactionAPI = retrofit.create(FreeBeePaymentTransactionAPI.class);

        transactionCodeTextView.setVisibility(View.INVISIBLE);
        transactionCodeValueTextview.setVisibility(View.INVISIBLE);

        userIdTextView.setVisibility(View.INVISIBLE);
        userIdValueTextView.setVisibility(View.INVISIBLE);

        statusTextView.setVisibility(View.INVISIBLE);
        statusValueTextView.setVisibility(View.INVISIBLE);

        creditsBuyedTextView.setVisibility(View.INVISIBLE);
        creditsBuyedValueTextView.setVisibility(View.INVISIBLE);

        transactionDateTimeTextView.setVisibility(View.INVISIBLE);
        transactionDateTimeValueTextView.setVisibility(View.INVISIBLE);

        getTransactionCodeProgresBar.setVisibility(View.VISIBLE);

        freeBeePaymentTransactionAPI.getPaymentTransaction(transactionCode).enqueue(new Callback<GetPaymentTransactionResponse>() {
            @Override
            public void onResponse(Call<GetPaymentTransactionResponse> call, Response<GetPaymentTransactionResponse> response) {

                getTransactionCodeProgresBar.setVisibility(View.GONE);

                GetPaymentTransactionResponse getPaymentTransactionResponse = response.body();

                transactionCodeValueTextview.setText(getPaymentTransactionResponse.getTransactionCode());
                userIdValueTextView.setText(getPaymentTransactionResponse.getUserId());
                statusValueTextView.setText(getPaymentTransactionResponse.getStatus());
                creditsBuyedValueTextView.setText(Integer.toString(getPaymentTransactionResponse.getCreditsBuyed()));
                transactionDateTimeValueTextView.setText(getPaymentTransactionResponse.getTransactionDateTime());

                btnPay.setVisibility(View.VISIBLE);

                transactionCodeTextView.setVisibility(View.VISIBLE);
                transactionCodeValueTextview.setVisibility(View.VISIBLE);

                userIdTextView.setVisibility(View.VISIBLE);
                userIdValueTextView.setVisibility(View.VISIBLE);

                statusTextView.setVisibility(View.VISIBLE);
                statusValueTextView.setVisibility(View.VISIBLE);

                creditsBuyedTextView.setVisibility(View.VISIBLE);
                creditsBuyedValueTextView.setVisibility(View.VISIBLE);

                transactionDateTimeTextView.setVisibility(View.VISIBLE);
                transactionDateTimeValueTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<GetPaymentTransactionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == 100) {
            final String transactionCode = data.getStringExtra("transactionCode");
            this.transactionCode = transactionCode;
            retrievePaymentTransactionDetails(transactionCode);
        }

    }
}
