package com.example.ecommerce.Models;

public class Orders_track
{
    private String key,Amount,Status,date,time,type,address;

    public Orders_track() {
    }

    public Orders_track(String key, String amount, String status, String date, String time, String type, String address) {
        this.key = key;
        Amount = amount;
        Status = status;
        this.date = date;
        this.time = time;
        this.type = type;
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
