package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.controller;

import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.*;
import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.ResponseStatus.SUCCESS;


@RestController
@RequestMapping("/api")
public class MerchantPaymentController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @PostMapping("/transaction")
    public CreatePaymentTransactionResponse createPaymentTransaction(@RequestBody CreatePaymentTransactionRequest createPaymentTransactionRequest) {

        PaymentTransaction paymentTransaction = paymentTransactionService.createPaymentTransaction(createPaymentTransactionRequest);

        CreatePaymentTransactionResponse createPaymentTransactionResponse = new CreatePaymentTransactionResponse(paymentTransaction.getTransactionCode(),
                                                                                                                 paymentTransaction.getDateCreated(),
                                                                                                                 SUCCESS);

        return createPaymentTransactionResponse;
    }

    @PostMapping("/transaction/{transactionCode}")
    public CreatePaymentTransactionResponse createPaymentTransactionWithTransactionCode(@RequestBody CreatePaymentTransactionRequest createPaymentTransactionRequest,
                                                                                        @PathVariable("transactionCode") String transactionCode) {

        PaymentTransaction paymentTransaction = paymentTransactionService.createPaymentTransactionWithTransactionCode(createPaymentTransactionRequest, transactionCode);

        CreatePaymentTransactionResponse createPaymentTransactionResponse = new CreatePaymentTransactionResponse(paymentTransaction.getTransactionCode(),
                                                                                                                 paymentTransaction.getDateCreated(),
                                                                                                                 SUCCESS);

        return createPaymentTransactionResponse;
    }

    @GetMapping("/transaction/{transactionCode}")
    public GetPaymentTransactionResponse getPaymentTransaction(@PathVariable("transactionCode") String transactionCode) {

        PaymentTransaction paymentTransaction = paymentTransactionService.getPaymentTransaction(transactionCode);

        return new GetPaymentTransactionResponse(paymentTransaction.getUserId(),
                                                 paymentTransaction.getTransactionCode(),
                                                 paymentTransaction.getStatus(),
                                                 paymentTransaction.getCreditsBuyed(),
                                                 paymentTransaction.getDateCreated(),
                                                 SUCCESS);
    }

    @GetMapping("/transaction/complete/{transactionCode}")
    public CompletePaymentTransactionResponse completePaymentTransaction(@PathVariable("transactionCode") String transactionCode) {
        paymentTransactionService.completePaymentTransaction(transactionCode);
        return new CompletePaymentTransactionResponse(SUCCESS);
    }
}
