package com.spectric.erpsystem.Models.Employee;

public class EmployeeAddModel {
    private String name;
    private String email;
    private String address;
    private String mobile;
    private String salary;
    private String joiningdate;
    //    private String password;
    private String status;
    private String role;
    private String pushId;

    public EmployeeAddModel(String name, String email, String address, String mobile, String salary, String joiningdate, String status, String role, String pushId) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.mobile = mobile;
        this.salary = salary;
        this.joiningdate = joiningdate;
        this.status = status;
        this.role = role;
        this.pushId = pushId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJoiningdate() {
        return joiningdate;
    }

    public void setJoiningdate(String joiningdate) {
        this.joiningdate = joiningdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

}