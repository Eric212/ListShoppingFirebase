package com.ericsospedra.listshoppingfirebase.models;


import java.util.List;

public class Category {
    private String name;
    private String image;

    private List<Product> productList;

    public Category() {
    }

    public Category(String name, String image, List<Product> productList) {
        this.name = name;
        this.image = image;
        this.productList = productList;
    }

    public Category(String name, List<Product> productList) {
        this.name = name;
        this.productList = productList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
