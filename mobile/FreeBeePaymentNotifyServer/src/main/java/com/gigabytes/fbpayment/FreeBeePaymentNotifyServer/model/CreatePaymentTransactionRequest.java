package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import java.util.Objects;

public class CreatePaymentTransactionRequest {

    private int userId;
    private int creditsBuyed;

    public CreatePaymentTransactionRequest(int userId, int creditsBuyed) {
        this.userId = userId;
        this.creditsBuyed = creditsBuyed;
    }

    public int getUserId() {
        return userId;
    }

    public int getCreditsBuyed() {
        return creditsBuyed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePaymentTransactionRequest that = (CreatePaymentTransactionRequest) o;
        return userId == that.userId &&
                creditsBuyed == that.creditsBuyed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, creditsBuyed);
    }

    @Override
    public String toString() {
        return "CreatePaymentTransactionRequest{" +
                "userId=" + userId +
                ", creditsBuyed=" + creditsBuyed +
                '}';
    }
}
