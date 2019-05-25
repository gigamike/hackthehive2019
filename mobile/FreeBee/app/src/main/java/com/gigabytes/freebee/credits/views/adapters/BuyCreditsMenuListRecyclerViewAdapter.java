package com.gigabytes.freebee.credits.views.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitpay.sdk.android.BitPayAndroid;
import com.bitpay.sdk.android.interfaces.BitpayPromiseCallback;
import com.bitpay.sdk.android.interfaces.InvoicePromiseCallback;
import com.bitpay.sdk.controller.BitPayException;
import com.bitpay.sdk.model.Invoice;
import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.R;
import com.gigabytes.freebee.credits.models.CreatePaymentTransactionRequest;
import com.gigabytes.freebee.credits.models.CreatePaymentTransactionResponse;
import com.gigabytes.freebee.credits.models.FreeBeePaymentTransactionAPI;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyCreditsMenuListRecyclerViewAdapter extends RecyclerView.Adapter<BuyCreditsMenuListRecyclerViewAdapter.BuyCreditsMenuListViewHolder>{


    private enum PaymentType {
        BITCOIN,
        CASH
    }

    private List<Integer> menuIndex;
    private Context context;
    private Activity activity;
    private Dialog buyCreditsDialog;
    private PaymentType paymentType;

    public static class BuyCreditsMenuListViewHolder extends RecyclerView.ViewHolder {
        public BuyCreditsMenuListViewHolder(View view) {
            super(view);
        }
    }


    public void startNewActivity(Context context,
                                 String packageName,
                                 String bitpayPaymentUrl) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(bitpayPaymentUrl));
        }
        context.startActivity(intent);
    }

    private Bitmap createQRCodeFromValue(String value, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(value, BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void payCashViaQRCode(String userId, int creditsBuyed) {

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.cash_qr_payment);
        dialog.setTitle("Cash qr payment");
        dialog.setCancelable(true);

        ImageView transactionCodeQRCodeImageView = dialog.findViewById(R.id.transactionCodeQRCodeImageView);
        ProgressBar generateQRCodeProgressbar = dialog.findViewById(R.id.generateQRCodeProgressbar);

        Button buttonCancelPaymentTransaction = dialog.findViewById(R.id.buttonCancelPaymentTransaction);

        buttonCancelPaymentTransaction.setOnClickListener(v -> dialog.dismiss());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://freebee-payment-notify-server.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FreeBeePaymentTransactionAPI freeBeePaymentTransactionAPI = retrofit.create(FreeBeePaymentTransactionAPI.class);

        CreatePaymentTransactionRequest createPaymentTransactionRequest
                = new CreatePaymentTransactionRequest(userId, creditsBuyed);

        generateQRCodeProgressbar.setVisibility(View.VISIBLE);

        freeBeePaymentTransactionAPI.createPaymentTransaction(createPaymentTransactionRequest).enqueue(new Callback<CreatePaymentTransactionResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentTransactionResponse> call, Response<CreatePaymentTransactionResponse> response) {

                generateQRCodeProgressbar.setVisibility(View.GONE);

                CreatePaymentTransactionResponse createPaymentTransactionResponse = response.body();
                transactionCodeQRCodeImageView.setImageBitmap(createQRCodeFromValue(createPaymentTransactionResponse.getTransactionCode(),
                                                                                    700, 700));
                Log.d("debug", createPaymentTransactionResponse.toString());
            }

            @Override
            public void onFailure(Call<CreatePaymentTransactionResponse> call, Throwable t) {
                generateQRCodeProgressbar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

        dialog.show();

    }

    private void payBitcoin(int creditsBuyed, double price) {

        FreeBeeApplication freeBeeApplication = (FreeBeeApplication) context.getApplicationContext();

        ProgressDialog loadingBitpayPaymentTransaction = new ProgressDialog(activity);;

        loadingBitpayPaymentTransaction.setIndeterminate(true);
        loadingBitpayPaymentTransaction.setMessage("Please wait...");
        loadingBitpayPaymentTransaction.setCancelable(false);
        loadingBitpayPaymentTransaction.show();

        String clientToken = "HuWx635UQwzZWwcuUaVya6UurNmR3PbZUa1v2UC9DRGE";
        BitPayAndroid.withToken(clientToken, "https://test.bitpay.com/").then(new BitpayPromiseCallback() {

            public void onSuccess(BitPayAndroid bitpay) {

                Invoice creditInvoice = new Invoice();

                try {
                    creditInvoice.setPrice(price);
                    creditInvoice.setCurrency("PHP");
                    creditInvoice.setItemDesc("Buying credits: " + creditsBuyed);
                    creditInvoice.setNotificationURL("https://freebee-payment-notify-server.herokuapp.com/bitpayInvoiceWebhookNotify");
                    creditInvoice.setTransactionSpeed("high");
                    creditInvoice.setRedirectURL("https://freebee-payment-notify-server.herokuapp.com/bitpayCompletePaymentRedirect");
                } catch (BitPayException e) {
                    e.printStackTrace();
                }

                bitpay.createNewInvoice(creditInvoice).then(new InvoicePromiseCallback() {

                    public void onSuccess(Invoice invoice) {
                        Log.d("debug", " invoice url " + invoice.getUrl() + " invoice id " + invoice.getId() );

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://freebee-payment-notify-server.herokuapp.com/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        FreeBeePaymentTransactionAPI freeBeePaymentTransactionAPI = retrofit.create(FreeBeePaymentTransactionAPI.class);

                        CreatePaymentTransactionRequest createPaymentTransactionRequest
                                = new CreatePaymentTransactionRequest(freeBeeApplication.userId,
                                                                      creditsBuyed);

                        freeBeePaymentTransactionAPI.createPaymentTransactionCodeWithTransactionCode(invoice.getId(), createPaymentTransactionRequest).enqueue(new Callback<CreatePaymentTransactionResponse>() {
                            @Override
                            public void onResponse(Call<CreatePaymentTransactionResponse> call, Response<CreatePaymentTransactionResponse> response) {

                                Log.d("debug", "CreatePaymentTransactionResponse " + response.body());

                                loadingBitpayPaymentTransaction.dismiss();

                                Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.bitpay_payment_gateway);
                                dialog.setTitle("Bitpay payment");
                                dialog.setCancelable(true);

                                Button buttonCancelPaymentTransaction = dialog.findViewById(R.id.buttonCancelPaymentTransaction);

                                buttonCancelPaymentTransaction.setOnClickListener(v -> {
                                    dialog.dismiss();
                                });

                                ProgressBar bitpayWebViewClientProgressBar = dialog.findViewById(R.id.bitpayWebViewClientProgressBar);

                                bitpayWebViewClientProgressBar.setMax(100);
                                bitpayWebViewClientProgressBar.setProgress(1);

                                WebView bitpayWebViewClient = dialog.findViewById(R.id.bitpayWebViewClient);

                                bitpayWebViewClient.loadUrl(invoice.getUrl());

                                WebSettings webSettings = bitpayWebViewClient.getSettings();

                                webSettings.setJavaScriptEnabled(true);

                                bitpayWebViewClient.setWebChromeClient(new WebChromeClient() {
                                    @Override
                                    public void onProgressChanged(WebView view, int newProgress) {
                                        bitpayWebViewClientProgressBar.setProgress(newProgress);
                                    }
                                });

                                bitpayWebViewClient.setWebViewClient(new WebViewClient() {

                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        Log.d("debug", "url  " + url);
                                        if( URLUtil.isNetworkUrl(url) ) {
                                            return false;
                                        }
                                        if(url.startsWith("bitcoin:")) {
                                            startNewActivity(context, "com.bitpay.wallet", url);
                                        } else if(url.startsWith("freebee:")) {
                                            String dataString = url.substring(url.indexOf(":") + 1);
                                            String[] dataValue = dataString.split(":");
                                            if (dataValue[0].equals("complete") && dataValue[1].equals("true")) {
                                                dialog.dismiss();
                                                buyCreditsDialog.dismiss();
                                            }
                                        }
                                        return true;
                                    }

                                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                        bitpayWebViewClientProgressBar.setVisibility(View.VISIBLE);
                                    }

                                    public void onPageFinished(WebView view, String url) {
                                        bitpayWebViewClientProgressBar.setVisibility(View.GONE);
                                    }

                                });

                                dialog.show();


                            }

                            @Override
                            public void onFailure(Call<CreatePaymentTransactionResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }
                    public void onError(BitPayException e) {
                        e.printStackTrace();
                    }
                });
            }

            public void onError(BitPayException e) {
                e.printStackTrace();
            }
        });


    }

    public BuyCreditsMenuListRecyclerViewAdapter(Context context, Activity activity) {
        FreeBeeApplication freeBeeApplication = (FreeBeeApplication)context.getApplicationContext();

        menuIndex = new ArrayList<>();
        menuIndex.add(1);
        menuIndex.add(2);

        buyCreditsDialog = new Dialog(activity);

        buyCreditsDialog.setContentView(R.layout.buy_credits_dialog);
        buyCreditsDialog.setTitle("Buy Credits");
        buyCreditsDialog.setCancelable(true);

        buyCreditsDialog.findViewById(R.id.buttonCancelBuyCreditsDialog).setOnClickListener(v -> buyCreditsDialog.dismiss());

        buyCreditsDialog.findViewById(R.id.buttonBuyCredits1).setOnClickListener(v -> {
            if(paymentType == PaymentType.BITCOIN) {
                payBitcoin(200, 50.00);
            } else if(paymentType == PaymentType.CASH) {
                payCashViaQRCode(freeBeeApplication.userId, 100);
            }

        });

        buyCreditsDialog.findViewById(R.id.buttonBuyCredits2).setOnClickListener(v -> {
            if(paymentType == PaymentType.BITCOIN) {
                payBitcoin(500, 200.00);
            } else if(paymentType == PaymentType.CASH) {
                payCashViaQRCode(freeBeeApplication.userId, 500);
            }
        });

        buyCreditsDialog.findViewById(R.id.buttonBuyCredits3).setOnClickListener(v -> {
            if(paymentType == PaymentType.BITCOIN) {
                payBitcoin(1000, 500.00);
            } else if(paymentType == PaymentType.CASH) {
                payCashViaQRCode(freeBeeApplication.userId, 1000);
            }
        });

        this.context = context;
        this.activity = activity;
    }

    @Override
    public BuyCreditsMenuListRecyclerViewAdapter.BuyCreditsMenuListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                 int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_list_item_menu, parent, false);
        return new BuyCreditsMenuListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(BuyCreditsMenuListViewHolder holder, int position) {
        View itemView = holder.itemView;

        TextView buyMenuTextView = itemView.findViewById(R.id.buyMenuTextView);
        CircleImageView buyLogoItemMenu = itemView.findViewById(R.id.buyLogoItemMenu);

        switch (menuIndex.get(position)) {
            case 1:
                buyLogoItemMenu.setImageDrawable(context.getDrawable(R.drawable.bitcoin_logo));
                buyMenuTextView.setText("Buy Credits using Bitcoin");

                itemView.setOnClickListener(v -> {
                    paymentType = PaymentType.BITCOIN;
                    buyCreditsDialog.show();
                });
                break;
            case 2:
                buyLogoItemMenu.setImageDrawable(context.getDrawable(R.drawable.peso_logo));
                buyMenuTextView.setText("Buy Credits thru Cash");

                itemView.setOnClickListener(v -> {
                    paymentType = PaymentType.CASH;
                    buyCreditsDialog.show();
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return menuIndex.size();
    }



}
