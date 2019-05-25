package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.service;

import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.CreatePaymentTransactionRequest;
import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.PaymentTransaction;
import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.repository.PaymentTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

import static com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.Status.PENDING;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    private String generateTransactionCode(CreatePaymentTransactionRequest createPaymentTransactionRequest, LocalDateTime currentTransactionDateTime) {
        String userId = Integer.toString(createPaymentTransactionRequest.getUserId());
        String creditsBuyed = Integer.toString(createPaymentTransactionRequest.getCreditsBuyed());

        String rawTransactionCode = currentTransactionDateTime + userId + creditsBuyed;
        return Base64.getEncoder().encodeToString(rawTransactionCode.getBytes());
    }

    @Override
    public PaymentTransaction createPaymentTransaction(CreatePaymentTransactionRequest createPaymentTransactionRequest) {

        LocalDateTime currentTransactionDateTime = LocalDateTime.now();
        String transactionCode = generateTransactionCode(createPaymentTransactionRequest, currentTransactionDateTime);

        PaymentTransaction newPaymentTransaction =
                new PaymentTransaction(createPaymentTransactionRequest.getUserId(),
                                       transactionCode,
                                       PENDING,
                                       createPaymentTransactionRequest.getCreditsBuyed(),
                                       currentTransactionDateTime);

        paymentTransactionRepository.save(newPaymentTransaction);

        return newPaymentTransaction;
    }

    @Override
    public PaymentTransaction createPaymentTransactionWithTransactionCode(CreatePaymentTransactionRequest createPaymentTransactionRequest, String transactionCode) {

        LocalDateTime currentTransactionDateTime = LocalDateTime.now();

        PaymentTransaction newPaymentTransaction =
                new PaymentTransaction(createPaymentTransactionRequest.getUserId(),
                                       transactionCode,
                                       PENDING,
                                       createPaymentTransactionRequest.getCreditsBuyed(),
                                       currentTransactionDateTime);

        paymentTransactionRepository.save(newPaymentTransaction);

        return newPaymentTransaction;
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String transactionCode) {
        return paymentTransactionRepository.getPaymentTransactionDetailsByTransactionCode(transactionCode);
    }

    @Override
    public void completePaymentTransaction(String transactionCode) {
        paymentTransactionRepository.completePaymentTransaction(transactionCode);
        // code to call mik api to top up load
    }
}
