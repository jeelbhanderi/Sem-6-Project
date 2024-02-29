package com.spectric.erpsystem.Models.Client;

public class ClientEditModel {
    String Name, Email, Mobile, ProjectName, ProjectDescription,
            TotalPrice, AdvancePayment, RemainingPayment, Language,
            SubmissionDate, ProjectManagerEmail;

    public ClientEditModel(String name, String email, String mobile, String projectName, String projectDescription, String totalPrice, String advancePayment, String remainingPayment, String language, String submissionDate, String projectManagerEmail) {
        Name = name;
        Email = email;
        Mobile = mobile;
        ProjectName = projectName;
        ProjectDescription = projectDescription;
        TotalPrice = totalPrice;
        AdvancePayment = advancePayment;
        RemainingPayment = remainingPayment;
        Language = language;
        SubmissionDate = submissionDate;
        ProjectManagerEmail = projectManagerEmail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectDescription() {
        return ProjectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        ProjectDescription = projectDescription;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
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

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        SubmissionDate = submissionDate;
    }

    public String getProjectManagerEmail() {
        return ProjectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        ProjectManagerEmail = projectManagerEmail;
    }
}
