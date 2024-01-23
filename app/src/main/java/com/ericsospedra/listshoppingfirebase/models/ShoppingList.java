package com.ericsospedra.listshoppingfirebase.models;


import com.ericsospedra.listshoppingfirebase.utils.DateUtil;

import java.util.Date;

public class ShoppingList {
    private String name;
    private String image;
    private Date date;
    private int user;

    private int cantidadProductos;

    public ShoppingList() {
    }

    public ShoppingList(String name, String image, Date date, int cantidadProductos) {
        this.name = name;
        this.image = image;
        this.date = date;
        this.cantidadProductos = cantidadProductos;
    }

    public ShoppingList(String name, String image, String date, int user, int cantidadProductos) {
        this.name = name;
        this.image = image;
        this.date = DateUtil.createDateFromString(date);
        this.user = user;
        this.cantidadProductos = cantidadProductos;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
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

    public String getDate() {
        return date.toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
