package is203.se.Entity;


import java.util.ArrayList;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bernice
 */
public class User {
    private String macAddress;
    private String name;
    private String password;
    private String email;
    private char gender;
    private long duration;
    private ArrayList<Interval> intervalArrList;
    
    public User(String macAddress, String name, String password, String email, char gender){
        this.macAddress = macAddress;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }
    
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<Interval> getIntervalArrList() {
        return intervalArrList;
    }

    public void setIntervalArrList(ArrayList<Interval> intervalArrList) {
        this.intervalArrList = intervalArrList;
    }
}
