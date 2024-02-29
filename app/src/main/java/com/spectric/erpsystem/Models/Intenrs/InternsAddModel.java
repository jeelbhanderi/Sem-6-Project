package com.spectric.erpsystem.Models.Intenrs;

public class InternsAddModel {

    private String fullName;
    private String field;
    private String gender;
    private String contactNo;
    private String emailAddress;
    private String fees;
    private String employeeEmail;
    private String joiningDate;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public InternsAddModel(String fullName, String field, String gender, String contactNo, String emailAddress, String fees, String employeeEmail, String joiningDate, String endDate, String duration, String pushId) {
        this.fullName = fullName;
        this.field = field;
        this.gender = gender;
        this.contactNo = contactNo;
        this.emailAddress = emailAddress;
        this.fees = fees;
        this.employeeEmail = employeeEmail;
        this.joiningDate = joiningDate;
        this.endDate = endDate;
        this.duration = duration;
        PushId = pushId;
    }

    private String endDate;
    private String duration;
    private String PushId;
}