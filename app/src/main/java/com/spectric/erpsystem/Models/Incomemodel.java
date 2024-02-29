package com.spectric.erpsystem.Models;

public class Incomemodel {

    String CustomerType,CustomerId,TotalAmount,CurrentDate,AdvancePayment,RemainingPayment;

    public String getCustomerType() {
        return CustomerType;
    }

    public void setCustomerType(String customerType) {
        CustomerType = customerType;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getAdvancePayment() {
        return AdvancePayment;
    }

    public void setAdvancePayment(String advancePayment) {
        AdvancePayment = advancePayment;
    }

    public String getRemainingPayment() {
        return RemainingPayment;
    }

    public void setRemainingPayment(String remainingPayment) {
        RemainingPayment = remainingPayment;
    }

    public Incomemodel(String customerType, String customerId, String totalAmount, String currentDate, String advancePayment, String remainingPayment) {
        CustomerType = customerType;
        CustomerId = customerId;
        TotalAmount = totalAmount;
        CurrentDate = currentDate;
        AdvancePayment = advancePayment;
        RemainingPayment = remainingPayment;
    }

    public Incomemodel() {
    }
}
