package com.example.ecommerce.Models;

public class Orders_View
{
    private String id,Amount,status,date;

    public Orders_View() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Orders_View(String id, String amount, String status, String date) {
        this.id = id;
        Amount = amount;
        this.status = status;
        this.date = date;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
