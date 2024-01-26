package com.ericsospedra.listshoppingfirebase.models;

public class LinesOfShoppingList {
    private String id;
    private String image;
    private String name;

    public LinesOfShoppingList() {
    }

    public LinesOfShoppingList(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
