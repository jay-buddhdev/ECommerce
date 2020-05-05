package com.example.ecommerce.Models;

public class Orders_View
{
    private String id,Amount,Status,date;

    public Orders_View() {
    }

    public Orders_View(String id, String amount, String status, String date) {
        this.id = id;
        Amount = amount;
        Status = status;
        this.date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
