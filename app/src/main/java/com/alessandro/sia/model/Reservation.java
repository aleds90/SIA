package com.alessandro.sia.model;

public class Reservation {

    private int id;
    private String status;

    public Reservation(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
