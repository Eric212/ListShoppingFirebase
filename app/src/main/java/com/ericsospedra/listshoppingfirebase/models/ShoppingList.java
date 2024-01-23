package com.ericsospedra.listshoppingfirebase.models;


import com.ericsospedra.listshoppingfirebase.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShoppingList {
    private String name;
    private String image;
    private long date;
    private int user;

    private int cantidadProductos;

    public ShoppingList() {
    }

    public ShoppingList(String name, String image, long date, int cantidadProductos) {
        this.name = name;
        this.image = image;
        this.date = date;
        this.cantidadProductos = cantidadProductos;
    }

    public ShoppingList(String name, String image, long date, int user, int cantidadProductos) {
        this.name = name;
        this.image = image;
        this.date = date;
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

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
