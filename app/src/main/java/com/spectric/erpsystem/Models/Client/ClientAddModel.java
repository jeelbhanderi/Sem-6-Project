package com.spectric.erpsystem.Models.Client;

public class ClientAddModel {
    String name, email, mobile, projectName, projectDescription,
            totalPrice, advancePayment, remainingPayment, language,
            submissionDate, projectManagerEmail,PushId;

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(String advancePayment) {
        this.advancePayment = advancePayment;
    }

    public String getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(String remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }



    public ClientAddModel(String name, String email, String mobile, String projectName, String projectDescription, String totalPrice, String advancePayment, String remainingPayment, String language, String submissionDate, String projectManagerEmail,String pushId) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.totalPrice = totalPrice;
        this.advancePayment = advancePayment;
        this.remainingPayment = remainingPayment;
        this.language = language;
        this.submissionDate = submissionDate;
        this.projectManagerEmail = projectManagerEmail;
        this.PushId = pushId;

    }
}
