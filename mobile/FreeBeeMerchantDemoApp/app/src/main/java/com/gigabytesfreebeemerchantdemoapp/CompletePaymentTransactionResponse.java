package com.gigabytesfreebeemerchantdemoapp;

import java.util.Objects;

public class CompletePaymentTransactionResponse {
    private String responseStatus;

    public String getResponseStatus() {
        return responseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletePaymentTransactionResponse that = (CompletePaymentTransactionResponse) o;
        return Objects.equals(responseStatus, that.responseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus);
    }

    @Override
    public String toString() {
        return "CompletePaymentTransactionResponse{" +
                "responseStatus='" + responseStatus + '\'' +
                '}';
    }
}
