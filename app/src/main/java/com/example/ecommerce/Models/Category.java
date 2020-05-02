package com.example.ecommerce.Models;

public class Category {
    private String CategoryName,image;

    public Category() {
    }

    public Category(String categoryName, String image) {
        CategoryName = categoryName;
        this.image = image;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
