package com.alessandro.sia.model;

import java.io.Serializable;

public class Rating implements Serializable{
    private int id;
    private int rate;

    public Rating(){}

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
