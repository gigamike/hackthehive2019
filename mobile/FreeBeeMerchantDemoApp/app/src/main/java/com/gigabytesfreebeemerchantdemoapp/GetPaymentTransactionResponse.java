package com.gigabytesfreebeemerchantdemoapp;

import java.util.Objects;

public class GetPaymentTransactionResponse {

    private String userId;
    private String transactionCode;
    private String status;
    private int creditsBuyed;
    private String transactionDateTime;
    private String responseStatus;

    public String getUserId() {
        return userId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getStatus() {
        return status;
    }

    public int getCreditsBuyed() {
        return creditsBuyed;
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
        GetPaymentTransactionResponse that = (GetPaymentTransactionResponse) o;
        return creditsBuyed == that.creditsBuyed &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(transactionCode, that.transactionCode) &&
                Objects.equals(status, that.status) &&
                Objects.equals(transactionDateTime, that.transactionDateTime) &&
                Objects.equals(responseStatus, that.responseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, transactionCode, status, creditsBuyed, transactionDateTime, responseStatus);
    }

    @Override
    public String toString() {
        return "GetPaymentTransactionResponse{" +
                "userId='" + userId + '\'' +
                ", transactionCode='" + transactionCode + '\'' +
                ", status='" + status + '\'' +
                ", creditsBuyed=" + creditsBuyed +
                ", transactionDateTime='" + transactionDateTime + '\'' +
                ", responseStatus='" + responseStatus + '\'' +
                '}';
    }
}

