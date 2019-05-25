package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class GetPaymentTransactionResponse {
    private long userId;

    private String transactionCode;

    private Status status;

    private int creditsBuyed;

    private LocalDateTime transactionDateTime;

    private ResponseStatus responseStatus;

    public GetPaymentTransactionResponse(long userId, String transactionCode, Status status, int creditsBuyed, LocalDateTime transactionDateTime, ResponseStatus responseStatus) {
        this.userId = userId;
        this.transactionCode = transactionCode;
        this.status = status;
        this.creditsBuyed = creditsBuyed;
        this.transactionDateTime = transactionDateTime;
        this.responseStatus = responseStatus;
    }

    public long getUserId() {
        return userId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public Status getStatus() {
        return status;
    }

    public int getCreditsBuyed() {
        return creditsBuyed;
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
        GetPaymentTransactionResponse that = (GetPaymentTransactionResponse) o;
        return userId == that.userId &&
                creditsBuyed == that.creditsBuyed &&
                Objects.equals(transactionCode, that.transactionCode) &&
                status == that.status &&
                Objects.equals(transactionDateTime, that.transactionDateTime) &&
                responseStatus == that.responseStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, transactionCode, status, creditsBuyed, transactionDateTime, responseStatus);
    }

    @Override
    public String toString() {
        return "GetPaymentTransactionResponse{" +
                "userId=" + userId +
                ", transactionCode='" + transactionCode + '\'' +
                ", status=" + status +
                ", creditsBuyed=" + creditsBuyed +
                ", transactionDateTime=" + transactionDateTime +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
