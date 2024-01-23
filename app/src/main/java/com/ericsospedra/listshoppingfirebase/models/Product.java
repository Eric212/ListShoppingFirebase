package com.ericsospedra.listshoppingfirebase.models;

public class Product {
    private String name;
    private String image;
    private int category;

    public Product() {
    }

    public Product(String name, String image, int category) {
        this.name = name;
        this.image = image;
        this.category = category;
    }

    public Product(String name, int category) {
        this.name = name;
        this.category = category;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", category=" + category +
                '}';
    }
}
