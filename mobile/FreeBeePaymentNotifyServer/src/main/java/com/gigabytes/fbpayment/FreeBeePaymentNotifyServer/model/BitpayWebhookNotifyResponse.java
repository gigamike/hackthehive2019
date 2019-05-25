package com.gigabytes.fbpayment.FreeBeePaymentNotifyServer.model;

import java.util.Objects;

public class BitpayWebhookNotifyResponse {

    private static class BuyerFields {
        private String buyerEmail;

        public String getBuyerEmail() {
            return buyerEmail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BuyerFields that = (BuyerFields) o;
            return Objects.equals(buyerEmail, that.buyerEmail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(buyerEmail);
        }

        @Override
        public String toString() {
            return "BuyerFields{" +
                    "buyerEmail='" + buyerEmail + '\'' +
                    '}';
        }
    }

    private String id;
    private String url;
    private String status;
    private double price;
    private String currency;
    private String invoiceTime;
    private String expirationTime;
    private String currentTime;
    private boolean exceptionStatus;
    BuyerFields buyerFields;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getInvoiceTime() {
        return invoiceTime;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public boolean isExceptionStatus() {
        return exceptionStatus;
    }

    public BuyerFields getBuyerFields() {
        return buyerFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitpayWebhookNotifyResponse that = (BitpayWebhookNotifyResponse) o;
        return Double.compare(that.price, price) == 0 &&
                exceptionStatus == that.exceptionStatus &&
                Objects.equals(id, that.id) &&
                Objects.equals(url, that.url) &&
                Objects.equals(status, that.status) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(invoiceTime, that.invoiceTime) &&
                Objects.equals(expirationTime, that.expirationTime) &&
                Objects.equals(currentTime, that.currentTime) &&
                Objects.equals(buyerFields, that.buyerFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, status, price, currency, invoiceTime, expirationTime, currentTime, exceptionStatus, buyerFields);
    }

    @Override
    public String toString() {
        return "BitpayWebhookNotifyResponse{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", invoiceTime='" + invoiceTime + '\'' +
                ", expirationTime='" + expirationTime + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", exceptionStatus=" + exceptionStatus +
                ", buyerFields=" + buyerFields +
                '}';
    }
}
