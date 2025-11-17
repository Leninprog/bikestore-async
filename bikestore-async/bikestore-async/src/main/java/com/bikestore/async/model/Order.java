package com.bikestore.async.model;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerEmail;
    private double amount;
    private String paymentStatus; // NEW, PAID, FAILED
    private int retryCount;

    public Order() { }

    public Order(String orderId, String customerEmail, double amount) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.paymentStatus = "NEW";
        this.retryCount = 0;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
