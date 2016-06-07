package com.alessandro.sia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class User implements Serializable{
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String competence;
    private String city;
    private String address;
    private String phone;
    private String description;
    private Set<Rating> ratingsForUserReceiver;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Rating> getRatingsForUserReceiver() {
        return ratingsForUserReceiver;
    }

    public void setRatingsForUserReceiver(Set<Rating> ratingsForUserReceiver) {
        this.ratingsForUserReceiver = ratingsForUserReceiver;
    }
}
