package com.gigabytes.freebee.credits.models;

import java.util.Objects;

public class CreatePaymentTransactionResponse {

    private String transactionCode;
    private String transactionDateTime;
    private String responseStatus;

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePaymentTransactionResponse that = (CreatePaymentTransactionResponse) o;
        return Objects.equals(transactionCode, that.transactionCode) &&
                Objects.equals(transactionDateTime, that.transactionDateTime) &&
                Objects.equals(responseStatus, that.responseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionCode, transactionDateTime, responseStatus);
    }

    @Override
    public String toString() {
        return "CreatePaymentTransactionResponse{" +
                "transactionCode='" + transactionCode + '\'' +
                ", transactionDateTime=" + transactionDateTime +
                ", responseStatus='" + responseStatus + '\'' +
                '}';
    }
}
