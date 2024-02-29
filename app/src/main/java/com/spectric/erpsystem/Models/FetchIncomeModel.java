package com.spectric.erpsystem.Models;

public class FetchIncomeModel {

        private String advancePayment;
        private String currentDate;
        private String customerId;
        private String customerType;
        private String remainingPayment;
        private String totalAmount;

        // Default constructor (required by Firebase)


    public FetchIncomeModel() {
    }

    // Getters and setters
        public String getAdvancePayment() {
            return advancePayment;
        }

        public void setAdvancePayment(String advancePayment) {
            this.advancePayment = advancePayment;
        }

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

        public String getRemainingPayment() {
            return remainingPayment;
        }

        public void setRemainingPayment(String remainingPayment) {
            this.remainingPayment = remainingPayment;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
    }


