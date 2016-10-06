package com.kevinvelcich.hwplanner.Classes;

public class Class {

    private int _id;
    private String _classname;
    private int _quantity;
    private int _color;

    public Class() {
    }

    public Class(int id, String classname, int quantity, int color) {
        this._id = id;
        this._classname = classname;
        this._quantity = quantity;
        this._color = color;
    }

    public Class(String classname, int quantity, int color) {
        this._classname = classname;
        this._quantity = quantity;
        this._color = color;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setClassName(String classname) {
        this._classname = classname;
    }

    public String getClassName() {
        return this._classname;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }

    public void setColor(int color) {
        this._color = color;
    }

    public int getColor() {
        return this._color;
    }
}
