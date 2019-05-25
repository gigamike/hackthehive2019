package com.gigabytes.freebee.credits.models;

import java.util.Objects;

public class CreatePaymentTransactionRequest {

    private String userId;
    private int creditsBuyed;

    public CreatePaymentTransactionRequest(String userId, int creditsBuyed) {
        this.userId = userId;
        this.creditsBuyed = creditsBuyed;
    }

    public String getUserId() {
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
        return creditsBuyed == that.creditsBuyed &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, creditsBuyed);
    }

    @Override
    public String toString() {
        return "CreatePaymentTransactionRequest{" +
                "userId='" + userId + '\'' +
                ", creditsBuyed=" + creditsBuyed +
                '}';
    }
}
