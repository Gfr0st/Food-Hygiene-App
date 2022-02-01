package com.example.foodhygieneapp;

public class Authority {

    private String apiID;
    private String name;
    private String region;

    public Authority(String name, String apiID, String region){
        this.name = name;
        this.apiID = apiID;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
