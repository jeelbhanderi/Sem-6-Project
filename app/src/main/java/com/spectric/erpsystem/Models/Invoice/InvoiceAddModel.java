package com.spectric.erpsystem.Models.Invoice;

public class InvoiceAddModel {
    String invoiceNo, clientName, date, client_address, client_phoneNumber, amount, cgst,client_email;

    public InvoiceAddModel(String invoiceNo, String clientName, String date, String client_address, String client_phoneNumber, String amount, String cgst,String client_email) {
        this.invoiceNo = invoiceNo;
        this.clientName = clientName;
        this.date = date;
        this.client_address = client_address;
        this.client_phoneNumber = client_phoneNumber;
        this.amount = amount;
        this.cgst = cgst;
        this.client_email = client_email;
    }


    public String getClient_email() {
        return client_email;
    }
    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getClient_phoneNumber() {
        return client_phoneNumber;
    }

    public void setClient_phoneNumber(String client_phoneNumber) {
        this.client_phoneNumber = client_phoneNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

}