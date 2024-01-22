package com.ericsospedra.listshoppingfirebase.models;


import com.ericsospedra.listshoppingfirebase.utils.DateUtil;

import java.util.Date;

public class ShoppingList {
    private int id;
    private String name;
    private String image;
    private Date date;
    private int user;

    public ShoppingList(int id, String name, String image, String date, int user) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = DateUtil.createDateFromString(date);
        this.user = user;
    }

    public ShoppingList(String name, String image, String date, int user) {
        this.name = name;
        this.image = image;
        this.date = DateUtil.createDateFromString(date);;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
