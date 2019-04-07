package com.datacube.pratikhanchate.fitdata;

import java.sql.Time;

public class User {

    String user_id;
    String name;
    String email;
    String password;
    String phoneNumber;
    String steps;
    String total_idleTime;
    String Location_home;
    String Location_office;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getTotal_idleTime() {
        return total_idleTime;
    }

    public void setTotal_idleTime(String total_idleTime) {
        this.total_idleTime = total_idleTime;
    }

    public String getLocation_home() {
        return Location_home;
    }

    public void setLocation_home(String location_home) {
        Location_home = location_home;
    }

    public String getLocation_office() {
        return Location_office;
    }

    public void setLocation_office(String location_office) {
        Location_office = location_office;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
