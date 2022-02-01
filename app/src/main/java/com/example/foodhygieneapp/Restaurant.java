package com.example.foodhygieneapp;

public class Restaurant {

    private String apiID;
    private String name;
    private String rating;

    public Restaurant(String name, String apiID, String rating){
        this.name = name;
        this.apiID = apiID;
        this.rating = rating;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
