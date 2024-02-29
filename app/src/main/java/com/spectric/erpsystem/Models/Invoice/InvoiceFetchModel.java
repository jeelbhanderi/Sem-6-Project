package com.spectric.erpsystem.Models.Invoice;

public class InvoiceFetchModel {
//    String amount, cgst, clientName, client_address, client_phoneNumber, date, email, invoiceNo;
    String date, amount, clientName, client_phoneNumber, client_email, cgst, client_address, invoiceNo;

    public InvoiceFetchModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClient_phoneNumber() {
        return client_phoneNumber;
    }

    public void setClient_phoneNumber(String client_phoneNumber) {
        this.client_phoneNumber = client_phoneNumber;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}