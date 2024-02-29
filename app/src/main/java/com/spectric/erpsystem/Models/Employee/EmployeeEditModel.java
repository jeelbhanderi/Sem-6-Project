package com.spectric.erpsystem.Models.Employee;

public class EmployeeEditModel {
    private String name;
    private String address;
    private String mobile;
    private String salary;
    private String joiningdate;

    public EmployeeEditModel(String name, String address, String mobile, String salary, String joiningdate) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.salary = salary;
        this.joiningdate = joiningdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
