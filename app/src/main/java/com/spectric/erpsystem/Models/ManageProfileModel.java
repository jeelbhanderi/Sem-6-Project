package com.spectric.erpsystem.Models;

public class ManageProfileModel {
    private String address;
    private String companyName;
    private String companyPan;
    private String email;
    private String gstNo;
    private String hscSac;
    private String mobile;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPan() {
        return companyPan;
    }

    public void setCompanyPan(String companyPan) {
        this.companyPan = companyPan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getHscSac() {
        return hscSac;
    }

    public void setHscSac(String hscSac) {
        this.hscSac = hscSac;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ManageProfileModel(String address, String companyName, String companyPan, String email, String gstNo, String hscSac, String mobile, String password, String status, String role, String website) {
        this.address = address;
        this.companyName = companyName;
        this.companyPan = companyPan;
        this.email = email;
        this.gstNo = gstNo;
        this.hscSac = hscSac;
        this.mobile = mobile;
        this.password = password;
        this.status = status;
        this.role = role;
        this.website = website;
    }

    private String password;
    private String status;
    private String role;
    private String website;
}
