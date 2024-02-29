package com.spectric.erpsystem.Models;

public class ProjectDetsilsFetch {
    String ProjectName;
    String ownerEmail;
    String ProjectDescription;
    String TotalPrice;
    String AdvancePayment;
    String RemainingPayment;
    String Status;
    String SubmitionDate;
    String Progress;
    String field;
    String OwnerType;
    String HandlerEMail;

    public String getHandlerEMail() {
        return HandlerEMail;
    }

    public void setHandlerEMail(String handlerEMail) {
        HandlerEMail = handlerEMail;
    }

    public ProjectDetsilsFetch() {

    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSubmitionDate() {
        return SubmitionDate;
    }

    public void setSubmitionDate(String submitionDate) {
        SubmitionDate = submitionDate;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(String ownerType) {
        OwnerType = ownerType;
    }
}
