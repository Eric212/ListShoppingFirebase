package com.ericsospedra.listshoppingfirebase.models;

public class Product {
    private String name;
    private String image;

    public Product() {
    }

    public Product(String name, String image) {
        this.name = name;
        this.image = image;
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

    @Override
    public String toString() {
        return "Product{" +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
