package com.alessandro.sia.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Appointment implements Serializable {
    private int id;
    private String name;
    private String description;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    private String dateStart;
    private String dateEnd;

    public ArrayList<Slot> getSlotArrayList() {
        return slotArrayList;
    }

    public void setSlotArrayList(ArrayList<Slot> slotArrayList) {
        this.slotArrayList = slotArrayList;
    }

    private ArrayList<Slot> slotArrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
