package com.spectric.erpsystem.Models.Company;

public class CompanyRegisterModel {
    String ComapnyName,Mobile,Address,Email,Website,GstNo,HscSac,CompanyPan,Status,Role,Password;

    public CompanyRegisterModel(String comapnyName, String mobile, String address, String email, String website, String gstNo, String hscSac, String companyPan, String status, String role, String password) {
        ComapnyName = comapnyName;
        Mobile = mobile;
        Address = address;
        Email = email;
        Website = website;
        GstNo = gstNo;
        HscSac = hscSac;
        CompanyPan = companyPan;
        Status = status;
        Role = role;
        Password = password;
    }

    public String getComapnyName() {
        return ComapnyName;
    }

    public void setComapnyName(String comapnyName) {
        ComapnyName = comapnyName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getGstNo() {
        return GstNo;
    }

    public void setGstNo(String gstNo) {
        GstNo = gstNo;
    }

    public String getHscSac() {
        return HscSac;
    }

    public void setHscSac(String hscSac) {
        HscSac = hscSac;
    }

    public String getCompanyPan() {
        return CompanyPan;
    }

    public void setCompanyPan(String companyPan) {
        CompanyPan = companyPan;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}