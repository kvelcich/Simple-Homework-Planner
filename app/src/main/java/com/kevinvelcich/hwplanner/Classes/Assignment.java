package com.kevinvelcich.hwplanner.Classes;

import java.util.Date;

public class Assignment {
    private int _id;
    private String _pClass;
    private String _name;
    private String _description;
    private int _year;
    private int _month;
    private int _day;

    public Assignment() {
    }

    public Assignment(int id, String pClass, String name, String description) {
        this._id = id;
        this._pClass = pClass;
        this._name = name;
        this._description = description;
    }

    public Assignment(String name, String description, int year, int month, int day) {
        this._name = name;
        this._description = description;
        this._year = year;
        this._month = month;
        this._day = day;
    }

    public Assignment(String pClass, String name, String description) {
        this._pClass = pClass;
        this._name = name;
        this._description = description;
    }

    public Assignment(String name, String description) {
        this._name = name;
        this._description = description;
    }

    public Assignment(String pClass) {
        this._pClass = pClass;
    }

    public void setID(int id) {
        this._id = id;
    }
    public int getID() {
        return this._id;
    }
    public void setParentClass(String pClass) {
        this._pClass = pClass;
    }
    public String getParentClass() {
        return this._pClass;
    }
    public void setName(String name) {
        this._name = name;
    }
    public String getName() {
        return this._name;
    }
    public void setDescription(String description) {
        this._description = description;
    }
    public String getDescription() {
        return this._description;
    }
    public void setDate(int year, int month, int day) {
        this._year = year;
        this._month = month;
        this._day = day;
    }
    public void setYear(int year) {
        this._year = year;
    }
    public void setMonth(int month) {
        this._month = month;
    }
    public void setDay(int day) {
        this._day = day;
    }
    public int getYear(){
        return this._year;
    }
    public int getMonth(){
        return this._month;
    }
    public int getDay() {
        return this._day;
    }
}
