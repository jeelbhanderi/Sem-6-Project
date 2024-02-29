package com.spectric.erpsystem.Models;public class ExpenseModel {
    private String currentDate;
    private String customerId;
    private String customerType;
    private String totalAmount;


    public ExpenseModel(String currentDate, String customerId, String customerType, String totalAmount) {
        this.currentDate = currentDate;
        this.customerId = customerId;
        this.customerType = customerType;
        this.totalAmount = totalAmount;
    }

    // Add getters and setters for each field
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
