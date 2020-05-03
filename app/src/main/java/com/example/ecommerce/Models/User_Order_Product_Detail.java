package com.example.ecommerce.Models;

public class User_Order_Product_Detail {
    private String image,pname,price,quantity;

    public User_Order_Product_Detail() {
    }

    public User_Order_Product_Detail(String image, String pname, String price, String quantity) {
        this.image = image;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
