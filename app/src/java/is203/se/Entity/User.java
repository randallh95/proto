/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Entity;

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
    
    //gender to be small caps
    public User(String macAddress, String name, String password, String email, char gender) {
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

    @Override
    public String toString() {
        return "User{" + "macAddress=" + macAddress + ", name=" + name + ", password=" + password + ", email=" + email + ", gender=" + gender + '}';
    }
    
    public boolean validateUser(String enteredPassword){
        return enteredPassword.equals(this.password);
    }
}
