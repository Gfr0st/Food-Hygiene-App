package com.example.foodhygieneapp;

public class BusinessType {

    private String apiID;
    private String name;


    public BusinessType(String name, String apiID){
        this.name = name;
        this.apiID = apiID;
    }

    public String getApiID() {
        return apiID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApiID(String apiID) {
        this.apiID = apiID;
    }

    @Override
    public String toString() {
        return this.name;
    }
}