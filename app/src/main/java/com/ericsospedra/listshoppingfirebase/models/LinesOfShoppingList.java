package com.ericsospedra.listshoppingfirebase.models;

public class LinesOfShoppingList {
    private int id;
    private String image;
    private String name;

    public LinesOfShoppingList() {
    }

    public LinesOfShoppingList(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
