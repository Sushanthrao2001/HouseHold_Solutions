package com.example.householdsolutions;


public class dataholder_order {

    String Service_address,Paymentmethod,Assigned_Date,customerid,name,status;

    public dataholder_order(String service_address, String paymentmethod, String assigned_Date, String customerid, String name, String status) {
        Service_address = service_address;
        Paymentmethod = paymentmethod;
        Assigned_Date = assigned_Date;
        this.customerid = customerid;
        this.name = name;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService_address() {
        return Service_address;
    }

    public void setService_address(String service_address) {
        Service_address = service_address;
    }

    public String getPaymentmethod() {
        return Paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        Paymentmethod = paymentmethod;
    }

    public String getAssigned_Date() {
        return Assigned_Date;
    }

    public void setAssigned_Date(String assigned_Date) {
        Assigned_Date = assigned_Date;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
}
