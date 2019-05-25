package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.service;

import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.CreatePaymentTransactionRequest;
import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.PaymentTransaction;

public interface PaymentTransactionService {
    PaymentTransaction createPaymentTransaction(CreatePaymentTransactionRequest createPaymentTransactionRequest);
    PaymentTransaction createPaymentTransactionWithTransactionCode(CreatePaymentTransactionRequest createPaymentTransactionRequest, String transactionCode);
    PaymentTransaction getPaymentTransaction(String transactionCode);
    void completePaymentTransaction(String transactionCode);
}
