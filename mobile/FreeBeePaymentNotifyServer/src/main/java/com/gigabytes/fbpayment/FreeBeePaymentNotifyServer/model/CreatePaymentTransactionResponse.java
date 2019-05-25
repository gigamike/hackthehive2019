package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class CreatePaymentTransactionResponse {

    private String transactionCode;
    private LocalDateTime transactionDateTime;
    private ResponseStatus responseStatus;

    public CreatePaymentTransactionResponse(String transactionCode, LocalDateTime transactionDate, ResponseStatus responseStatus) {
        this.transactionCode = transactionCode;
        this.transactionDateTime = transactionDate;
        this.responseStatus = responseStatus;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePaymentTransactionResponse that = (CreatePaymentTransactionResponse) o;
        return Objects.equals(transactionCode, that.transactionCode) &&
                Objects.equals(transactionDateTime, that.transactionDateTime) &&
                responseStatus == that.responseStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionCode, transactionDateTime, responseStatus);
    }

    @Override
    public String toString() {
        return "CreatePaymentTransactionResponse{" +
                "transactionCode='" + transactionCode + '\'' +
                ", transactionDate=" + transactionDateTime +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
