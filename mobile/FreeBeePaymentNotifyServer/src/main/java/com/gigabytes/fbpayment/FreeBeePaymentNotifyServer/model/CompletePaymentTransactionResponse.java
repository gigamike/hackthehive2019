package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import java.util.Objects;

public class CompletePaymentTransactionResponse {

    private ResponseStatus responseStatus;

    public CompletePaymentTransactionResponse(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletePaymentTransactionResponse that = (CompletePaymentTransactionResponse) o;
        return responseStatus == that.responseStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus);
    }

    @Override
    public String toString() {
        return "CompletePaymentTransactionResponse{" +
                "responseStatus=" + responseStatus +
                '}';
    }
}
