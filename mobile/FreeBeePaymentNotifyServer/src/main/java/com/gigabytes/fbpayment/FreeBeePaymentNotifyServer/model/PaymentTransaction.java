package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payment_transaction")
@Access(AccessType.FIELD)
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "credits_buyed")
    private int creditsBuyed;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    private PaymentTransaction() {}

    public PaymentTransaction(long userId, String transactionCode, Status status, int creditsBuyed, LocalDateTime dateCreated) {
        this.userId = userId;
        this.transactionCode = transactionCode;
        this.status = status;
        this.creditsBuyed = creditsBuyed;
        this.dateCreated = dateCreated;
    }

    public long getTransactionId() {
        return transactionId;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentTransaction that = (PaymentTransaction) o;
        return transactionId == that.transactionId &&
                userId == that.userId &&
                creditsBuyed == that.creditsBuyed &&
                Objects.equals(transactionCode, that.transactionCode) &&
                status == that.status &&
                Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, userId, transactionCode, status, creditsBuyed, dateCreated);
    }

    @Override
    public String toString() {
        return "PaymentTransaction{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", transactionCode='" + transactionCode + '\'' +
                ", status=" + status +
                ", creditsBuyed=" + creditsBuyed +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
