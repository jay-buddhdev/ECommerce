package com.example.ecommerce.Models;

public class Orders_Admin_Details
{
    private String Amount,address,date,time,type,Status;

    public Orders_Admin_Details() {
    }



    public Orders_Admin_Details(String amount, String address, String date, String time, String type, String status) {
        Amount = amount;
        this.address = address;
        this.date = date;
        this.time = time;
        this.type = type;
        Status = status;
    }
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
