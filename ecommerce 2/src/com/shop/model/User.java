package com.shop.model;

/**
 * User — stores customer account details.
 * Java concept: Encapsulation, basic data modelling
 */
public class User {
    private String username;
    private String password;
    private String email;
    private String address;

    public User(String username, String password, String email, String address) {
        this.username = username;
        this.password = password;
        this.email    = email;
        this.address  = address;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }
    public String getAddress()  { return address; }
    public void   setEmail(String e)   { email = e; }
    public void   setAddress(String a) { address = a; }
}
