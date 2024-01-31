package com.ericsospedra.listshoppingfirebase.models;

public class LinesOfShoppingList {
    private String id;
    private String image;
    private String name;
    private boolean buy;

    public LinesOfShoppingList() {
    }

    public LinesOfShoppingList(String id, String name, String image, boolean buy) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.buy = buy;
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

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean isBuy) {
        this.buy = isBuy;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
