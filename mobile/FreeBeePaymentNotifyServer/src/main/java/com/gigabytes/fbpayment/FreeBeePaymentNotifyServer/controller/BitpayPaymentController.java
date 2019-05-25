package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.controller;

import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model.BitpayWebhookNotifyResponse;
import com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class BitpayPaymentController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @GetMapping("/bitpayCompletePaymentRedirect")
    public String bitpayCompletePaymentRedirect() {
        return "bitpayCompletePaymentRedirect";
    }

    @RequestMapping(value = "/bitpayInvoiceWebhookNotify", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void bitpayInvoiceWebhookNotify(@RequestBody BitpayWebhookNotifyResponse bitpayWebhookNotifyResponse) {
        System.out.println("bitpay response: " + bitpayWebhookNotifyResponse);

        if(bitpayWebhookNotifyResponse.getStatus().equals("paid")) {
            System.out.println("PAID!!");
            String transactionCode = bitpayWebhookNotifyResponse.getId();
            paymentTransactionService.completePaymentTransaction(transactionCode);
        }
    }

}
